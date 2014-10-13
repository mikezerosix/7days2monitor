package org.mikezerosix.telnet.commands;

import org.mikezerosix.telnet.handlers.TelnetOutputHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.concurrent.Delayed;

public interface TelnetCommand extends TelnetOutputHandler, Delayed {
    public boolean isFinished();
    public String getCommand();
    public void reset();
}
