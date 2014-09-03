package org.mikezerosix.rest;

import org.apache.commons.collections.IteratorUtils;
import org.mikezerosix.entities.Server;
import org.mikezerosix.entities.ServerRepository;
import org.mikezerosix.telnet.TelnetService;
import org.mikezerosix.util.SessionUtil;

import static org.mikezerosix.AppConfiguration.PROTECTED_URL;
import static org.mikezerosix.util.JsonUtil.fromJson;
import static org.mikezerosix.util.JsonUtil.toJson;
import static spark.Spark.*;

@SuppressWarnings("unchecked")
public class TelnetResource {


    public TelnetResource(TelnetService telnetService, Thread thread) {

        before(PROTECTED_URL + "*", (request, response) -> {
            if (!SessionUtil.isLoggedIn(request)) {
                halt(401, "You are not welcome here");
            }
        });

        get(PROTECTED_URL + "telnet", (request, response) -> telnetService.isLoggedIn());

        post(PROTECTED_URL + "telnet", (request, response) -> {
            thread.start();
            return true;
        });

        delete(PROTECTED_URL + "telnet", (request, response) -> {
            thread.stop();
            return true;
        });

    }
}
