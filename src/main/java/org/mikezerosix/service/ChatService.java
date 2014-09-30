package org.mikezerosix.service;


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


}
