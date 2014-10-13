package org.mikezerosix.ftp;

import org.junit.Before;
import org.junit.Test;
import org.mikezerosix.comet.CometSharedMessageQueue;
import org.mikezerosix.entities.ConnectionSettings;
import org.mikezerosix.entities.ConnectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class FTPServiceTest {
    private static final Logger log = LoggerFactory.getLogger(FTPServiceTest.class);

    private static ConnectionSettings connectionSettings = new ConnectionSettings();
    private FTPService ftpService = new FTPService(new CometSharedMessageQueue());


    @Before
    public void setUp() throws Exception {
        Properties prop = new Properties();
        File secrets = new File("secrets.properties");
        try {
            prop.load(new InputStreamReader(new FileInputStream(secrets)));
            connectionSettings.setType(ConnectionType.FTP);
            connectionSettings.setAddress(prop.getProperty("ftpHost"));
            connectionSettings.setPort(Integer.parseInt(prop.getProperty("ftpPort")));
            connectionSettings.setUsername(prop.getProperty("ftpUserName"));
            connectionSettings.setPassword(prop.getProperty("ftpPassword"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ftpService.setConnectionSettings(connectionSettings);

    }

    @Test
    public void testConnect() throws Exception {
        ftpService.connect();

    }

    @Test
    public void testLS() throws Exception {
        ftpService.connect();


        assertThat(ftpService.listFiles("/").length, not(0));
    }
}