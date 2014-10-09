package org.mikezerosix.telnet.commands;


import java.io.PrintStream;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;


public abstract class ManualCommand implements TelnetCommand  {

    private long nextRun = System.currentTimeMillis();

    @Override
    public long getDelay(TimeUnit unit) {
        return 0;
    }

    @Override
    public int compareTo(Delayed o) {
        if (this.nextRun <  ((ManualCommand) o).nextRun) {
            return -1;
        }
        if (this.nextRun > ((ManualCommand) o).nextRun) {
            return 1;
        }
        return 0;
    }

    @Override
    public synchronized void runCommand(PrintStream stream) {
        nextRun = System.currentTimeMillis();
        stream.println(getCommand());
    }

    abstract String getCommand();

    @Override
    public boolean isFinished() {
        return true;
    }
}
