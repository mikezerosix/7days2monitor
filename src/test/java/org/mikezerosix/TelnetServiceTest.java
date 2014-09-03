package org.mikezerosix;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mikezerosix.entities.Linkage;
import org.mikezerosix.entities.ConnectionType;
import org.mikezerosix.handlers.AllHandler;
import org.mikezerosix.telnet.TelnetService;

import java.io.*;
import java.util.Properties;

public class TelnetServiceTest {
    private static Linkage linkage = new Linkage();

    @BeforeClass
    public static void setup() {
        Properties prop = new Properties();
        File secrets = new File("secrets.properties");
        try {
            prop.load(new InputStreamReader(new FileInputStream(secrets)));
            linkage.setType(ConnectionType.GAME_TELNET);
            linkage.setAddress(prop.getProperty("telnetHost"));
            linkage.setPort(Integer.parseInt(prop.getProperty("telnetPort")));
            linkage.setPassword(prop.getProperty("telnetPassword"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testRun() throws Exception {

        TelnetService telnetService = new TelnetService(linkage);
       // telnetService.addHandler(new AllHandler(System.out.));
        telnetService.start();
        while (!telnetService.isConnected()) {
            Thread.sleep(1000);
        }
        Thread.sleep(5000);
        telnetService.interrupt();

    }


}