package org.mikezerosix.rest;

import org.mikezerosix.entities.User;
import org.mikezerosix.entities.UserRepository;

import static org.mikezerosix.AppConfiguration.PROTECTED_URL;
import static spark.Spark.*;

public class LoginResource {

    public static final String PATH = PROTECTED_URL + "login";

    public LoginResource(UserRepository userRepository) {

        get(PATH, (request, response) -> {
            String name = request.params("name");
            String password = request.params("password");
            return userRepository.findByNameAndPassword(name, password);
        });

        post(PATH, (request, response) -> {
            String name = request.params("name");
            String password = request.params("password");
            final User user = userRepository.findByNameAndPassword(name, password);
            SessionUtil.setSessionUser(request, user);
            if (user != null) {
                response.redirect("/bar");
            }
            return false;
        });

        delete(PATH, (request, response) -> {
            SessionUtil.setSessionUser(request, null);
            return "Logged out";
        });
    }

}
