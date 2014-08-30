package org.mikezerosix;

import junit.framework.TestCase;
import org.mikezerosix.handlers.AllHandler;
import org.mikezerosix.handlers.ChatHandler;
import org.mikezerosix.telnet.TelnetConnection;
import org.mikezerosix.telnet.TelnetListener;

public class TelnetListenerTest extends TestCase {

    public void testRun() throws Exception {
        String host = "";
        int port = 0;
        String password = "";
        TelnetConnection telnetConnection = new TelnetConnection(host, port, password);
        telnetConnection.connect();

        TelnetListener telnetListener = new TelnetListener(telnetConnection);
        AllHandler allHandler = new AllHandler(System.out);
        ChatHandler  chatHandler = new ChatHandler();
        telnetListener.addHandler(allHandler);
        telnetListener.addHandler(chatHandler);

        final Thread thread = new Thread(telnetListener);
        thread.start();

        while (true) {
            telnetConnection.write("say Hello (test)");
            Thread.sleep(1000* 5);

        }

    }


}