package org.mikezerosix.rest;

import org.mikezerosix.entities.ConnectionSettings;
import org.mikezerosix.entities.Setting;
import org.mikezerosix.rest.transformers.JsonTransformer;
import org.mikezerosix.service.SettingsService;

import static org.mikezerosix.service.SettingsService.PROTECTED_URL;
import static org.mikezerosix.util.JsonUtil.fromJson;
import static spark.Spark.get;
import static spark.Spark.put;

@SuppressWarnings("unchecked")
public class SettingsResource {
    private long started = System.currentTimeMillis();
    private SettingsService settingsService;

    public SettingsResource(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    public void registerRoutes() {

        get(PROTECTED_URL + "settings", (request, response) -> settingsService.listSettings(), new JsonTransformer());

        put(PROTECTED_URL + "settings", (request, response) -> settingsService.setSetting(fromJson(request, Setting.class)), new JsonTransformer());

        get(PROTECTED_URL + "settings/connections", (request, response) -> settingsService.listConnections(), new JsonTransformer());

        get(PROTECTED_URL + "settings/uptime", (request, response) -> (System.currentTimeMillis() - started), new JsonTransformer());

        put(PROTECTED_URL + "settings/connections", (request, response) -> {
            final ConnectionSettings connectionSettings = fromJson(request, ConnectionSettings.class);
            if (settingsService.getConnection(connectionSettings.getId()) != null) {
                return settingsService.setConnection(connectionSettings);
            } else {
                response.status(404);
                return "Error finding connection: " + connectionSettings.getId() ;
            }
        }, new JsonTransformer());

    }

}

