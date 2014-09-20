package org.mikezerosix.rest;

import org.mikezerosix.rest.transformers.JsonTransformer;
import org.mikezerosix.telnet.TelnetRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Response;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mikezerosix.service.SettingsService.PROTECTED_URL;
import static spark.Spark.*;
@SuppressWarnings("unchecked")
public class TelnetResource {
    private static final Logger log = LoggerFactory.getLogger(TelnetResource.class);
    private TelnetRunner telnetRunner;

    public TelnetResource(TelnetRunner telnetRunner) {
        this.telnetRunner = telnetRunner;
    }

    public void registerRoutes() {
        get(PROTECTED_URL + "telnet", (request, response) -> getStatus(telnetRunner), new JsonTransformer());

        post(PROTECTED_URL + "telnet", (request, response) -> {
            if (!telnetRunner.isConnected()) {
                try {
                    telnetRunner.connect();
                } catch (Exception e) {
                    log.error("Failed to call connect() on telnetRunner ", e);
                    response.status(500);
                }
            }
            return getStatus(telnetRunner);
        });

        delete(PROTECTED_URL + "telnet", (request, response) -> {
            if (telnetRunner.isConnected()) {
                try {
                    telnetRunner.disconnect();
                } catch (Exception e) {
                    log.error("Failed to call disconnect() on telnetRunner ", e);
                    response.status(500);
                }
            }
            return getStatus(telnetRunner);
        });

        get(PROTECTED_URL + "telnet/server-info", (request, response) -> telnetRunner.getServerInformation(), new JsonTransformer());

        get(PROTECTED_URL + "telnet/raw", (request, response) -> {
            if (telnetRunner.isAlive()) {
                log.warn("raw is not supported , needs web sockets ");
            }
            return returnDeadConnectionError(response);
        });

        get(PROTECTED_URL + "telnet/chat", (request, response) -> {
            //TODO: Java WatchService for chat file , also store last read at client
            String lastRead = request.params("lastRead");
            String days = request.params("days");
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
            if (telnetRunner.isAlive()) {
                telnetRunner.write(say);
                return true;
            }
            return returnDeadConnectionError(response);
        });

        post(PROTECTED_URL + "telnet/send-cmd", (request, response) -> {
            String cmd = request.body();
            if (telnetRunner.isAlive()) {
                log.info("sending cmd: " + cmd);
                telnetRunner.write(cmd);
                return true;
            }
            return returnDeadConnectionError(response);
        });


    }

    private Map<String, Boolean> getStatus(TelnetRunner telnetRunner) {
        Map<String, Boolean> res = new HashMap<>();
        res.put("alive", telnetRunner.isAlive());
        res.put("connected", telnetRunner.isConnected());
        res.put("monitoring", telnetRunner.isMonitoring());
        return res;
    }

    private String returnDeadConnectionError(Response response) {
        log.warn("calling dead telnet connection");
        response.status(500);
        return "telnet connection is dead";
    }
}
