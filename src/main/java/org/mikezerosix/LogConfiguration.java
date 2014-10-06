package org.mikezerosix;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.helper.RenameUtil;
import org.mikezerosix.service.ChatService;
import org.mikezerosix.telnet.TelnetRunner;
import org.slf4j.LoggerFactory;


public class LogConfiguration {

    public static final String CHAT_APPENDER = "chatAppender";
    public static boolean ran = false;

    public static void init() {
        if (ran) {
            throw new RuntimeException("init should never be called again !!!");
        }
        ran = true;
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.setName("7days2monitor");
        // Needed or otherwise Logback will use it's default console appender without asking us
        Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.detachAndStopAllAppenders();

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
        //chatAppender.setFile("chat.log");
        chatAppender.setPrudent(true);
        chatAppender.setEncoder(chatEncoder);
        chatAppender.setContext(loggerContext);
        chatAppender.setName(CHAT_APPENDER);


        TimeBasedRollingPolicy<ILoggingEvent> policy = new TimeBasedRollingPolicy<>();
        policy.setFileNamePattern("chat_%d{yyyy-MM-dd}.log");
        policy.setParent(chatAppender);
        policy.setContext(loggerContext);
        policy.setMaxHistory(30);

        policy.start();
        chatAppender.setRollingPolicy(policy);

        chatAppender.start();


        rootLogger.addAppender(consoleAppender);
        rootLogger.setLevel(Level.INFO);

        loggerContext.getLogger(TelnetRunner.class).setLevel(Level.DEBUG);
        loggerContext.getLogger(TelnetRunner.class).addAppender(consoleAppender);
        loggerContext.getLogger(TelnetRunner.class).addAppender(fileAppender);

        loggerContext.getLogger(ChatService.class).setLevel(Level.ALL);
        loggerContext.getLogger(ChatService.class).addAppender(chatAppender);
        loggerContext.getLogger(RenameUtil.class).setLevel(Level.ALL);
    }

}
