package org.mikezerosix.service;

import org.mikezerosix.entities.ConnectionSettings;
import org.mikezerosix.ftp.FTPService;
import org.mikezerosix.telnet.TelnetRunner;
import org.mikezerosix.telnet.commands.ListPlayersRepeatingCommand;
import org.mikezerosix.telnet.handlers.ChatHandler;
import org.mikezerosix.telnet.handlers.PlayerLoginHandler;
import org.mikezerosix.telnet.handlers.StatHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MonitoringService {
    private static final Logger log = LoggerFactory.getLogger(MonitoringService.class);
    private TelnetRunner telnetRunner;
    private FTPService ftpService;
    private StatHandler statHandler;
    private ChatHandler chatHandler;
    private PlayerLoginHandler playerLoginHandler;
    private ListPlayersRepeatingCommand listPlayersRepeatingCommand;

    public MonitoringService(TelnetRunner telnetRunner, FTPService ftpService,  StatHandler statHandler, ChatHandler chatHandler, PlayerLoginHandler playerLoginHandler, ListPlayersRepeatingCommand listPlayersRepeatingCommand) {
        this.telnetRunner = telnetRunner;
        this.ftpService = ftpService;
        this.statHandler = statHandler;
        this.chatHandler = chatHandler;
        this.playerLoginHandler = playerLoginHandler;
        this.listPlayersRepeatingCommand = listPlayersRepeatingCommand;
    }

    public void setConnectionSettings(ConnectionSettings connectionSettings) {
        switch (connectionSettings.getType()) {
            case Telnet:
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

            case FTP:
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
        if (handlerClass.equals(StatHandler.class)) {
            telnetRunner.addHandler(statHandler);
        }
        if (handlerClass.equals(ChatHandler.class)) {
            telnetRunner.addHandler(chatHandler);
        }
    }

    public void removeHandler(Class chatHandlerClass) {
        telnetRunner.removeHandler(chatHandlerClass);
    }

    public void setStatDays(long statDays) {
        statHandler.setStatDays(statDays);
    }

    public void setChatDays(int chatDays) {
        chatHandler.setMaxDays(chatDays);
    }

    public void init() {
        telnetRunner.addHandler(playerLoginHandler);
        telnetRunner.addCommand(listPlayersRepeatingCommand);
    }

    public void setPlayerDays(long playerDays) {
        listPlayersRepeatingCommand.setPlayerDays(playerDays);
    }

    public void setPlayerInterval(long l) {
        listPlayersRepeatingCommand.setDelay(l);
    }
}
