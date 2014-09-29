package org.mikezerosix;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mikezerosix.entities.ConnectionSettings;
import org.mikezerosix.entities.ConnectionType;
import org.mikezerosix.telnet.TelnetRunner;

import java.io.*;
import java.util.Properties;

public class TelnetRunnerTest {
    private static ConnectionSettings connectionSettings = new ConnectionSettings();

    @BeforeClass
    public static void setup() {
        Properties prop = new Properties();
        File secrets = new File("secrets.properties");
        try {
            prop.load(new InputStreamReader(new FileInputStream(secrets)));
            connectionSettings.setType(ConnectionType.Telnet);
            connectionSettings.setAddress(prop.getProperty("telnetHost"));
            connectionSettings.setPort(Integer.parseInt(prop.getProperty("telnetPort")));
            connectionSettings.setPassword(prop.getProperty("telnetPassword"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testRun() throws Exception {

        TelnetRunner telnetRunner = new TelnetRunner(null);
        telnetRunner.setConnectionSettings(connectionSettings);

       // telnetRunner.addHandler(new AllHandler(System.out.));
        telnetRunner.start();
        telnetRunner.connect();
        while (!telnetRunner.isConnected()) {
            Thread.sleep(1000);
        }
        Thread.sleep(5000);
        telnetRunner.interrupt();

    }


}