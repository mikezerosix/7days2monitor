package org.mikezerosix;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.sql.SQLException;

import static spark.Spark.*;
import static spark.SparkBase.setPort;
import static spark.SparkBase.staticFileLocation;

public class App {
    public static final String PROTECTED = "/protected/";
    static   String user  = null;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);
        AppConfiguration app = context.getBean(AppConfiguration.class);

        String password = app.initPassword();
        int port = app.getPort();
        setPort(port);
        reqisterRoutes();

        System.out.println("HTTP service running in port: " + port + " password: " + password);

        //if server & autoconnect
        //2.1. if telnet conn & auto connect()
        //2.1. if ftp conn *&
    }


    private static void reqisterRoutes() {


        staticFileLocation("/static");

        before(PROTECTED + "*", (request, response) -> {
            if (user == null || !user.equals(request.session().attribute("user"))) {
                halt(401, "You are not welcome here");
            }
        });
        get(PROTECTED + "settings", (request, response) -> {
            return "7 days to die monitor ";
        });

        post(PROTECTED + "settings", (request, response) -> {
            return "7 days to die monitor ";
        });

        put(PROTECTED + "settings", (request, response) -> {
            return "login";
        });

    }
}
