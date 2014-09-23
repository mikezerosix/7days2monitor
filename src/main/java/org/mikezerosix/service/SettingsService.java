package org.mikezerosix.service;


import com.google.common.collect.Lists;
import org.mikezerosix.entities.*;
import org.mikezerosix.telnet.handlers.ChatHandler;
import org.mikezerosix.telnet.handlers.HandlerSettingKeys;
import org.mikezerosix.telnet.handlers.PlayerLoginHandler;
import org.mikezerosix.telnet.handlers.StatHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SettingsService {
    public static final String PROTECTED_URL = "/protected/";
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
        Iterable<Setting> settingses = settingsRepository.findAll();
        boolean noSettings = true;
        for (Setting setting : settingses) {
            settingsChange(setting);
            noSettings = false;
        }
        if (noSettings) {
            initSettings();
        }

        boolean noConnections = true;
        for (ConnectionSettings connectionSettings : connectionRepository.findAll()) {
            connectionChange(connectionSettings);
            noConnections = false;
        }
        if (noConnections) {
            initConnections();
        }
    }

    private void initConnections() {
        for (ConnectionType type : ConnectionType.values()) {
            ConnectionSettings connectionSettings = new ConnectionSettings();
            connectionSettings.setType(type);
            connectionRepository.save(connectionSettings);
        }
    }

    private void initSettings() {
        for (HandlerSettingKeys key : HandlerSettingKeys.values()) {
            settingsRepository.save(new Setting(key.name(), null));
        }
    }

    public void settingsChange(Setting setting) {
        switch (HandlerSettingKeys.valueOf(setting.getId())) {
            case CHAT_HANDLER_ENABLE:
                if (Boolean.parseBoolean(setting.getValue())) {
                    monitoringService.addHandler(ChatHandler.class);
                } else {
                    monitoringService.removeHandler(ChatHandler.class);
                }
                break;
            case STAT_HANDLER_ENABLE:
                if (Boolean.parseBoolean(setting.getValue())) {
                    monitoringService.addHandler(StatHandler.class);
                } else {
                    monitoringService.removeHandler(StatHandler.class);
                }
                break;
            case PLAYER_HANDLER_ENABLE:
                if (Boolean.parseBoolean(setting.getValue())) {
                    monitoringService.addHandler(PlayerLoginHandler.class);
                } else {
                    monitoringService.removeHandler(PlayerLoginHandler.class);
                }
                break;
            case STAT_HANDLER_DAYS:
                if (setting.getValue() != null && Integer.parseInt(setting.getValue()) > 0) {

                } else {

                }
                break;
        }
    }

    public void connectionChange(ConnectionSettings connectionSettings) {
        monitoringService.setConnectionSettings(connectionSettings);
    }

    public Setting setSetting(Setting setting) {
        Setting saved = settingsRepository.save(setting);
        settingsChange(saved);
        return saved;
    }

    public Setting getSetting(String key) {
        return settingsRepository.findOne(key);
    }

    public List<ConnectionSettings> listConnections() {
        return Lists.newArrayList(connectionRepository.findAll());

    }

    public List<Setting> listSettings() {
        return Lists.newArrayList(settingsRepository.findAll());
    }

    public ConnectionSettings getConnection(long id) {
        return connectionRepository.findOne(id);
    }

    public ConnectionSettings setConnection(ConnectionSettings connectionSettings) {
        return connectionRepository.save(connectionSettings);
    }
}
