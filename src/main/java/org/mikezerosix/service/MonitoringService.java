package org.mikezerosix.service;

import org.mikezerosix.comet.CometSharedMessageQueue;
import org.mikezerosix.entities.ConnectionSettings;
import org.mikezerosix.ftp.FTPService;
import org.mikezerosix.telnet.TelnetRunner;
import org.mikezerosix.telnet.handlers.StatHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MonitoringService {
    private static final Logger log = LoggerFactory.getLogger(MonitoringService.class);
    private TelnetRunner telnetRunner;
    private FTPService ftpService;
    private StatHandler statHandler;

    public MonitoringService(TelnetRunner telnetRunner, FTPService ftpService,  StatHandler statHandler) {
        this.telnetRunner = telnetRunner;
        this.ftpService = ftpService;
        this.statHandler = statHandler;
    }

    public void setConnectionSettings(ConnectionSettings connectionSettings) {
        switch (connectionSettings.getType()) {
            case GAME_TELNET:
                telnetRunner.setConnectionSettings(connectionSettings);
                if (connectionSettings.isAuto() && !telnetRunner.isConnected()) {
                    try {
                        telnetRunner.connect();
                    } catch (Exception e) {
                        //TODO: push error to client
                        log.error("Failed to call connect() on telnetRunner ", e);
                    }
                }
                break;

            case GAME_FTP:
                ftpService.setConnectionSettings(connectionSettings);
                if (connectionSettings.isAuto()) {
                    try {
                        ftpService.connect();
                    } catch (IOException e) {
                        //TODO: push error to client
                        log.error("FTP connection failed", e);
                    }
                }
                break;

            //TODO: other connection
        }
    }

    public void addHandler(Class handlerClass) {
        if (handlerClass.getName().equals(StatHandler.class.getName())) {
            telnetRunner.addHandler(statHandler);
        }

    }

    public void removeHandler(Class chatHandlerClass) {
        telnetRunner.removeHandler(chatHandlerClass);
    }
}
