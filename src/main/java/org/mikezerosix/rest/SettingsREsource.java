package org.mikezerosix.rest;

import org.mikezerosix.AppConfiguration;

import static org.mikezerosix.AppConfiguration.PROTECTED_URL;
import static spark.Spark.*;

public class SettingsResource {

    public SettingsResource() {

        before(PROTECTED_URL + "*", (request, response) -> {
            if (!SessionUtil.isLoggedIn(request)) {
                halt(401, "You are not welcome here");
            }
        });
        get(PROTECTED_URL + "settings", (request, response) -> {
            return "7 days to die monitor ";
        });

        post(PROTECTED_URL + "settings", (request, response) -> {
            return "7 days to die monitor ";
        });

        put(PROTECTED_URL + "settings", (request, response) -> {
            return "login";
        });
    }
}
