package org.mikezerosix.service;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import com.google.common.collect.Lists;
import com.sun.javafx.binding.StringFormatter;
import org.mikezerosix.LogConfiguration;
import org.mikezerosix.comet.CometMessage;
import org.mikezerosix.comet.CometSharedMessageQueue;
import org.mikezerosix.comet.MessageTarget;
import org.mikezerosix.rest.data.ChatMessage;
import org.mikezerosix.util.SafeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatService {
    public static Logger chatLogger = LoggerFactory.getLogger(ChatService.class);
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS ");
    private CometSharedMessageQueue cometSharedMessageQueue;
    private static AtomicInteger readingLog = new AtomicInteger(0);

    public ChatService(CometSharedMessageQueue cometSharedMessageQueue) {
        this.cometSharedMessageQueue = cometSharedMessageQueue;
    }

    public void log(String chat) {
        cometSharedMessageQueue.addMessage(new CometMessage(MessageTarget.CHAT, buildChatMessage(chat)));
        blockWriteHackForMicroShitWinblows();

        chatLogger.info(chat);
    }

    //On MicroShit Winblows reading the log file locks the file and prevents log roller rename and crashes the logger
    private void blockWriteHackForMicroShitWinblows() {
        int maxWait = 50;
        while (readingLog.get() > 0 && maxWait-- > 0) {
            SafeUtil.safeSleep(5);
        }
    }

    private ChatMessage buildChatMessage(String chat) {
        return new ChatMessage(sdf.format(new Date()) + chat);
    }

    public void setMaxLogDays(int maxDays) {
        RollingFileAppender<ILoggingEvent> appender = getLogAppender();
        TimeBasedRollingPolicy<ILoggingEvent> policy = (TimeBasedRollingPolicy<ILoggingEvent>) appender.getRollingPolicy();
        policy.setMaxHistory(maxDays);
    }

    private RollingFileAppender<ILoggingEvent> getLogAppender() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        final ch.qos.logback.classic.Logger logger = loggerContext.getLogger(ChatService.class);
        return (RollingFileAppender<ILoggingEvent>) logger.getAppender(LogConfiguration.CHAT_APPENDER);
    }

    public List<ChatMessage> readMessages() throws IOException {
        return readFile(getLogAppender().getFile());
    }
    public List<ChatMessage> readMessages(String date) throws IOException {
        return readFile("chat_"+ date +".log");
    }
    private List<ChatMessage> readFile(String file) throws IOException {
        if (file.equals(getLogAppender().getFile())) {
            readingLog.incrementAndGet();
        }
        List<ChatMessage> lines = Lists.newArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(new ChatMessage(line));
            }
        } finally {
            readingLog.decrementAndGet();
        }
        return lines;
    }

    public String[] getDays() {
        File pwd = new File(".");
        String[] files = pwd.list((dir, name) -> name.startsWith("chat_") && name.endsWith(".log"));
        for (int i = 0; i < files.length; i++) {
            files[i] = files[i].substring(5, 15);
        }
        return files;
    }

}
