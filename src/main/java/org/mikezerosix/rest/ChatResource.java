package org.mikezerosix.rest;


import com.google.common.collect.Lists;
import org.mikezerosix.rest.data.ChatMessage;
import org.mikezerosix.rest.transformers.JsonTransformer;
import org.mikezerosix.telnet.TelnetRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static org.mikezerosix.service.SettingsService.PROTECTED_URL;
import static spark.Spark.get;
import static spark.Spark.post;

public class ChatResource {
    private static final Logger log = LoggerFactory.getLogger(ChatResource.class);
    private TelnetRunner telnetRunner;

    public ChatResource(TelnetRunner telnetRunner) {
        this.telnetRunner = telnetRunner;
    }

    public void registerRoutes() {
        get(PROTECTED_URL + "chat", (request, response) -> {
            try {
                return readMessages("chat.log");
            } catch (IOException e) {
                response.status(500);
                log.error("IO exception on reading chat", e);
                return e;
            }
        }, new JsonTransformer());

        get(PROTECTED_URL + "chat/:day", (request, response) -> {
            try {
                return readMessages("chat_" + request.params("day") + ".log");
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

    private List<ChatMessage> readMessages(String file) throws IOException {
        List<ChatMessage> lines = Lists.newArrayList();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(new ChatMessage(line));
        }
        return lines;
    }


}
