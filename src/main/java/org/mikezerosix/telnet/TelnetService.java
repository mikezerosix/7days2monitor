package org.mikezerosix.telnet;

import org.apache.commons.net.telnet.*;
import org.mikezerosix.handlers.TelnetOutputHandler;

import java.io.*;
import java.util.List;

/**
 * http://www.java2s.com/Code/Java/Network-Protocol/ExampleofuseofTelnetClient.htm
 */
public class TelnetService implements Runnable, TelnetNotificationHandler {


    public static final String PLEASE_ENTER_PASSWORD = "Please enter password:";
    public static final String CONNECTED_WITH_7_DTD_SERVER = "*** Connected with 7DTD server.";
    private final String address;
    private final int port;
    private final String password;
    private TelnetClient telnet = null;
    private BufferedInputStream input = null;
    private PrintStream output = null;
    private long waitTime = 500;
    private long connectionTimeoutSeconds = 10;

    private List<TelnetOutputHandler> handlers;

    public TelnetService(String address, int port, String password) {
        this.address = address;
        this.port = port;
        this.password = password;
    }


    public InputStream getInputStream() {
        return telnet.getInputStream();
    }

    public OutputStream getOutputStream() {
        return telnet.getOutputStream();
    }

    @Override
    public void run() {
        telnet = new TelnetClient();

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
            System.out.println("Connection established");

        } catch (InvalidTelnetOptionException e) {
            System.err.println("Error registering option handlers: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean isConnected() {
        return telnet != null && telnet.isConnected();
    }

    private boolean connect() throws InterruptedException, IOException, InvalidTelnetOptionException {
        System.out.println("Telnet connecting to: " + address);
        telnet.addOptionHandler(new TerminalTypeOptionHandler("VT100", false, false, true, false));
        telnet.addOptionHandler(new EchoOptionHandler(true, false, true, false));
        telnet.addOptionHandler(new SuppressGAOptionHandler(true, true, true, true));

        telnet.setConnectTimeout(5000);

        telnet.connect(address, port);
        Thread.sleep(waitTime);
        input = new BufferedInputStream(telnet.getInputStream());
        output = new PrintStream(telnet.getOutputStream());

        readUntil(PLEASE_ENTER_PASSWORD);
        write(password);
        Thread.sleep(waitTime);
        return true;
    }

    public void write(String password) {
        output.println(password);
        output.flush();

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


}
