package org.mikezerosix.telnet;

import org.apache.commons.net.telnet.*;
import org.mikezerosix.entities.ConnectionSettings;
import org.mikezerosix.rest.LoginResource;
import org.mikezerosix.telnet.commands.TelnetCommand;
import org.mikezerosix.telnet.handlers.ServerGreetingHandler;
import org.mikezerosix.telnet.handlers.TelnetOutputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * http://www.java2s.com/Code/Java/Network-Protocol/ExampleofuseofTelnetClient.htm
 */
public class TelnetService extends Thread implements TelnetNotificationHandler {
    public static final String PLEASE_ENTER_PASSWORD = "Please enter password:";
    public static final String WRONG_PASSWORD = "Password incorrect, please enter password:";
    public static final long connectionTimeoutSeconds = 10;
    public static final long waitTime = 500;
    private static final Logger log = LoggerFactory.getLogger(LoginResource.class);
    private static TelnetService instance = null;
    private final List<TelnetOutputHandler> handlers = new ArrayList<>();
    private final ArrayBlockingQueue<TelnetCommand> commands = new ArrayBlockingQueue<>(12);
    private final TelnetClient telnet = new TelnetClient();
    private BufferedInputStream input = null;
    private PrintStream output = null;
    private TelnetCommand runningCommand = null;
    private ServerInformation serverInformation = new ServerInformation();
    private ConnectionSettings connectionSettings;

    private TelnetService() {
        super();
    }

    public static synchronized TelnetService getInstance() {
        if (instance == null) {
            instance = new TelnetService();
        }
        return instance;
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
        log.info("** Starting TelnetService");

        while (!isInterrupted()) {
            try {
                monitor();

            } catch (InterruptedException e) {
                log.warn("Interrupted ", e);
            } catch (InterruptedIOException e) {
                //I assume this is from intention interrupt
                log.warn("Interrupted IO ", e);
            } catch (IOException e) {
                log.error("IO error", e);
                throw new RuntimeException(e);
            } finally {
                serverInformation.connected = false;
            }
        }
    }

    private void monitor() throws IOException, InterruptedException {
        if (!isMonitoring()) {
            return;
        }
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getInputStream()));
        log.info("Telnet Monitor is running");
        while (isConnected()) {
            runCommand();

            final String line = bufferedReader.readLine();
            commandHandleInput(line);
            for (TelnetOutputHandler handler : handlers) {
                handler.handleInput(line);
            }
            Thread.sleep(waitTime);

        }
    }

    private ServerInformation readServerInfo() throws IOException, InterruptedException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getInputStream()));
        ServerInformation res = new ServerInformation();
        ServerGreetingHandler handler = new ServerGreetingHandler(res);
        int lineCounter = 0;
        while (isConnected() && handler.getMissingLines() > 0 && lineCounter < 20) {
            lineCounter++;
            String line = bufferedReader.readLine();
            handler.handleInput(line);
            Thread.sleep(waitTime);
        }
        return res;
    }

    private void waitForConnection() throws InterruptedException {
        long counter = 0;
        while (!telnet.isConnected()) {
            Thread.sleep(1000);
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

    public boolean connect() throws InterruptedException, IOException, InvalidTelnetOptionException {
        log.info("Telnet connecting to: " + connectionSettings.getAddress());
        telnet.addOptionHandler(new TerminalTypeOptionHandler("VT100", false, false, true, false));
        telnet.addOptionHandler(new EchoOptionHandler(true, false, true, false));
        telnet.addOptionHandler(new SuppressGAOptionHandler(true, true, true, true));

        telnet.setConnectTimeout(5000);

        telnet.connect(connectionSettings.getAddress(), connectionSettings.getPort());
        Thread.sleep(waitTime);
        input = new BufferedInputStream(telnet.getInputStream());
        output = new PrintStream(telnet.getOutputStream());

        readUntil(PLEASE_ENTER_PASSWORD, WRONG_PASSWORD);
        write(connectionSettings.getPassword());
        waitForConnection();
        serverInformation = readServerInfo();
        log.info("Connection established");

        return true;
    }

    public void disconnect() throws IOException {
        if (!telnet.isConnected()) {
            telnet.disconnect();
        }
        log.info("Telnet disconnected");
    }

    public void write(String output) {
        this.output.println(output);
        this.output.flush();

    }

    private void readUntil(String stopPhrase, String errorPhrase) throws IOException, InterruptedException {
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
            Thread.sleep(waitTime);
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

        private ServerInformation() {
        }
    }
}
