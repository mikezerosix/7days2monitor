package org.mikezerosix.actions;


import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatLogger {
    public static final Logger chatLogger = LoggerFactory.getLogger(ChatLogger.class);

    public void log(String chat) {

        chatLogger.info(chat);
    }
}
