package org.mikezerosix.telnet;

import org.apache.commons.net.telnet.*;
import org.mikezerosix.comet.CometSharedMessageQueue;
import org.mikezerosix.entities.ConnectionSettings;
import org.mikezerosix.telnet.commands.TelnetCommand;
import org.mikezerosix.telnet.handlers.ServerGreetingHandler;
import org.mikezerosix.telnet.handlers.TelnetOutputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * http://www.java2s.com/Code/Java/Network-Protocol/ExampleofuseofTelnetClient.htm
 */
public class TelnetRunner extends Thread implements TelnetNotificationHandler {
    public static final String PLEASE_ENTER_PASSWORD = "Please enter password:";
    public static final String WRONG_PASSWORD = "Password incorrect, please enter password:";
    public static final long connectionTimeoutSeconds = 10;
    public static final long waitTime = 500;
    private static final Logger log = LoggerFactory.getLogger(TelnetRunner.class);
    private static TelnetRunner instance = null;
    private final List<TelnetOutputHandler> handlers = new ArrayList<>();
    private final ArrayBlockingQueue<TelnetCommand> commands = new ArrayBlockingQueue<>(12);
    private final TelnetClient telnet = new TelnetClient();
    private BufferedInputStream input = null;
    private PrintStream output = null;
    private TelnetCommand runningCommand = null;
    private ServerInformation serverInformation = new ServerInformation();
    private ConnectionSettings connectionSettings;
    private CometSharedMessageQueue cometSharedMessageQueue;
    private boolean shuttingDown = false;
    private boolean starting = false;

    public TelnetRunner(CometSharedMessageQueue cometSharedMessageQueue) {
        super();
        this.cometSharedMessageQueue = cometSharedMessageQueue;
    }

    @PostConstruct
    public void init() {
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

    @Override
    public void run() {
        log.info("** Starting TelnetRunner");
        initTelnetOptions();
        while (!isInterrupted()) {
            if (isMonitoring()) {
                try {
                    monitor();
                } catch (InterruptedIOException e) {
                    log.warn("Interrupted IO from monitor ", e);
                } catch (IOException e) {

                    /*
2014-09-14 17:51:45,527 ERROR [Thread-0] o.m.t.TelnetRunner [TelnetRunner.java:69] IO error from monitor
java.net.SocketException: Connection reset

TODO: auto reconnect
*/
                    log.error("IO error from monitor", e);
                } catch (Exception e) {
                    log.error("Mystery error from monitor but we keep on trucking ", e);
                } finally {
                    log.warn("monitoring ended ");
                    serverInformation.connected = false;
                }
            }
            safeSleep(waitTime * 2);
        }
        log.warn("Telnet service thread interrupted and terminating. ");
    }

    private void safeSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.warn("Safe Sleep interrupted", e);
        }
    }

    private void initTelnetOptions() {
        try {
            telnet.addOptionHandler(new TerminalTypeOptionHandler("VT100", false, false, true, false));
            telnet.addOptionHandler(new EchoOptionHandler(true, false, true, false));
            telnet.addOptionHandler(new SuppressGAOptionHandler(true, true, true, true));
            telnet.setConnectTimeout(5000);
        } catch (InvalidTelnetOptionException e) {
            log.error("Failed to initialize Telnet ", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("Failed to setConnectionSettings Telnet ", e);
            throw new RuntimeException(e);
        }
    }

    private void monitor() throws IOException {
        if (!isMonitoring()) {
            return;
        }
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getInputStream()));
        log.info("Telnet Monitor is running");
        while (isConnected()) {
            runCommand();

            final String line = bufferedReader.readLine();
            if (line != null && !line.isEmpty()) {
                commandHandleInput(line);
                for (TelnetOutputHandler handler : handlers) {
                    try {
                        handler.handleInput(line);
                    } catch (Exception e) {
                        log.error("Handler :" + handler.getClass().getName() + " threw exception "  + e.getMessage() , e);
                    }
                }
            } else {
                safeSleep(waitTime*2);
            }
        }
    }

    private ServerInformation readServerInfo() throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getInputStream()));
        ServerInformation res = new ServerInformation();
        ServerGreetingHandler handler = new ServerGreetingHandler(res);
        int lineCounter = 0;
        while (isConnected() && lineCounter < 24) {
            lineCounter++;
            String line = bufferedReader.readLine();
            handler.handleInput(line);
            if (res.help) {
                break;
            }
            safeSleep(waitTime);
        }
        return res;
    }

    private void waitForConnection() {
        long counter = 0;
        while (!telnet.isConnected()) {
            safeSleep(waitTime);
            log.info("    ...waiting for connection");
            if (++counter > connectionTimeoutSeconds) {
                log.error("ERROR: Connection timed out. Another connection might have taken the telnet.");
                throw new RuntimeException("ERROR: Connection timed out. Another connection might have taken the telnet.");
            }
        }
    }

    public boolean isConnected() {
        return isAlive() && telnet.isConnected();
    }

    public boolean isMonitoring() {
        return isConnected() && serverInformation.connected;
    }

    public void connect() throws IOException {
        if (!telnet.isConnected()) {
            log.info("Telnet connecting to: " + connectionSettings.getAddress());
            telnet.connect(connectionSettings.getAddress(), connectionSettings.getPort());
            safeSleep(waitTime);
            input = new BufferedInputStream(telnet.getInputStream());
            output = new PrintStream(telnet.getOutputStream());

            readUntil(PLEASE_ENTER_PASSWORD, WRONG_PASSWORD);
            write(connectionSettings.getPassword());
            waitForConnection();
            serverInformation = readServerInfo();
            log.info("Connection established");
        } else {
            log.info("Telnet already connected to: " + connectionSettings.getAddress());
        }

    }

    public void disconnect() throws IOException {
        if (telnet.isConnected()) {
            shuttingDown = true;
            telnet.disconnect();
        }

        log.info("Telnet disconnected");
    }

    public void write(String output) {
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
                throw new RuntimeException("ErrorPhrase (" + errorPhrase + ") detected");
            }
            log.debug("Wrong stopPhrase, ignoring: " + line);
            safeSleep(waitTime);
        }
    }

    @Override
    public void receivedNegotiation(int i, int i2) {

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

    public boolean hasHandler(Class handler) {
        for (TelnetOutputHandler telnetOutputHandler : handlers) {
            if (telnetOutputHandler.getClass().equals(handler)) {
                return true;
            }
        }
        return false;
    }

    public void sendCommand(TelnetCommand cmd) throws InterruptedException {
        for (TelnetCommand command : commands) {
            if (command.getCommand().equals(cmd.getCommand())) {
                return;
            }
        }
        commands.put(cmd);
    }

    public String getStatus() {
        if (isMonitoring()) {
            return "monitoring";
        }
        return "dead";
    }
    private void runCommand() {
        if (runningCommand == null || runningCommand.isFinished()) {
            runningCommand = commands.poll();
            if (runningCommand != null) {
                write(runningCommand.getCommand());
            }
        }

    }

    private void commandHandleInput(String line) {
        if (runningCommand != null) {
            runningCommand.handleInput(line);
        }
    }

    public class ServerInformation {
        public boolean connected;
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
