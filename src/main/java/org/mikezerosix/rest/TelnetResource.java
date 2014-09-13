package org.mikezerosix.rest;

import org.apache.commons.net.telnet.InvalidTelnetOptionException;
import org.mikezerosix.rest.transformers.JsonTransformer;
import org.mikezerosix.telnet.TelnetService;
import org.mikezerosix.util.SessionUtil;
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

import static org.mikezerosix.AppConfiguration.PROTECTED_URL;
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

        get(PROTECTED_URL + "telnet", (request, response) -> getStatus(telnetService) , new JsonTransformer());

        post(PROTECTED_URL + "telnet", (request, response) -> {
            if (!telnetService.isConnected()) {
                try {
                    telnetService.connect();
                } catch (Exception e) {
                    log.error("Failed to call connect() on telnetService ", e);
                    response.status(500);
                }
            }
            return getStatus(telnetService);
        });

        delete(PROTECTED_URL + "telnet", (request, response) -> {
            if (telnetService.isConnected()) {
                try {
                    telnetService.disconnect();
                } catch (Exception e) {
                    log.error("Failed to call disconnect() on telnetService ", e);
                    response.status(500);
                }
            }
            return getStatus(telnetService);
        });

        get(PROTECTED_URL + "telnet/server-info", (request, response) -> telnetService.getServerInformation(), new JsonTransformer());

        get(PROTECTED_URL + "telnet/raw", (request, response) -> {
            if (telnetService.isAlive()) {
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
            if (telnetService.isAlive()) {
                telnetService.write(say);
                return true;
            }
            return returnDeadConnectionError(response);
        });

        post(PROTECTED_URL + "telnet/send-cmd", (request, response) -> {
            String cmd =  request.body();
            if (telnetService.isAlive()) {
                log.info("sending cmd: " + cmd);
                telnetService.write(cmd);
                return true;
            }
            return returnDeadConnectionError(response);
        });



    }

    private Map<String, Boolean> getStatus(TelnetService telnetService) {
        Map<String, Boolean> res = new HashMap<>();
        res.put("alive", telnetService.isAlive());
        res.put("connected", telnetService.isConnected());
        res.put("monitoring", telnetService.isMonitoring());
        return res;
    }

    private String returnDeadConnectionError(Response response) {
       log.warn("calling dead telnet connection");
       response.status(500);
       return "telnet connection is dead";
   }
}
