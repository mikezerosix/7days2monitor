package org.mikezerosix.telnet;

import org.apache.commons.net.telnet.*;
import org.mikezerosix.entities.Linkage;
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
    private static final Logger log = LoggerFactory.getLogger(LoginResource.class);

    public static final String PLEASE_ENTER_PASSWORD = "Please enter password:";
    public static final String CONNECTED_WITH_7_DTD_SERVER = "*** Connected with 7DTD server.";
    private Linkage linkage;

    private TelnetClient telnet = null;
    private BufferedInputStream input = null;
    private PrintStream output = null;
    private long waitTime = 500;
    private long connectionTimeoutSeconds = 10;
    private boolean loggedIn = false;
    private List<TelnetOutputHandler> handlers = new ArrayList<>();

    public TelnetService(Linkage linkage) {
        super();
        this.linkage = linkage;
    }

    public InputStream getInputStream() {
        return telnet.getInputStream();
    }

    public OutputStream getOutputStream() {
        return telnet.getOutputStream();
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
                System.out.println("    ...waiting for connection");
                if (--connectionTimeoutSeconds < 0) {
                    System.out.println("ERROR: Connection timed out. Another connection might have taken the telnet.");
                    throw new RuntimeException("ERROR: Connection timed out. Another connection might have taken the telnet.");
                }
            }
            readUntil(CONNECTED_WITH_7_DTD_SERVER);
            loggedIn = true;
            System.out.println("Connection established");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getInputStream()));
            while (telnet.isConnected()) {
                final String line = bufferedReader.readLine();
                for (TelnetOutputHandler handler : handlers) {
                    handler.handleInput(line);
                }

                Thread.sleep(waitTime);

            }
            loggedIn = false;

        } catch (InvalidTelnetOptionException e) {
            System.err.println("Error registering option handlers: " + e.getMessage());
            loggedIn = false;
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            loggedIn = false;
            return;
        } catch (InterruptedIOException e) {
            //I assume this is from intention interrupt
            loggedIn = false;
            return;
        } catch (IOException e) {
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
        System.out.println("Telnet connecting to: " + linkage.getAddress());
        telnet.addOptionHandler(new TerminalTypeOptionHandler("VT100", false, false, true, false));
        telnet.addOptionHandler(new EchoOptionHandler(true, false, true, false));
        telnet.addOptionHandler(new SuppressGAOptionHandler(true, true, true, true));

        telnet.setConnectTimeout(5000);

        telnet.connect(linkage.getAddress(), linkage.getPort());
        Thread.sleep(waitTime);
        input = new BufferedInputStream(telnet.getInputStream());
        output = new PrintStream(telnet.getOutputStream());

        readUntil(PLEASE_ENTER_PASSWORD);
        write(linkage.getPassword());
        Thread.sleep(waitTime);
        return true;
    }

    public void write(String output) {
        this.output.println(output);
        this.output.flush();

    }

    private void readUntil(String stopPhrase) throws IOException, InterruptedException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
        while (true) {
            String line = bufferedReader.readLine();
            if (line.equals(stopPhrase)) {
                System.out.println("Logged in");
                return;
            }
            Thread.sleep(waitTime);
        }
    }

    @Override
    public void receivedNegotiation(int i, int i2) {

    }

    public void addHandler(TelnetOutputHandler handler) {
        handlers.add(handler);
    }
}
