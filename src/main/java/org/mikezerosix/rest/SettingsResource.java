package org.mikezerosix.rest;

import org.apache.commons.collections.IteratorUtils;
import org.mikezerosix.AppConfiguration;
import org.mikezerosix.entities.Settings;
import org.mikezerosix.entities.SettingsRepository;
import org.mikezerosix.telnet.TelnetService;
import org.mikezerosix.util.SessionUtil;

import static org.mikezerosix.AppConfiguration.PROTECTED_URL;
import static org.mikezerosix.util.JsonUtil.fromJson;
import static org.mikezerosix.util.JsonUtil.toJson;
import static spark.Spark.*;

@SuppressWarnings("unchecked")
public class SettingsResource {

    public SettingsResource(SettingsRepository settingsRepository, AppConfiguration appConfiguration) {

        before(PROTECTED_URL + "*", (request, response) -> {
            if (!SessionUtil.isLoggedIn(request)) {
                halt(401, "You are not welcome here");
            }
        });

        get(PROTECTED_URL + "settings", (request, response) -> toJson(IteratorUtils.toList(settingsRepository.findAll().iterator())));

        put(PROTECTED_URL + "settings", (request, response) -> {
            final Settings settings = fromJson(request, Settings.class);
            final Settings save = settingsRepository.save(settings);
            appConfiguration.settingsChange(settings);
            return toJson(save);
        });
    }
}
