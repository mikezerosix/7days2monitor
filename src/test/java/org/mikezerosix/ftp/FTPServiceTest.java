package org.mikezerosix.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.Before;
import org.junit.Test;
import org.mikezerosix.entities.ConnectionSettings;
import org.mikezerosix.entities.ConnectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class FTPServiceTest {
    public static final Logger log = LoggerFactory.getLogger(FTPServiceTest.class);

    private static ConnectionSettings connectionSettings = new ConnectionSettings();
    private FTPService ftpService;

    @Before
    public void setUp() throws Exception {
        Properties prop = new Properties();
        File secrets = new File("secrets.properties");
        try {
            prop.load(new InputStreamReader(new FileInputStream(secrets)));
            connectionSettings.setType(ConnectionType.GAME_FTP);
            connectionSettings.setAddress(prop.getProperty("ftpHost"));
            connectionSettings.setPort(Integer.parseInt(prop.getProperty("ftpPort")));
            connectionSettings.setUsername(prop.getProperty("ftpUserName"));
            connectionSettings.setPassword(prop.getProperty("ftpPassword"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ftpService = new FTPService(connectionSettings);

    }

    @Test
    public void testConnect() throws Exception {
        ftpService.connect();
        long start = System.currentTimeMillis();
        try {
            final FTPClient ftp = ftpService.getFtp();
            final String status = ftp.getStatus();

            log.info("status: " + status);
            log.info("connected: " + ftp.isConnected());
            log.info("available: " + ftp.isAvailable());
            log.info("system: " + ftp.getSystemType());
            log.info("help: " + ftp.listHelp());
            log.info("list: " + ftp.list());

            log.info("wd: " + ftp.printWorkingDirectory());

            ftp.initiateListParsing();
            final FTPFile[] ftpFiles = ftp.listDirectories();
            log.info("files: " + ftpFiles.length);
            log.info("reply: " + ftp.getReplyString());
        } catch (Exception e) {
           log.error("listing files took: " + (System.currentTimeMillis() - start) + "ms");
            e.printStackTrace();
        }

    }
}