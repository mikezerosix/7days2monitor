package org.mikezerosix;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import org.eclipse.jetty.util.security.Credential;
import org.mikezerosix.entities.*;
import org.mikezerosix.rest.*;
import org.mikezerosix.telnet.TelnetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.sql.SQLException;

@Configuration
@ComponentScan
@EnableJpaRepositories
@EnableTransactionManagement

@Import({JdbcConfiguration.class})
public class AppConfiguration {
    public static final String PASSWORD = "password";
    public static final String PORT = "port";
    public static final String PROTECTED_URL = "/protected/";
    public static final String ADMIN = "admin";
    private static final Logger log = LoggerFactory.getLogger(AppConfiguration.class);

    private static TelnetService telnetService;
    private static Thread thread;

    @Inject
    private SettingsRepository settingsRepository;

    @Inject
    private ServerRepository serverRepository;

    @Inject
    private UserRepository userRepository;

    @PostConstruct
    public void init() throws SQLException {
        initLogger();
        initPassword();
    }

    public void initPassword() {
        if (userRepository.count() < 1) {
            String password = Credential.MD5.digest("" + Math.random()).substring(5, 13);
            //TODO: remove debug
            password = "x";
            final String msg = "New installation, creating user: admin , password: " + password;
            log.info(msg);
            System.out.println(msg);
            final User user = new User();
            user.setName(ADMIN);
            user.setPassword(password);
            userRepository.save(user);
        }
    }

    private Settings setSetting(String key, String value) {
        return settingsRepository.save(new Settings(value, key));
    }

    private String getSetting(String key) {
        Settings settings = settingsRepository.findOne(key);
        return settings != null ? settings.getValue() : null;
    }


    private void initLogger() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        PatternLayoutEncoder ple = new PatternLayoutEncoder();

        ple.setPattern("%date %level [%thread] %logger{10} [%file:%line] %msg%n");
        ple.setContext(lc);
        ple.start();

        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setEncoder(ple);
        consoleAppender.setContext(lc);
        consoleAppender.start();

        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        fileAppender.setFile("7days2monitor.log");
        fileAppender.setEncoder(ple);
        fileAppender.setContext(lc);
        fileAppender.start();

    }


    public int getPort() {
        String port = getSetting(PORT);
        try {
            return Integer.parseInt(port);
        } catch (NumberFormatException e) {
            return 9090;
        }
    }

    public LoginResource loginResource() {
        return new LoginResource(userRepository);
    }

    public SettingsResource settingsResource() {
        return new SettingsResource(settingsRepository);
    }

    public UserResource userResource() {
        return new UserResource(userRepository);
    }

    public ServerResource serverResource() {return new ServerResource(serverRepository);}
    public TelnetResource telnetResource() {
        return new TelnetResource(telnetService, thread);
    }
}
