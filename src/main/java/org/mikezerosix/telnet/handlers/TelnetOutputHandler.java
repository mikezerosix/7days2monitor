package org.mikezerosix.telnet.handlers;

import java.util.regex.Matcher;

public interface TelnetOutputHandler {
    public Matcher[] matcher(String line);
    public void handleInput(String input);
}
