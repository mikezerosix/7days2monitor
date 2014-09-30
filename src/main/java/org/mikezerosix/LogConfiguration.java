package org.mikezerosix;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import org.mikezerosix.service.ChatService;
import org.mikezerosix.telnet.TelnetRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogConfiguration {

    public static void init() {
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
        policy.setFileNamePattern("chat_%d.log");
        policy.setParent(chatAppender);
        policy.setContext(loggerContext);
        policy.start();
        chatAppender.setRollingPolicy(policy);

        chatAppender.start();


        loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(consoleAppender);
        loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).setLevel(Level.INFO);

        loggerContext.getLogger(TelnetRunner.class).setLevel(Level.DEBUG);
        loggerContext.getLogger(TelnetRunner.class).addAppender(consoleAppender);
        loggerContext.getLogger(TelnetRunner.class).addAppender(fileAppender);

        loggerContext.getLogger(ChatService.class).setLevel(Level.ALL);
        loggerContext.getLogger(ChatService.class).addAppender(chatAppender);
    }

}
