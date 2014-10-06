package org.mikezerosix.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import org.junit.Before;
import org.junit.Test;
import org.mikezerosix.service.ChatService;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** On Windows 7 SP1 - 64 Bit with laterst updates
 *  Java 1.8u20 64-bit (Windows)
 *  logback-core 1.1.2
 *  logback-classic 1.1.2
 *
 *
 * Created by michael on 6.10.2014.
 */
public class LogRotationTest {

    @Before
    public void setUp() throws Exception {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        // Needed or otherwise Logback will use it's default console appender without asking us
        Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.detachAndStopAllAppenders();
        PatternLayoutEncoder chatEncoder = new PatternLayoutEncoder();

        chatEncoder.setPattern("%date %msg%n");
        chatEncoder.setContext(loggerContext);
        chatEncoder.start();


        RollingFileAppender<ILoggingEvent> chatAppender = new RollingFileAppender<>();
        chatAppender.setFile("test.log");
        chatAppender.setEncoder(chatEncoder);
        chatAppender.setContext(loggerContext);


        TimeBasedRollingPolicy<ILoggingEvent> policy = new TimeBasedRollingPolicy<>();
        policy.setFileNamePattern("test%d{yyyyMMddHHmmss}.log");
        policy.setParent(chatAppender);
        policy.setContext(loggerContext);
        //       policy.setMaxHistory(30);
        policy.start();
        chatAppender.setRollingPolicy(policy);

        chatAppender.start();
        loggerContext.getLogger(LogRotationTest.class).setLevel(Level.ALL);
        loggerContext.getLogger(LogRotationTest.class).addAppender(chatAppender);
    }

    @Test
    public void testRotation() throws Exception {
        org.slf4j.Logger logger = LoggerFactory.getLogger(LogRotationTest.class);
        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            executor.execute( new LogRunner(i));
        }


    }

    public class LogRunner implements Runnable {
        org.slf4j.Logger logger = LoggerFactory.getLogger(LogRotationTest.class);
         int id;

        public LogRunner(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            for (int i = 0; i < 20; i++) {
                logger.info("id"+id+ " tick " + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {


                }
            }

        }
    }
}
