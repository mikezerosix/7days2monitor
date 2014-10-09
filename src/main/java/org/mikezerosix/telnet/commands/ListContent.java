package org.mikezerosix.telnet.commands;

import org.mikezerosix.util.TelnetLineUtil;

import java.io.PrintStream;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
le
1. id=88140, [type=EntityZombie, name=zombie05, id=88140], pos=(-520.6, 70.0, -83.1), rot=(0.0, 363.7, 0.0), lifetime=float.Max, remote=False, dead=False, health=100
2. id=171, [type=EntityPlayer, name=Mike06, id=171], pos=(-510.1, 3.0, -268.1), rot=(0.0, 64.7, 0.0), lifetime=float.Max, remote=True, dead=False, health=100
Total of 2 in the game

 */
public class ListContent implements TelnetCommand {
    private final Pattern pattern = Pattern.compile(TelnetLineUtil.TIME_STAMP
            + "(\\d+). id=(\\d+), [type=(.?), name=(.?), id=(\\d+)], pos=\\((.?)\\) ");

    private boolean finished = false;

    @Override
    public boolean isFinished() {
        finished = false;
        return finished;
    }

    @Override
    public Matcher[] matcher(String line) {
        return null;
    }

    @Override
    public void handleInput(String input) {

    }

    @Override
    public void runCommand(PrintStream stream) {

    }

    @Override
    public long getDelay(TimeUnit unit) {
        return 0;
    }

    @Override
    public int compareTo(Delayed o) {
        return 0;
    }
}
