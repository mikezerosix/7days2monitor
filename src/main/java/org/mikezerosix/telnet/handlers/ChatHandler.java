package org.mikezerosix.telnet.handlers;

import org.mikezerosix.service.ChatService;
import org.mikezerosix.util.TelnetLineUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
167612.300 GMSG: ShoC: they know where I am... eeek
*/
public class ChatHandler implements TelnetOutputHandler {
    private static final Logger log = LoggerFactory.getLogger(ChatHandler.class);
    public static final String GMSG = TelnetLineUtil.TIME_STAMP + "GMSG:\\s(.*?):\\s(.*)";
    private final Pattern pattern = Pattern.compile(GMSG);

    private final ChatService chatService;

    public ChatHandler(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public Matcher[] matcher(String line) {
        return new Matcher[]{pattern.matcher(line)};
    }

    @Override
    public void handleInput(String input) {
        final Matcher matcher = matcher(input)[0];
        if (matcher.find()) {
            log.debug("chat message: " + input);
            final String chat = matcher.group(1).trim().replace(' ', '_')+ " " +matcher.group(2).trim();
            chatService.log(chat);
        }
    }
}
