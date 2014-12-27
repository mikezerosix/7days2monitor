package org.mikezerosix.rest;

import org.mikezerosix.entities.Monitor;
import org.mikezerosix.rest.transformers.JsonTransformer;
import org.mikezerosix.service.MonitorService;

import static org.mikezerosix.service.SettingsService.PROTECTED_URL;
import static org.mikezerosix.util.JsonUtil.fromJson;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by michael on 23.10.2014.
 */
public class MonitorResource {
    private MonitorService monitorService;

    public MonitorResource(MonitorService monitorService) {
        this.monitorService = monitorService;
    }

    public void registerRoutes() {
        get(PROTECTED_URL + "/monitors", (request, response) -> monitorService.list(), new JsonTransformer());

        post(PROTECTED_URL + "/monitors", (request, response) -> monitorService.create(fromJson(request, Monitor.class)), new JsonTransformer());
    }
}
