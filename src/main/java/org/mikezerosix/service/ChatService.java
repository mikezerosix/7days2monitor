package org.mikezerosix.service;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import org.mikezerosix.LogConfiguration;
import org.mikezerosix.comet.CometMessage;
import org.mikezerosix.comet.CometSharedMessageQueue;
import org.mikezerosix.comet.MessageTarget;
import org.mikezerosix.rest.data.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatService {
    public static final Logger chatLogger = LoggerFactory.getLogger(ChatService.class);
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS ");
    private CometSharedMessageQueue cometSharedMessageQueue;

    public ChatService(CometSharedMessageQueue cometSharedMessageQueue) {
        this.cometSharedMessageQueue = cometSharedMessageQueue;
    }

    public void log(String chat) {
        cometSharedMessageQueue.addMessage(new CometMessage(MessageTarget.CHAT, buildChatMessage(chat)));
        chatLogger.info(chat);
    }

    private ChatMessage buildChatMessage(String chat) {
        return new ChatMessage(sdf.format(new Date()) + chat);
    }

    public void setMaxLogDays(int maxDays) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        final ch.qos.logback.classic.Logger logger = loggerContext.getLogger(ChatService.class);
        RollingFileAppender<ILoggingEvent> appender = (RollingFileAppender<ILoggingEvent>) logger.getAppender(LogConfiguration.CHAT_APPENDER);
        TimeBasedRollingPolicy<ILoggingEvent> policy = (TimeBasedRollingPolicy<ILoggingEvent>) appender.getRollingPolicy();
        policy.setMaxHistory(maxDays);
    }

}
