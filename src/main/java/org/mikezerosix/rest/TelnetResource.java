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
import java.util.List;

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
        get(PROTECTED_URL + "telnet", (request, response) -> telnetRunner.getStatus().name());

        post(PROTECTED_URL + "telnet", (request, response) -> {
            if (telnetRunner.getStatus().equals(TelnetRunner.TelnetStatus.DISCONNECTED)) {
                try {
                    telnetRunner.connect();
                } catch (Exception e) {
                    log.error("Failed to call connect() on telnetRunner ", e);
                    response.status(500);
                }
            }
            return telnetRunner.getStatus().name();
        });

        delete(PROTECTED_URL + "telnet", (request, response) -> {
            try {
                telnetRunner.disconnect();
            } catch (Exception e) {
                log.error("Failed to call disconnect() on telnetRunner ", e);
                response.status(500);
            }
            return telnetRunner.getStatus().name();
        });

        get(PROTECTED_URL + "telnet/server-info", (request, response) -> telnetRunner.getServerInformation(), new JsonTransformer());

        get(PROTECTED_URL + "telnet/chat", (request, response) -> {
            //TODO: a lot
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
                try {
                    telnetRunner.write(say);
                    return true;
                } catch (IOException e) {
                    log.error("failed to send command: " + say);
                }
            }
            return returnDeadConnectionError(response);
        });

        post(PROTECTED_URL + "telnet/send-cmd", (request, response) -> {
            String cmd = request.body();
            if (telnetRunner.isAlive()) {
                log.info("sending cmd: " + cmd);
                try {
                    telnetRunner.write(cmd);
                    return true;
                } catch (IOException e) {
                    log.error("failed to send command: " + cmd );
                }
            }
            return returnDeadConnectionError(response);
        });


    }

    private String returnDeadConnectionError(Response response) {
        log.warn("calling dead telnet connection");
        response.status(500);
        return "telnet connection is dead";
    }
}
