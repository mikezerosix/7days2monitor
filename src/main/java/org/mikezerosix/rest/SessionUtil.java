package org.mikezerosix.rest;

import org.mikezerosix.entities.User;
import spark.Request;
import spark.Session;

public class SessionUtil {


    public static User getSessionUser(Request request) {
        return request.session().attribute("user");
    }

    public static void setSessionUser(Request request, User user) {
         request.session().attribute("user", user);
    }

    public static boolean isLoggedIn(Request request) {
        return getSessionUser(request) != null;
    }
}
