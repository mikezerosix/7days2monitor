package org.mikezerosix.actions;


import ch.qos.logback.classic.Level;
import org.mikezerosix.comet.CometMessage;
import org.mikezerosix.comet.CometSharedMessageQueue;
import org.mikezerosix.comet.MessageTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatLogger {
    public static final Logger chatLogger = LoggerFactory.getLogger(ChatLogger.class);
    private CometSharedMessageQueue cometSharedMessageQueue;

    public ChatLogger(CometSharedMessageQueue cometSharedMessageQueue) {
        this.cometSharedMessageQueue = cometSharedMessageQueue;
    }

    public void log(String chat) {
        cometSharedMessageQueue.addMessage(new CometMessage(MessageTarget.CHAT, null, chat));
        chatLogger.info(chat);
    }
}
