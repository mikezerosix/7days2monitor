package org.mikezerosix.telnet;

import com.google.common.base.Charsets;
import org.apache.commons.net.telnet.*;
import org.mikezerosix.comet.CometMessage;
import org.mikezerosix.comet.CometSharedMessageQueue;
import org.mikezerosix.comet.MessageTarget;
import org.mikezerosix.entities.ConnectionSettings;
import org.mikezerosix.telnet.commands.RepeatingCommand;
import org.mikezerosix.telnet.commands.TelnetCommand;
import org.mikezerosix.telnet.handlers.ServerGreetingHandler;
import org.mikezerosix.telnet.handlers.TelnetOutputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;

/**
 * http://www.java2s.com/Code/Java/Network-Protocol/ExampleofuseofTelnetClient.htm
 */
public class TelnetRunner extends Thread implements TelnetNotificationHandler {
    public static final String PLEASE_ENTER_PASSWORD = "Please enter password:";
    public static final String WRONG_PASSWORD = "Password incorrect, please enter password:";
    public static final long connectionTimeoutSeconds = 10;
    public static final long waitTime = 500;
    private static final Logger log = LoggerFactory.getLogger(TelnetRunner.class);
    private static final String LOGON_SUCCESSFUL = "Logon successful.";
    private static TelnetRunner instance = null;
    private final List<TelnetOutputHandler> handlers = new ArrayList<>();
    private final DelayQueue<TelnetCommand> commands = new DelayQueue<>();
    private final TelnetClient telnet = new TelnetClient();
    private BufferedInputStream input = null;
    private PrintStream output = null;
    private TelnetCommand runningCommand = null;
    private ServerInformation serverInformation = new ServerInformation();
    private ConnectionSettings connectionSettings;
    private CometSharedMessageQueue cometSharedMessageQueue;
    private TelnetStatus status = TelnetStatus.DEAD;

    public TelnetRunner(CometSharedMessageQueue cometSharedMessageQueue) {
        super();
        this.cometSharedMessageQueue = cometSharedMessageQueue;
    }

    @PostConstruct
    public void init() {
        initTelnetOptions();
        start();
    }

    private InputStream getInputStream() {
        return telnet.getInputStream();
    }

    public void setConnectionSettings(ConnectionSettings connectionSettings) {
        this.connectionSettings = connectionSettings;
    }

    public ServerInformation getServerInformation() {
        return serverInformation;
    }

    public TelnetStatus getStatus() {
        return status;
    }

    private void initTelnetOptions() {
        try {
            telnet.addOptionHandler(new TerminalTypeOptionHandler("VT100", false, false, false, false));
            telnet.addOptionHandler(new EchoOptionHandler(false, false, false, false));
            telnet.addOptionHandler(new SuppressGAOptionHandler(true, true, true, true));
            //telnet.setConnectTimeout(5000);
        } catch (InvalidTelnetOptionException e) {
            log.error("Failed to initialize Telnet ", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("Failed to setConnectionSettings Telnet ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            statusChange(TelnetStatus.DISCONNECTED);
            while (!isInterrupted()) {
                if (status.equals(TelnetStatus.CONNECTED)) {
                    try {
                        monitor();
                    } catch (IOException e) {
                        //Disconnect throws  java.net.SocketException: Connection reset
                        if (!status.equals(TelnetStatus.DISCONNECTING)) {
                            log.error("IO error from monitor, this is fatal, disconnecting ", e);
                            try {
                                disconnect();
                            } catch (IOException ie) {
                                log.warn("monitoring exit calling ");
                            }
                        }
                    } catch (Exception e ) {
                        log.warn(" caught an Error inside running loop, we keep on trucking ", e );
                        cometSharedMessageQueue.addMessage(new CometMessage(MessageTarget.ERROR, "Error from monitor :" + e.getMessage()));
                    }
                } else if (status.equals(TelnetStatus.DISCONNECTING) && !isConnected()) {
                    statusChange(TelnetStatus.DISCONNECTED);
                } else if (status.equals(TelnetStatus.MONITORING)) {
                    statusChange(isConnected() ? TelnetStatus.CONNECTED : TelnetStatus.DISCONNECTED);
                    //TODO auto re-connect here
                }
                safeSleep(waitTime * 2);
            }
        } finally {
            statusChange(TelnetStatus.DEAD);
        }
    }

    // DEAD --> DISCONNECTED -> CONNECTING --> LOGGING_IN --> CONNECTED --> MONITORING
    //                        ^                                           |
    //                        |                                           v
    //                           <-                               DISCONNECTING

    private void statusChange(TelnetStatus newStatus) {
        if (!newStatus.equals(status)) {
            cometSharedMessageQueue.addMessage(new CometMessage(MessageTarget.TELNET_STATUS, newStatus));
            status = newStatus;
            log.info("** Telnet status change to: " + newStatus.name());
        }

    }

    private void safeSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.warn("Safe Sleep interrupted", e);
        }
    }

    private void monitor() throws IOException {
        statusChange(TelnetStatus.MONITORING);
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getInputStream()));
        while (isConnected()) {
            runCommand();

            final String line = bufferedReader.readLine();
            if (line != null && !line.isEmpty()) {
                commandHandleInput(line);
                for (TelnetOutputHandler handler : handlers) {
                    try {
                        handler.handleInput(line);
                    } catch (Exception e) {
                        String msg = "Handler :" + handler.getClass().getName() + " threw exception " + e.getMessage();
                        log.error(msg, e);
                        cometSharedMessageQueue.addMessage(new CometMessage(MessageTarget.ERROR, msg));
                    }
                }
            } else {
                safeSleep(waitTime);
            }
        }
    }

    private ServerInformation readServerInfo() throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getInputStream()));
        ServerInformation res = new ServerInformation();
        ServerGreetingHandler handler = new ServerGreetingHandler(res);
        long end = System.currentTimeMillis() + (60 * 1000);
        while (isConnected() && System.currentTimeMillis() < end) {
            String line = bufferedReader.readLine();
            if (LOGON_SUCCESSFUL.equals(line)) {
                statusChange(TelnetStatus.CONNECTED);
            } else if (WRONG_PASSWORD.equals(line)) {
                throw new RuntimeException(WRONG_PASSWORD);
            }
            log.debug("ServerInformation reading line: " + line);
            handler.handleInput(line);
            if (res.help) {
                break;
            }

        }
        log.info("read server info");

        return res;
    }

    private void waitForConnection() {
        long counter = 0;
        while (!telnet.isConnected()) {
            safeSleep(waitTime);
            log.info("    ...waiting for connection");
            if (++counter > connectionTimeoutSeconds) {
                log.error("ERROR: Connection timed out. Another connection might have taken the telnet.");
                statusChange(TelnetStatus.DISCONNECTED);
                cometSharedMessageQueue.addMessage(new CometMessage(MessageTarget.ERROR, "ERROR: Connection timed out. Another connection might have taken the telnet."));
                throw new RuntimeException("ERROR: Connection timed out. Another connection might have taken the telnet.");
            }
        }
    }

    public boolean isConnected() {
        return isAlive() && telnet.isConnected();
    }

    public void connect() throws IOException {
        if (!telnet.isConnected()) {
            try {
                statusChange(TelnetStatus.CONNECTING);
                log.info("Telnet connecting to: " + connectionSettings.getAddress());
                telnet.connect(connectionSettings.getAddress(), connectionSettings.getPort());
                waitForConnection();
                input = new BufferedInputStream(telnet.getInputStream());
                output = new PrintStream(telnet.getOutputStream());

                statusChange(TelnetStatus.LOGGING_IN);
                readUntil(PLEASE_ENTER_PASSWORD, WRONG_PASSWORD);
                workaroundForAllocsBug();
                write(connectionSettings.getPassword());
                //log.debug("entering telnet password: '" + connectionSettings.getPassword() + "'");
                serverInformation = readServerInfo();
                log.info("Connection established");
            } catch (Exception e) {
                if (status.equals(TelnetStatus.LOGGING_IN)) {
                    log.error("Failed to connect telnet, sending error message ", e);
                    cometSharedMessageQueue.addMessage(new CometMessage(MessageTarget.ERROR, "Telnet login failed. " + e.getMessage()));
                }
                disconnect();
            }
        }
    }
    //Alloc broke telnet login
    private void workaroundForAllocsBug() throws IOException {
        write("");
        readUntil(WRONG_PASSWORD, null);
    }

    public void disconnect() throws IOException {
        if (telnet.isConnected()) {
            statusChange(TelnetStatus.DISCONNECTING);
            safeSleep(500);
            telnet.disconnect();
        }
    }

    public void write(String output) throws IOException {
       // log.debug("sending : '" + output + "' to telnet.");
       /* String cmd = output + "\n";
        byte[] bytes = output.getBytes(Charsets.UTF_8);
        this.output.write(bytes);*/
        this.output.println(output);
        this.output.flush();
    }

    private void readUntil(String stopPhrase, String errorPhrase) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
        log.info("...waiting for stopPhrase: " + stopPhrase);
        while (isConnected()) {
            String line = bufferedReader.readLine();
            if (line.equals(stopPhrase)) {
                log.info("Received stopPhrase: " + stopPhrase);
                return;
            } else if (errorPhrase != null && errorPhrase.equals(line)) {
                throw new RuntimeException("ErrorPhrase (" + errorPhrase + ") detected instead of " + stopPhrase);
            }
            log.debug("Wrong stopPhrase, ignoring: " + line);
            safeSleep(waitTime);
        }
    }

    @Override
    public void receivedNegotiation(int i, int i2) {
        log.info("Received negotiation " + i + "," + i2);
    }

    public void addHandler(TelnetOutputHandler handler) {
        for (TelnetOutputHandler telnetOutputHandler : handlers) {
            if (telnetOutputHandler.getClass().equals(handler.getClass())) {
                log.debug("telnet output handlers already contain " + handler.getClass().getName());
                return;
            }
        }
        log.info("Registering TelnetOutputHandler :" + handler.getClass().getName());
        handlers.add(handler);
    }

    public void removeHandler(Class handler) {
        TelnetOutputHandler remove = null;
        for (TelnetOutputHandler telnetOutputHandler : handlers) {
            if (telnetOutputHandler.getClass().equals(handler)) {
                remove = telnetOutputHandler;
                break;
            }
        }

        if (remove != null) {
            log.info("Removing TelnetOutputHandler :" + remove.getClass().getName());
            handlers.remove(remove);
        }
    }

    public void addCommand(TelnetCommand command) {
        commands.add(command);
    }
    private void runCommand() throws IOException {
        if (runningCommand == null || runningCommand.isFinished()) {
            runningCommand = commands.poll();
            if (runningCommand != null) {
                runningCommand.runCommand(output);

                if (runningCommand instanceof  RepeatingCommand) {
                    commands.add(runningCommand);
                }
            }
        }
    }

    private void commandHandleInput(String line) {
        if (runningCommand != null) {
            runningCommand.handleInput(line);
        }
    }

    public enum TelnetStatus {
        DEAD,
        DISCONNECTED,
        CONNECTING,
        LOGGING_IN,
        CONNECTED,
        MONITORING,
        DISCONNECTING
    }

    public class ServerInformation {
        public String version;
        public String compatibility;
        public String ip;
        public int port;
        public int maxPlayers;
        public String mode;
        public String world;
        public String game;
        public int difficulty;
        public boolean allocsExtension;
        public boolean help;

        private ServerInformation() {
        }
    }
}
