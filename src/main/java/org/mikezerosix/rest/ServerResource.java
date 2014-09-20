package org.mikezerosix.rest;

import org.mikezerosix.steam.SteamAPI;
import org.mikezerosix.tumblr.TumblrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.mikezerosix.service.SettingsService.PROTECTED_URL;
import static spark.Spark.get;
public class ServerResource {
    private static final Logger log = LoggerFactory.getLogger(ServerResource.class);

    final SteamAPI steamAPI = new SteamAPI(null);
    final TumblrService tumblrService = new TumblrService();

    public ServerResource() {

    }

    public void registerRoutes() {
        //TODO: refactor some storage and poller for latest version data

        get(PROTECTED_URL + "/server/news/steam", (request, response) -> {
                    try {
                        return steamAPI.getGameNews();
                    } catch (IOException e) {
                        response.status(500);
                        return "failed to connect to SteamAPI" + e.getMessage();
                    }
                }
        );

        get(PROTECTED_URL + "/server/news/tumblr", (request, response) -> {
                    try {
                        return tumblrService.getLastJoelsBlog();
                    } catch (IOException e) {
                        response.status(500);
                        return "failed to connect to Tumbl" + e.getMessage();
                    }
                }
        );
    }
}
