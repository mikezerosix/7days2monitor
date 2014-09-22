package org.mikezerosix.rest;

import com.google.common.collect.Maps;
import org.mikezerosix.entities.User;
import org.mikezerosix.entities.UserRepository;
import org.mikezerosix.rest.transformers.JsonTransformer;
import org.mikezerosix.util.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.mikezerosix.service.SettingsService.PROTECTED_URL;
import static org.mikezerosix.util.JsonUtil.fromJson;
import static spark.Spark.*;

public class LoginResource {
    private static final Logger log = LoggerFactory.getLogger(LoginResource.class);
    public static final String PATH = PROTECTED_URL + "login";
    private UserRepository userRepository;

    public LoginResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerRoutes() {

        get("/public/login", (request, response) -> {
            final User sessionUser = SessionUtil.getSessionUser(request);
            return getUserData(sessionUser);
        }, new JsonTransformer());

        post("/public/login", (request, response) -> {
            final User requestUser = fromJson(request, User.class);
            final User user = userRepository.findByNameAndPassword(requestUser.getName(), requestUser.getPassword());
            if (user == null) {
                response.status(401);
                log.error("Could not find matching user: " + requestUser);
                return null;
            }
            log.info("User:" + user.getName() + " logged in");
            SessionUtil.setSessionUser(request, user);
            return getUserData(user);

        }, new JsonTransformer());

        delete(PROTECTED_URL + "login", (request, response) -> {
            SessionUtil.setSessionUser(request, null);
            return "Logged out";
        });
    }

    private Map<String, String> getUserData(User sessionUser) {
        Map<String, String> useData = Maps.newHashMap();
        useData.put("user", sessionUser.getName());
        useData.put("role", sessionUser.getName());
        return useData;
    }

}
