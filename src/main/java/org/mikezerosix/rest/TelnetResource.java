package org.mikezerosix.rest;

import org.mikezerosix.telnet.TelnetService;
import org.mikezerosix.util.SessionUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mikezerosix.AppConfiguration.PROTECTED_URL;
import static spark.Spark.*;

@SuppressWarnings("unchecked")
public class TelnetResource {
    public TelnetResource(TelnetService telnetService) {

        before(PROTECTED_URL + "*", (request, response) -> {
            if (!SessionUtil.isLoggedIn(request)) {
                halt(401, "You are not welcome here");
            }
        });

        get(PROTECTED_URL + "telnet", (request, response) -> telnetService.isAlive());

        post(PROTECTED_URL + "telnet", (request, response) -> {
            if (!telnetService.isAlive()) {
                telnetService.start();
            }
            return true;
        });

        delete(PROTECTED_URL + "telnet", (request, response) -> {
            if (telnetService.isAlive()) {
                telnetService.interrupt();
            }
            return true;
        });
        get(PROTECTED_URL + "telnet/raw", (request, response) -> {
            if (telnetService.isAlive()) {

            }
            return false;
        });

        get(PROTECTED_URL + "telnet/chat", (request, response) -> {
            response.type("text/javascript");
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
                return e.getMessage();
            }
        }, new JsonTransformer());

    }

}
