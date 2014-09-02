package org.mikezerosix.rest;

import org.apache.commons.collections.IteratorUtils;
import org.mikezerosix.entities.Server;
import org.mikezerosix.entities.ServerRepository;
import org.mikezerosix.entities.User;
import org.mikezerosix.entities.UserRepository;
import org.mikezerosix.util.SessionUtil;

import static org.mikezerosix.AppConfiguration.PROTECTED_URL;
import static org.mikezerosix.util.JsonUtil.fromJson;
import static org.mikezerosix.util.JsonUtil.toJson;
import static spark.Spark.*;

@SuppressWarnings("unchecked")
public class ServerResource {


    public ServerResource(ServerRepository serverRepository) {

        before(PROTECTED_URL + "*", (request, response) -> {
            if (!SessionUtil.isLoggedIn(request)) {
                halt(401, "You are not welcome here");
            }
        });

        get(PROTECTED_URL + "servers", (request, response) -> toJson(IteratorUtils.toList(serverRepository.findAll().iterator())));

        post(PROTECTED_URL + "servers", (request, response) -> {
            final Server server = fromJson(request, Server.class);
            if (server.getId() < 1) {
                serverRepository.save(server);
            } else {
                response.status(500);
            }
            return true;
        });

        put(PROTECTED_URL + "servers", (request, response) -> {
            final Server server = fromJson(request, Server.class);
            if (serverRepository.exists(server.getId())){
                serverRepository.save(server);
            } else {
                response.status(404);
            }
            return true;
        });

        delete(PROTECTED_URL + "servers/:id", (request, response) -> {
            final long id = Long.parseLong(request.queryParams("id"));
            serverRepository.delete(id);
            return true;
        });

    }
}
