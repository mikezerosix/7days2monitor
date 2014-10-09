package org.mikezerosix.telnet.commands;

import org.mikezerosix.telnet.handlers.TelnetOutputHandler;

import java.io.PrintStream;
import java.util.concurrent.Delayed;

public interface TelnetCommand extends TelnetOutputHandler, Delayed {
    public void runCommand(PrintStream stream);
    public boolean isFinished();
}
