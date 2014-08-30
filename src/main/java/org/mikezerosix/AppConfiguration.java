package org.mikezerosix;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import org.eclipse.jetty.util.security.Credential;
import org.mikezerosix.entities.Settings;
import org.mikezerosix.entities.SettingsRepository;
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
    private static final Logger log = LoggerFactory.getLogger(AppConfiguration.class);
    public static final String PASSWORD = "password";
    public static final String PORT = "port";
    @Inject
    private SettingsRepository settingsRepository;

    @PostConstruct
    public void init() throws SQLException {
        log.info("Initializing application");
        initLogger();
    }

    public String initPassword() {
        if (settingsRepository.exists(PASSWORD)) {
            return getSetting(PASSWORD);
        }
        String password = Credential.MD5.digest("" + Math.random()).substring(5,13);
        Settings save = setSetting(password, PASSWORD);
        return save.getValue();
    }

    private Settings setSetting(String key, String value) {
        return settingsRepository.save(new Settings(value, key));
    }

    private String getSetting(String password) {
        Settings settings = settingsRepository.findOne(password);
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
        /*
        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        fileAppender.setFile(file);
        fileAppender.setEncoder(ple);
        fileAppender.setContext(lc);
        fileAppender.start();*/

    }


    public int getPort() {
        String port = getSetting(PORT);
        try {
            return Integer.parseInt(port);
        } catch (NumberFormatException e) {
            return 9090;
        }
    }

}
