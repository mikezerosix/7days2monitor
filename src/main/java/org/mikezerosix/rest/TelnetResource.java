package org.mikezerosix.rest;

import org.mikezerosix.handlers.AllHandler;
import org.mikezerosix.telnet.TelnetService;
import org.mikezerosix.util.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
                try {
                    final HttpServletResponse raw = response.raw();
                    telnetService.addHandler(new AllHandler(raw.getWriter()));
                    return raw;

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return false;
        });
    }
}
