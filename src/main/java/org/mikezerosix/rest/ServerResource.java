package org.mikezerosix.rest;

import com.google.common.collect.Lists;
import org.mikezerosix.entities.PlayerRepository;
import org.mikezerosix.rest.transformers.JsonTransformer;
import org.mikezerosix.steam.SteamAPI;
import org.mikezerosix.telnet.TelnetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.mikezerosix.AppConfiguration.PROTECTED_URL;
import static spark.Spark.get;

public class ServerResource {
    private static final Logger log = LoggerFactory.getLogger(ServerResource.class);

    public ServerResource() {

        //TODO: refactor some storage and poller for latest version data
        get(PROTECTED_URL + "/server/news", (request, response) -> {
            try {
                return new SteamAPI(null).getGameNews();
            } catch (IOException e) {
                response.status(500);
                return "failed to connect SteamAPI" + e.getMessage();
            }
        }
        );

    }
}
