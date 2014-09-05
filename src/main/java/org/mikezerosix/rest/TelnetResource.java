package org.mikezerosix.rest;

import org.mikezerosix.rest.transformers.JsonTransformer;
import org.mikezerosix.telnet.TelnetService;
import org.mikezerosix.util.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mikezerosix.AppConfiguration.PROTECTED_URL;
import static org.mikezerosix.util.JsonUtil.toJson;
import static spark.Spark.*;

@SuppressWarnings("unchecked")
public class TelnetResource {
    private static final Logger log = LoggerFactory.getLogger(TelnetResource.class);

    public TelnetResource(TelnetService telnetService) {

        before(PROTECTED_URL + "*", (request, response) -> {
            if (!SessionUtil.isLoggedIn(request)) {
                halt(401, "You are not welcome here");
            }
        });

        get(PROTECTED_URL + "telnet/status", (request, response) -> {
            Map<String, Boolean> res = new HashMap<>();
            res.put("alive", telnetService.isAlive());
            res.put("connected", telnetService.isConnected());
            return res;
        }, new JsonTransformer());

        post(PROTECTED_URL + "telnet", (request, response) -> {
            if (!telnetService.isAlive()) {
                telnetService.start();
            }
            log.warn("calling start on live telnet connection");
            return toJson(true);
        });

        delete(PROTECTED_URL + "telnet", (request, response) -> {
            if (telnetService.isAlive()) {
                telnetService.interrupt();
            }
            log.warn("calling disconnect on dead telnet connection");
            return toJson(true);
        });

        get(PROTECTED_URL + "telnet/raw", (request, response) -> {
            if (telnetService.isAlive()) {
                log.warn("raw is not supported , needs web sockets ");
            }
            return false;
        });

        get(PROTECTED_URL + "telnet/chat", (request, response) -> {
            //TODO: Java WatchService for chat file , also store last read
            List<String> lines = new ArrayList<>();
            try {
                final File file = new File("chat.log");
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
                return lines;
            } catch (IOException e) {
                response.status(500);
                log.error("IO exception on reading chat", e);
                return e;
            }
        }, new JsonTransformer());

        post(PROTECTED_URL + "telnet/say", (request, response) -> {
            String say = "say " + request.body();
            if (telnetService.isAlive()) {
                telnetService.write(say);
                return true;
            }
            log.warn("calling say on dead telnet connection");
            response.status(500);
            return "telnet connection is dead";
        });
    }

}
