package org.mikezerosix.telnet.commands;

import org.mikezerosix.telnet.handlers.TelnetOutputHandler;

public interface TelnetCommand extends TelnetOutputHandler {
    public boolean isFinished();
    public String getCommand();

}
