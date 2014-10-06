package org.mikezerosix.rest;


import org.mikezerosix.rest.transformers.JsonTransformer;
import org.mikezerosix.service.ChatService;
import org.mikezerosix.telnet.TelnetRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.mikezerosix.service.SettingsService.PROTECTED_URL;
import static spark.Spark.get;
import static spark.Spark.post;

public class ChatResource {
    private static final Logger log = LoggerFactory.getLogger(ChatResource.class);
    private TelnetRunner telnetRunner;
    private ChatService chatService;

    public ChatResource(TelnetRunner telnetRunner, ChatService chatService) {
        this.telnetRunner = telnetRunner;
        this.chatService = chatService;
    }

    public void registerRoutes() {
        get(PROTECTED_URL + "chat", (request, response) -> {
            try {
                return chatService.readMessages();
            } catch (IOException e) {
                response.status(500);
                log.error("IO exception on reading chat", e);
                return e;
            }
        }, new JsonTransformer());

        get(PROTECTED_URL + "chat/:day", (request, response) -> {
            try {
                return chatService.readMessages(request.params("day"));
            } catch (IOException e) {
                response.status(500);
                log.error("IO exception on reading chat", e);
                return e;
            }
        }, new JsonTransformer());

        post(PROTECTED_URL + "chat", (request, response) -> {
            String say = "say " + request.body();
            try {
                telnetRunner.write(say);
                return true;
            } catch (IOException e) {
                log.error("failed to send command: " + say);
                response.status(500);
                return e;
            }
        });
    }


}
