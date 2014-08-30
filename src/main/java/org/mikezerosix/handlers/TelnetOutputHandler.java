package org.mikezerosix.handlers;

public interface TelnetOutputHandler {
    public boolean match(String line);
    public void handleInput(String input);
}
