package org.mikezerosix;

import junit.framework.TestCase;
import org.mikezerosix.telnet.TelnetConnection;

public class TelnetConnectionTest extends TestCase {

    public void testConnect() throws Exception {
        String host = "";
        int port = 0;
        String password = "";
        TelnetConnection telnetConnection = new TelnetConnection(host, port, password);
        telnetConnection.connect();
    }

}