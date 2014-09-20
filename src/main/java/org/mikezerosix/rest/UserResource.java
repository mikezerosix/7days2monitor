package org.mikezerosix.rest;

import org.apache.commons.collections.IteratorUtils;
import org.mikezerosix.entities.User;
import org.mikezerosix.entities.UserRepository;
import org.mikezerosix.util.SessionUtil;

import static org.mikezerosix.service.SettingsService.PROTECTED_URL;
import static org.mikezerosix.util.JsonUtil.fromJson;
import static org.mikezerosix.util.JsonUtil.toJson;
import static spark.Spark.*;
@SuppressWarnings("unchecked")
public class UserResource {


    private UserRepository userRepository;

    public UserResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerRoutes() {
        before(PROTECTED_URL + "*", (request, response) -> {
            if (!SessionUtil.isLoggedIn(request)) {
                halt(401, "You are not welcome here");
            }
        });

        get(PROTECTED_URL + "users", (request, response) -> toJson(IteratorUtils.toList(userRepository.findAll().iterator())));

        put(PROTECTED_URL + "users", (request, response) -> {
            final User user = fromJson(request, User.class);
            if (userRepository.exists(user.getId())){
                userRepository.save(user);
            }
            return true;
        });
    }
}
