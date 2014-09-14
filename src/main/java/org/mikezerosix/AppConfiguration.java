package org.mikezerosix;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import org.eclipse.jetty.util.security.Credential;
import org.mikezerosix.actions.ChatLogger;
import org.mikezerosix.entities.*;
import org.mikezerosix.ftp.FTPService;
import org.mikezerosix.rest.*;
import org.mikezerosix.telnet.TelnetService;
import org.mikezerosix.telnet.handlers.ChatHandler;
import org.mikezerosix.telnet.handlers.PlayerLoginHandler;
import org.mikezerosix.telnet.handlers.StatHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.sql.SQLException;

@Configuration
@ComponentScan
@EnableJpaRepositories
@EnableTransactionManagement

@Import({JdbcConfiguration.class})
public class AppConfiguration {
    public static final String PORT = "port";
    public static final String PROTECTED_URL = "/protected/";
    public static final String ADMIN = "admin";
    public static final String SETTING_CURRENT_SERVER = "CURRENT_SERVER";
    public static final String SETTING_CHAT_HANDLER_ENABLE = "CHAT_HANDLER_ENABLE";
    public static final String SETTING_STAT_HANDLER_ENABLE = "STAT_HANDLER_ENABLE";

    private static final Logger log = LoggerFactory.getLogger(AppConfiguration.class);
    private static TelnetService telnetService = TelnetService.getInstance();
    private static FTPService ftpService = FTPService.getInstance();

    @Inject
    private SettingsRepository settingsRepository;

    @Inject
    private ConnectionRepository connectionRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PlayerRepository playerRepository;

    @Inject
    private StatRepository statRepository;

    @PostConstruct
    public void init() throws SQLException {
        initLogger();
        telnetService.start();
        initUsers();
        initSettings();
        initConnections();
        telnetService.addHandler(new PlayerLoginHandler(playerRepository));
    }

    private void initSettings() {
        final Iterable<Settings> settings = settingsRepository.findAll();
        for (Settings setting : settings) {
            settingsChange(setting);
        }
    }

    private void initConnections() {
        for (ConnectionType connectionType : ConnectionType.values()) {
            ConnectionSettings connectionSettings = connectionRepository.findByType(connectionType);
            if (connectionSettings == null) {
                connectionSettings = new ConnectionSettings();
                connectionSettings.setType(connectionType);
                connectionRepository.save(connectionSettings);
            } else {
                connectionChange(connectionSettings);
            }
        }
    }

    public void connectionChange(ConnectionSettings connectionSettings) {
        switch (connectionSettings.getType()) {
            case GAME_TELNET:
                telnetService.setConnectionSettings(connectionSettings);
                if (connectionSettings.isAuto() && !telnetService.isConnected()) {
                    try {
                        telnetService.connect();
                    } catch (Exception e) {
                        log.error("Failed to call connect() on telnetService ", e);
                    }
                }
                break;

            case GAME_FTP:
                ftpService.config(connectionSettings);
                if (connectionSettings.isAuto()) {
                    try {
                        ftpService.connect();
                    } catch (IOException e) {
                        log.error("FTP connection failed", e);
                    }
                }
                break;

            //TODO: other connection
        }
    }

    private void initUsers() {
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
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        // Needed or otherwise Logback will use it's default console appender without asking us
        loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).detachAndStopAllAppenders();

        PatternLayoutEncoder ple = new PatternLayoutEncoder();
        ple.setPattern("%date %level [%thread] %logger{10} [%file:%line] %msg%n");
        ple.setContext(loggerContext);
        ple.start();

        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setEncoder(ple);
        consoleAppender.setContext(loggerContext);
        consoleAppender.start();

        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        fileAppender.setFile("7days2monitor.log");
        fileAppender.setEncoder(ple);
        fileAppender.setContext(loggerContext);
        fileAppender.start();

        PatternLayoutEncoder chatEncoder = new PatternLayoutEncoder();

        chatEncoder.setPattern("%date %msg%n");
        chatEncoder.setContext(loggerContext);
        chatEncoder.start();


        RollingFileAppender<ILoggingEvent> chatAppender = new RollingFileAppender<>();
        chatAppender.setFile("chat.log");
        chatAppender.setEncoder(chatEncoder);
        chatAppender.setContext(loggerContext);

        TimeBasedRollingPolicy<ILoggingEvent> policy = new TimeBasedRollingPolicy<>();
        policy.setFileNamePattern("chat.%d");
        policy.setParent(chatAppender);
        policy.setContext(loggerContext);
        policy.start();
        chatAppender.setRollingPolicy(policy);

        chatAppender.start();


        loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(consoleAppender);
        loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).setLevel(Level.INFO);

        loggerContext.getLogger(TelnetService.class).setLevel(Level.DEBUG);
        loggerContext.getLogger(TelnetService.class).addAppender(consoleAppender);
        loggerContext.getLogger(TelnetService.class).addAppender(fileAppender);

        loggerContext.getLogger(ChatLogger.class).setLevel(Level.ALL);
        loggerContext.getLogger(ChatLogger.class).addAppender(chatAppender);
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
        return new SettingsResource(settingsRepository, this, connectionRepository);
    }

    public UserResource userResource() {
        return new UserResource(userRepository);
    }

    public TelnetResource telnetResource() {
        return new TelnetResource(telnetService);
    }

    public PlayerResource playerResource() {
        return new PlayerResource(telnetService, playerRepository);
    }

    public void settingsChange(Settings settings) {
        switch (settings.getId()) {
            case SETTING_CHAT_HANDLER_ENABLE:
                if (Boolean.parseBoolean(settings.getValue())) {
                    telnetService.addHandler(new ChatHandler());
                } else {
                    telnetService.removeHandler(ChatHandler.class);
                }
                break;
            case SETTING_STAT_HANDLER_ENABLE:
                if (Boolean.parseBoolean(settings.getValue())) {
                    telnetService.addHandler(new StatHandler(statRepository));
                } else {
                    telnetService.removeHandler(StatHandler.class);
                }
                break;

        }
    }

    public FTPResource ftpResource() {
        return new FTPResource(ftpService);
    }

    public ServerResource serverResource() {
        return new ServerResource();
    }
    public StatResource statResource() {
        return new StatResource(statRepository);
    }
}
