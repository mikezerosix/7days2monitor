package org.mikezerosix.rest;

import org.mikezerosix.entities.User;
import org.mikezerosix.entities.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Set;

import static org.mikezerosix.AppConfiguration.PROTECTED_URL;
import static org.mikezerosix.rest.JsonUtil.fromJson;
import static org.mikezerosix.rest.JsonUtil.toJson;
import static spark.Spark.*;

public class LoginResource {
    private static final Logger log = LoggerFactory.getLogger(LoginResource.class);
    public static final String PATH = PROTECTED_URL + "login";

    public LoginResource(UserRepository userRepository) {

        get(PATH, (request, response) -> {
            return SessionUtil.getSessionUser(request) != null;
        });

        post(PATH, (request, response) -> {
            final User requestUser = fromJson(request, User.class);
            final User user = userRepository.findByNameAndPassword(requestUser.getName(), requestUser.getPassword());
            if (user == null) {
                for (User user1 : userRepository.findAll()) {
                    log.debug(user.getName());
                }
                response.status(401);
                log.error("Could not find matching user: " + requestUser);
                return null;
            }
            log.info("User:" + user.getName() + " logged in");
            SessionUtil.setSessionUser(request, user);
            return true;
        });

        delete(PATH, (request, response) -> {
            SessionUtil.setSessionUser(request, null);
            return "Logged out";
        });
    }

}
