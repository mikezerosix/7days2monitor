package org.mikezerosix.rest;

import org.mikezerosix.rest.transformers.JsonTransformer;
import org.mikezerosix.telnet.TelnetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import static org.mikezerosix.AppConfiguration.PROTECTED_URL;
import static spark.Spark.get;

/**
 * Created by michael on 7.9.2014.
 */
public class PlayerResource {
    private static final Logger log = LoggerFactory.getLogger(TelnetResource.class);

    public PlayerResource(TelnetService telnetService) {


        get(PROTECTED_URL + "telnet/status", (request, response) -> {
            Map<String, Boolean> res = new HashMap<>();
            res.put("alive", telnetService.isAlive());
            res.put("connected", telnetService.isConnected());
            return res;
        }, new JsonTransformer());

    }
}
