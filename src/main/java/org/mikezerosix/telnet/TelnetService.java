package org.mikezerosix.telnet;

import org.apache.commons.net.telnet.*;
import org.mikezerosix.entities.ConnectionSettings;
import org.mikezerosix.handlers.TelnetOutputHandler;
import org.mikezerosix.rest.LoginResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * http://www.java2s.com/Code/Java/Network-Protocol/ExampleofuseofTelnetClient.htm
 */
public class TelnetService extends Thread implements TelnetNotificationHandler {
    public static final String PLEASE_ENTER_PASSWORD = "Please enter password:";
    public static final String CONNECTED_WITH_7_DTD_SERVER = "*** Connected with 7DTD server.";
    public static final String WRONG_PASSWORD = "Password incorrect, please enter password:";
    private static final Logger log = LoggerFactory.getLogger(LoginResource.class);
    private static TelnetClient telnet = null;
    private static BufferedInputStream input = null;
    private static PrintStream output = null;
    private static TelnetService instance = null;
    private ConnectionSettings connectionSettings;
    private long waitTime = 500;
    private static long connectionTimeoutSeconds = 10;
    private static boolean loggedIn = false;
    private static List<TelnetOutputHandler> handlers = new ArrayList<>();

    private TelnetService() {
        super();

    }

    public static synchronized TelnetService getInstance() {
        if (instance == null) {
            instance = new TelnetService();
        }
        return instance;
    }

    public InputStream getInputStream() {
        return telnet.getInputStream();
    }

    public OutputStream getOutputStream() {
        return telnet.getOutputStream();
    }

    public void setConnectionSettings(ConnectionSettings connectionSettings) {
        this.connectionSettings = connectionSettings;
    }

    @Override
    public void run() {
        log.info("** Starting TelnetService");
        telnet = new TelnetClient();
        loggedIn = false;
        try {
            connect();

            while (!telnet.isConnected()) {
                Thread.sleep(1000);
                log.info("    ...waiting for connection");
                if (--connectionTimeoutSeconds < 0) {
                    log.error("ERROR: Connection timed out. Another connection might have taken the telnet.");
                    throw new RuntimeException("ERROR: Connection timed out. Another connection might have taken the telnet.");
                }
            }
            readUntil(CONNECTED_WITH_7_DTD_SERVER, null);
            loggedIn = true;
            log.info("Connection established");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getInputStream()));
            while (telnet.isConnected()) {
                final String line = bufferedReader.readLine();
                for (TelnetOutputHandler handler : handlers) {
                    handler.handleInput(line);
                }

                Thread.sleep(waitTime);

            }
            loggedIn = false;
            log.info("Telnet shutdown");

        } catch (InvalidTelnetOptionException e) {
            log.error("Error registering option handlers: " + e.getMessage());
            loggedIn = false;
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            log.warn("Interrupted ", e);
            loggedIn = false;
            return;
        } catch (InterruptedIOException e) {
            //I assume this is from intention interrupt
            log.warn("Interrupted IO ", e);
            loggedIn = false;
            return;
        } catch (IOException e) {
            log.error("IO error", e);
            loggedIn = false;
            throw new RuntimeException(e);
        }

    }

    public boolean isConnected() {
        return telnet != null && telnet.isConnected();
    }

    public boolean isLoggedIn() {
        return isConnected() && loggedIn;
    }

    private boolean connect() throws InterruptedException, IOException, InvalidTelnetOptionException {
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
        Thread.sleep(waitTime);
        return true;
    }

    public void write(String output) {
        this.output.println(output);
        this.output.flush();

    }

    private void readUntil(String stopPhrase, String errorPhrase) throws IOException, InterruptedException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
        log.info("...waiting for stopPhrase: " + stopPhrase);
        while (true) {
            String line = bufferedReader.readLine();
            if (line.equals(stopPhrase)) {
                log.info("Received stopPhrase: " + stopPhrase);
                return;
            } else if (errorPhrase != null && errorPhrase.equals(line)){
                throw new RuntimeException("ErrorPhrase (" + errorPhrase +") detected");
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
}
