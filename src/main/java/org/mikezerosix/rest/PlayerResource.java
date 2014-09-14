package org.mikezerosix.rest;

import com.google.common.collect.Lists;
import org.mikezerosix.entities.Player;
import org.mikezerosix.entities.PlayerRepository;
import org.mikezerosix.rest.transformers.JsonTransformer;
import org.mikezerosix.telnet.TelnetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static org.mikezerosix.AppConfiguration.PROTECTED_URL;
import static spark.Spark.get;

public class PlayerResource {
    private static final Logger log = LoggerFactory.getLogger(PlayerResource.class);

    public PlayerResource(TelnetService telnetService, PlayerRepository playerRepository) {

        get(PROTECTED_URL + "players", (request, response) ->
             Lists.newArrayList(playerRepository.findAll())
        , new JsonTransformer());

    }
}
