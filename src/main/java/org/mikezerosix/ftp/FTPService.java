package org.mikezerosix.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.mikezerosix.comet.CometSharedMessageQueue;
import org.mikezerosix.entities.ConnectionSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.AccessDeniedException;

/**
 * http://commons.apache.org/proper/commons-net/apidocs/org/apache/commons/net/ftp/FTPClient.html
 */
public class FTPService {
    public static final Logger log = LoggerFactory.getLogger(FTPService.class);
    private FTPClient ftp;
    private ConnectionSettings connectionSettings;
    private CometSharedMessageQueue cometSharedMessageQueue;

    public FTPService(CometSharedMessageQueue cometSharedMessageQueue) {
        this.cometSharedMessageQueue = cometSharedMessageQueue;
        this.ftp = new FTPClient();
        ftp.setControlKeepAliveTimeout(300);

    }

    public void setConnectionSettings(ConnectionSettings connectionSettings) {
        this.connectionSettings = connectionSettings;
        FTPClientConfig config = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
        //setConnectionSettings.setXXX(YYY); // change required options
        ftp.configure(config);
    }

    public FTPFile[] listFiles(String path) throws IOException {
        connect();
        FTPFile[] files = ftp.listFiles(path);
        confirmPositiveCompletion();
        return files;
    }

    public FTPFile[] listFiles() throws IOException {
        connect();
        FTPFile[] files;
        try {

            files = ftp.listDirectories();
        } catch (IOException e) {
            log.error("IO exception from list files ", e );
            throw e;
        }
        confirmPositiveCompletion();
        return files;
    }

    public void pullFile(String from, OutputStream to) throws IOException {
        connect();
        ftp.retrieveFile(from, to);
        confirmPositiveCompletion();
    }

    public void pushFile(InputStream from, String to) throws IOException {
        connect();
        ftp.storeFile(to, from);
        confirmPositiveCompletion();
    }

    public void copyFile(String from, String to) throws IOException {
        ByteArrayOutputStream tempData = new ByteArrayOutputStream();
        pullFile(from , tempData);
        InputStream is = new ByteArrayInputStream(tempData.toByteArray());
        pushFile(is, to);
    }

    public void renameFile(String from, String to) throws IOException {
        connect();
        ftp.rename(from, to);
        confirmPositiveCompletion();
    }

    public void deleteFile(String delete) throws IOException {
        connect();
        ftp.deleteFile(delete);
        confirmPositiveCompletion();
    }

    public boolean isConnected() {
        return ftp.isConnected();
    }

    public void connect() throws IOException {
        if (!ftp.isConnected()) {
            log.info("Connecting FTP to " + connectionSettings.getAddress());
            ftp.connect(connectionSettings.getAddress(), connectionSettings.getPort());
            ftp.enterLocalPassiveMode();
            final boolean login = ftp.login(connectionSettings.getUsername(), connectionSettings.getPassword());
            if (!login ) {
                throw new AccessDeniedException("FTP login failed: " + ftp.getReplyString());
            }
            if ("Windows_NT".equals(ftp.getSystemType())) {
                ftp.configure( new FTPClientConfig(FTPClientConfig.SYST_NT));
            }
            log.info("FTP Connected to " + connectionSettings.getAddress() + ":" + connectionSettings.getPort() + ".");
            log.info(ftp.getReplyString());
            confirmPositiveCompletion();
        }
        //TODO: can I use this if I have not issued any commands  ?
        // confirmPositiveCompletion();
    }
    public void disconnect() throws IOException {
        ftp.disconnect();
    }
    private void confirmPositiveCompletion() {
        final int replyCode = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(replyCode)) {
            throw new RuntimeException("Bad reply code: " + replyCode + ", " + ftp.getReplyString());
        }


    }
}
