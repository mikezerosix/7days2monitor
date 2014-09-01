package org.mikezerosix.rest;

import org.mikezerosix.entities.User;
import org.mikezerosix.entities.UserRepository;

import static org.mikezerosix.AppConfiguration.PROTECTED_URL;
import static org.mikezerosix.rest.JsonUtil.toJson;
import static spark.Spark.*;

public class LoginResource {

    public static final String PATH = PROTECTED_URL + "login";

    public LoginResource(UserRepository userRepository) {

        get(PATH, (request, response) -> {
            return toJson(SessionUtil.getSessionUser(request) != null);
        });

        post(PATH, (request, response) -> {
            String name = request.params("name");
            String password = request.params("password");
            final User user = new User(); //userRepository.findByNameAndPassword(name, password);
            user.setName("admin");
            SessionUtil.setSessionUser(request, user);
            return toJson(user);
        });

        delete(PATH, (request, response) -> {
            SessionUtil.setSessionUser(request, null);
            return "Logged out";
        });
    }

}
