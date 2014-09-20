package org.mikezerosix.rest;

import com.google.common.collect.Lists;
import org.mikezerosix.entities.PlayerRepository;
import org.mikezerosix.rest.transformers.JsonTransformer;
import org.mikezerosix.service.MonitoringService;
import org.mikezerosix.telnet.TelnetRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mikezerosix.service.SettingsService.PROTECTED_URL;
import static spark.Spark.get;

public class PlayerResource {
    private static final Logger log = LoggerFactory.getLogger(PlayerResource.class);
    private MonitoringService monitoringService;
    private PlayerRepository playerRepository;

    public PlayerResource(MonitoringService monitoringService, PlayerRepository playerRepository) {

        this.monitoringService = monitoringService;
        this.playerRepository = playerRepository;
    }

    public void registerRoutes() {
        get(PROTECTED_URL + "players", (request, response) ->
             Lists.newArrayList(playerRepository.findAll())
        , new JsonTransformer());

    }
}
