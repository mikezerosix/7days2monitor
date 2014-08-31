package org.mikezerosix;

import org.apache.commons.io.IOUtils;
import org.mikezerosix.rest.LoginResource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static spark.Spark.*;
import static spark.SparkBase.setPort;

public class App {

    static   String user  = null;
    private static LoginResource loginResource;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);
        AppConfiguration app = context.getBean(AppConfiguration.class);

        String password = app.initPassword();
        int port = app.getPort();
        setPort(port);
        staticFileLocation("/static");
        loginResource = app.loginResource();

        System.out.println("HTTP service running in port: " + port + " password: " + password);

        //if server & autoconnect
        //2.1. if telnet conn & auto connect()
        //2.1. if ftp conn *&
    }

}
