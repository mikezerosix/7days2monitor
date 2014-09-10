package org.mikezerosix;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mikezerosix.entities.ConnectionSettings;
import org.mikezerosix.entities.ConnectionType;
import org.mikezerosix.telnet.TelnetService;

import java.io.*;
import java.util.Properties;

public class TelnetServiceTest {
    private static ConnectionSettings connectionSettings = new ConnectionSettings();

    @BeforeClass
    public static void setup() {
        Properties prop = new Properties();
        File secrets = new File("secrets.properties");
        try {
            prop.load(new InputStreamReader(new FileInputStream(secrets)));
            connectionSettings.setType(ConnectionType.GAME_TELNET);
            connectionSettings.setAddress(prop.getProperty("telnetHost"));
            connectionSettings.setPort(Integer.parseInt(prop.getProperty("telnetPort")));
            connectionSettings.setPassword(prop.getProperty("telnetPassword"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testRun() throws Exception {

        TelnetService telnetService = TelnetService.getInstance();
        telnetService.setConnectionSettings(connectionSettings);

       // telnetService.addHandler(new AllHandler(System.out.));
        telnetService.start();
        while (!telnetService.isConnected()) {
            Thread.sleep(1000);
        }
        Thread.sleep(5000);
        telnetService.interrupt();

    }


}