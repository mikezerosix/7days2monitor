package org.mikezerosix;

import org.apache.commons.cli.*;
import org.mikezerosix.rest.*;
import org.mikezerosix.service.SettingsService;
import org.mikezerosix.service.UserService;
import org.mikezerosix.telnet.TelnetRunner;
import org.mikezerosix.util.SessionUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import static org.mikezerosix.service.SettingsService.PROTECTED_URL;
import static spark.Spark.*;
import static spark.SparkBase.setPort;

@Component
public class App {
    private static int port = 9090;
    private static String password = "x";

    @Inject
    private SettingsService settingsService;
    @Inject
    private TelnetRunner telnetRunner;
    @Inject
    private UserService userService;

    @Inject
    private SettingsResource settingsResource;

    @Inject
    private LoginResource loginResource;
    @Inject
    private UserResource userResource;
    @Inject
    private TelnetResource telnetResource;
    @Inject
    private ServerResource serverResource;
    @Inject
    private StatResource statResource;
    @Inject
    private PlayerResource playerResource;
    @Inject
    private FTPResource ftpResource;
    @Inject
    private CometResource cometResource;

    public static void main(String[] args) {
        readArgs(args);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);
        App app = context.getBean(App.class);
        app.start();
    }

    private static void readArgs(String[] args) {
        HelpFormatter f = new HelpFormatter();

        Options opt = new Options();

        opt.addOption("h", false, "Print help for this application");
        opt.addOption("p", true, "The port to use");
        opt.addOption("l", true, "The initial login password for 'admin' user (only works if user DB is empty).");

        BasicParser parser = new BasicParser();
        try {
            CommandLine cl = parser.parse(opt, args);

            if (cl.hasOption('h')) {
                f.printHelp("OptionsTip", opt);
            } else {
                if (cl.hasOption('p')) {
                    port = Integer.parseInt(cl.getOptionValue("p"));
                }
                if (cl.hasOption('l')) {
                    password = cl.getOptionValue("l");
                }
            }
        } catch (ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    private void start() {
        userService.init(password);

        settingsService.init();

        startHTTP();

        System.out.println("HTTP service running in port: " + port);
    }

    private void startHTTP() {
        setPort(port);
        staticFileLocation("/static");

        before(PROTECTED_URL + "*", (request, response) -> {
            if (!SessionUtil.isLoggedIn(request)) {
                halt(401, "You are not welcome here");
            }
        });

        settingsResource.registerRoutes();
        loginResource.registerRoutes();
        userResource.registerRoutes();
        telnetResource.registerRoutes();
        serverResource.registerRoutes();
        statResource.registerRoutes();
        playerResource.registerRoutes();
        ftpResource.registerRoutes();
        cometResource.registerRoutes();

    }
}
