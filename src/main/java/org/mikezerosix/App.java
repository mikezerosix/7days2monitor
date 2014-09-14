package org.mikezerosix;

import org.mikezerosix.rest.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static spark.Spark.staticFileLocation;
import static spark.SparkBase.setPort;

public class App {

    static String user = null;
    private static LoginResource loginResource;
    private static SettingsResource settingsResource;
    private static UserResource userResource;
    private static TelnetResource telnetResource;
    private static PlayerResource playerResource;
    private static FTPResource ftpResource;
    private static ServerResource serverResource;
    private static StatResource statResource;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);
        AppConfiguration app = context.getBean(AppConfiguration.class);

        int port = app.getPort();
        setPort(port);
        staticFileLocation("/static");

        loginResource = app.loginResource();
        settingsResource = app.settingsResource();
        userResource = app.userResource();
        telnetResource = app.telnetResource();
        playerResource = app.playerResource();
        ftpResource = app.ftpResource();
        serverResource = app.serverResource();
        statResource = app.statResource();

        System.out.println("HTTP service running in port: " + port);

    }

}
