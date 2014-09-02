package org.mikezerosix;

import org.mikezerosix.entities.UserRepository;
import org.mikezerosix.rest.LoginResource;
import org.mikezerosix.rest.ServerResource;
import org.mikezerosix.rest.SettingsResource;
import org.mikezerosix.rest.UserResource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static spark.Spark.staticFileLocation;
import static spark.SparkBase.setPort;

public class App {

    static String user = null;
    private static LoginResource loginResource;
    private static SettingsResource settingsResource;
    private static UserResource userResource;
    private static ServerResource serverResource;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);
        AppConfiguration app = context.getBean(AppConfiguration.class);

        int port = app.getPort();
        setPort(port);
        staticFileLocation("/static");

        loginResource = app.loginResource();
        settingsResource = app.settingsResource();
        userResource = app.userResource();
        serverResource = app.serverResource();

        System.out.println("HTTP service running in port: " + port);

        //if server & autoconnect
        //2.1. if telnet conn & auto connect()
        //2.1. if ftp conn *&
    }

}
