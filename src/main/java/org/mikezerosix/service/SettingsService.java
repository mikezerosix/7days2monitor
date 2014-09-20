package org.mikezerosix.service;


import org.mikezerosix.entities.ConnectionRepository;
import org.mikezerosix.entities.ConnectionSettings;
import org.mikezerosix.entities.Settings;
import org.mikezerosix.entities.SettingsRepository;
import org.mikezerosix.telnet.handlers.ChatHandler;
import org.mikezerosix.telnet.handlers.PlayerLoginHandler;
import org.mikezerosix.telnet.handlers.StatHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SettingsService {
    public static final String PROTECTED_URL = "/protected/";
    public static final String SETTING_CHAT_HANDLER_ENABLE = "CHAT_HANDLER_ENABLE";
    public static final String SETTING_STAT_HANDLER_ENABLE = "STAT_HANDLER_ENABLE";
    public static final String SETTING_PLAYER_HANDLER_ENABLE = "SETTING_PLAYER_HANDLER_ENABLE";
    private static final Logger log = LoggerFactory.getLogger(SettingsService.class);
    private SettingsRepository settingsRepository;
    private ConnectionRepository connectionRepository;
    private MonitoringService monitoringService;

    public SettingsService(SettingsRepository settingsRepository, ConnectionRepository connectionRepository, MonitoringService monitoringService) {
        this.settingsRepository = settingsRepository;
        this.connectionRepository = connectionRepository;
        this.monitoringService = monitoringService;
    }

    public void init() {
        for (Settings setting : settingsRepository.findAll()) {
            settingsChange(setting);
        }
        for (ConnectionSettings connectionSettings : connectionRepository.findAll()) {
            connectionChange(connectionSettings);
        }
    }

    public void settingsChange(Settings settings) {
        switch (settings.getId()) {
            case SETTING_CHAT_HANDLER_ENABLE:
                if (Boolean.parseBoolean(settings.getValue())) {
                    monitoringService.addHandler(ChatHandler.class);
                } else {
                    monitoringService.removeHandler(ChatHandler.class);
                }
                break;
            case SETTING_STAT_HANDLER_ENABLE:
                if (Boolean.parseBoolean(settings.getValue())) {
                    monitoringService.addHandler(StatHandler.class);
                } else {
                    monitoringService.removeHandler(StatHandler.class);
                }
                break;
            case SETTING_PLAYER_HANDLER_ENABLE:
                if (Boolean.parseBoolean(settings.getValue())) {
                    monitoringService.addHandler(PlayerLoginHandler.class);
                } else {
                    monitoringService.removeHandler(PlayerLoginHandler.class);
                }
                break;
        }
    }

    public void connectionChange(ConnectionSettings connectionSettings) {
        monitoringService.setConnectionSettings(connectionSettings);
    }

    private Settings setSetting(String key, String value) {
        Settings settings = settingsRepository.save(new Settings(value, key));
        settingsChange(settings);
        return settings;
    }

    private String getSetting(String key) {
        Settings settings = settingsRepository.findOne(key);
        return settings != null ? settings.getValue() : null;
    }

}
