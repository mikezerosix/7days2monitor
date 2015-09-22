package org.mikezerosix.service;

import org.jolokia.config.ConfigKey;
import org.jolokia.jvmagent.JolokiaServer;
import org.jolokia.jvmagent.JolokiaServerConfig;
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
import java.util.HashMap;

public class MonitoringService {
    private static final Logger log = LoggerFactory.getLogger(MonitoringService.class);
    private TelnetRunner telnetRunner;
    private FTPService ftpService;
    private StatHandler statHandler;
    private ChatHandler chatHandler;
    private PlayerLoginHandler playerLoginHandler;
    private ListPlayersRepeatingCommand listPlayersRepeatingCommand;

    public MonitoringService(TelnetRunner telnetRunner, FTPService ftpService, StatHandler statHandler, ChatHandler chatHandler, PlayerLoginHandler playerLoginHandler, ListPlayersRepeatingCommand listPlayersRepeatingCommand) {
        this.telnetRunner = telnetRunner;
        this.ftpService = ftpService;
        this.statHandler = statHandler;
        this.chatHandler = chatHandler;
        this.playerLoginHandler = playerLoginHandler;
        this.listPlayersRepeatingCommand = listPlayersRepeatingCommand;
    }

    public void setConnectionSettings(ConnectionSettings connectionSettings) {
        switch (connectionSettings.getType()) {
            case Jolokia:
                HashMap<String, String> pConfig = new HashMap<>();
                pConfig.put("port", "" + connectionSettings.getPort());
                pConfig.put("host", "" + connectionSettings.getAddress());
                if (connectionSettings.getUsername() != null && !connectionSettings.getUsername().isEmpty()) {
                    pConfig.put(ConfigKey.USER.name(), connectionSettings.getUsername());
                }
                if (connectionSettings.getPassword() != null && !connectionSettings.getPassword().isEmpty()) {
                    pConfig.put(ConfigKey.PASSWORD.name(), connectionSettings.getPassword());
                }
                if (connectionSettings.isAuto()) {
                    try {
                        JolokiaServerConfig jolokiaServerConfig = new JolokiaServerConfig(pConfig);
                        (new JolokiaServer(jolokiaServerConfig, true)).start();
                        System.out.println("Jolokia server running at " + jolokiaServerConfig.getAddress().getHostName() + ":" + jolokiaServerConfig.getPort() + jolokiaServerConfig.getContextPath());
                    } catch (Exception e) {
                       log.warn("Failed  to start Jolokia server", e);
                    }
                }
                break;
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
