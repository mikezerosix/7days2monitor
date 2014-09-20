package org.mikezerosix.rest;

import org.apache.commons.collections.IteratorUtils;
import org.mikezerosix.entities.ConnectionRepository;
import org.mikezerosix.entities.ConnectionSettings;
import org.mikezerosix.entities.Settings;
import org.mikezerosix.entities.SettingsRepository;
import org.mikezerosix.rest.transformers.JsonTransformer;

import static org.mikezerosix.service.SettingsService.PROTECTED_URL;
import static org.mikezerosix.util.JsonUtil.fromJson;
import static org.mikezerosix.util.JsonUtil.toJson;
import static spark.Spark.get;
import static spark.Spark.put;
@SuppressWarnings("unchecked")
public class SettingsResource {
    private long started = System.currentTimeMillis();
    private SettingsRepository settingsRepository;
    private ConnectionRepository connectionRepository;

    public SettingsResource(SettingsRepository settingsRepository, ConnectionRepository connectionRepository) {
        this.settingsRepository = settingsRepository;
        this.connectionRepository = connectionRepository;
    }

    public void registerRoutes() {

        get(PROTECTED_URL + "settings", (request, response) -> toJson(IteratorUtils.toList(settingsRepository.findAll().iterator())));

        put(PROTECTED_URL + "settings", (request, response) -> {
            final Settings settings = fromJson(request, Settings.class);
            final Settings save = settingsRepository.save(settings);
            //   appConfiguration.settingsChange(settings);
            return toJson(save);
        });

        get(PROTECTED_URL + "settings/connections", (request, response) -> IteratorUtils.toList(connectionRepository.findAll().iterator()), new JsonTransformer());
        get(PROTECTED_URL + "settings/uptime", (request, response) -> (System.currentTimeMillis() - started), new JsonTransformer());

        put(PROTECTED_URL + "settings/connections", (request, response) -> {
            final ConnectionSettings connectionSettings = fromJson(request, ConnectionSettings.class);
            if (connectionRepository.exists(connectionSettings.getId())) {
                connectionRepository.save(connectionSettings);
                //     appConfiguration.connectionChange(connectionSettings);
            } else {
                response.status(404);
            }
            return true;
        }, new JsonTransformer());

    }

}

