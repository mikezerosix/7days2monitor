package org.mikezerosix.handlers;

import org.mikezerosix.actions.ChatLogger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
167612.300 GMSG: ShoC: they know where I am... eeek
*/
public class ChatHandler implements TelnetOutputHandler {
    public static final String GMSG = "\\d{6,}+\\.\\d{3}\\sGMSG:\\s(.*)";
    private final Pattern pattern = Pattern.compile(GMSG);

    private ChatLogger chatLogger = new ChatLogger();

    @Override
    public boolean match(String line) {
        return   pattern.matcher(line).matches();
    }

    @Override
    public void handleInput(String input) {
        final Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            final String chat = matcher.group(1).trim();
            chatLogger.log(chat);
        }
    }
}
