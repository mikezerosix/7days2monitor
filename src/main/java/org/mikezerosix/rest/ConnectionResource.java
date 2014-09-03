package org.mikezerosix.rest;

import org.apache.commons.collections.IteratorUtils;
import org.mikezerosix.entities.Linkage;
import org.mikezerosix.entities.ConnectionRepository;
import org.mikezerosix.util.SessionUtil;

import static org.mikezerosix.AppConfiguration.PROTECTED_URL;
import static org.mikezerosix.util.JsonUtil.fromJson;
import static org.mikezerosix.util.JsonUtil.toJson;
import static spark.Spark.*;

@SuppressWarnings("unchecked")
public class ConnectionResource {


    public ConnectionResource(ConnectionRepository connectionRepository) {

        before(PROTECTED_URL + "*", (request, response) -> {
            if (!SessionUtil.isLoggedIn(request)) {
                halt(401, "You are not welcome here");
            }
        });

        get(PROTECTED_URL + "connections", (request, response) -> toJson(IteratorUtils.toList(connectionRepository.findAll().iterator())));

        put(PROTECTED_URL + "connections", (request, response) -> {
            final Linkage linkage = fromJson(request, Linkage.class);
            if (connectionRepository.exists(linkage.getId())) {
                connectionRepository.save(linkage);
            } else {
                response.status(404);
            }
            return true;
        });


    }
}
