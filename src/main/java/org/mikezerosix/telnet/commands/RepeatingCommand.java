package org.mikezerosix.telnet.commands;


import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;


public abstract class RepeatingCommand implements TelnetCommand {
    protected static long delay = 1000 * 60;
    protected static long nextRun = nextRun = System.currentTimeMillis() + 1000;

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(nextRun - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (this.nextRun < ((RepeatingCommand) o).nextRun) {
            return -1;
        }
        if (this.nextRun > ((RepeatingCommand) o).nextRun) {
            return 1;
        }
        return 0;
    }

    @Override
    public void resetCoolDown() {
        nextRun = System.currentTimeMillis() + delay;
    }
}

