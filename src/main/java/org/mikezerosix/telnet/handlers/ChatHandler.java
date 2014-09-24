package org.mikezerosix.telnet.handlers;

import org.mikezerosix.actions.ChatLogger;
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
    public static final String GMSG = TelnetLineUtil.TIME_STAMP + "GMSG:\\s(.*)";
    private final Pattern pattern = Pattern.compile(GMSG);

    private final ChatLogger chatLogger;

    public ChatHandler(ChatLogger chatLogger) {
        this.chatLogger = chatLogger;
    }

    @Override
    public Matcher[] matcher(String line) {
        return new Matcher[]{pattern.matcher(line)};
    }

    @Override
    public void handleInput(String input) {
        final Matcher matcher = matcher(input)[0];
        if (matcher.find()) {
            final String chat = matcher.group(1).trim();
            chatLogger.log(chat);
        }
    }
}
