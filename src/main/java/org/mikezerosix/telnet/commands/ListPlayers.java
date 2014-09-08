package org.mikezerosix.telnet.commands;

import org.mikezerosix.entities.Player;
import org.mikezerosix.telnet.handlers.TelnetOutputHandler;
import org.mikezerosix.util.TelentLineUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
lp
1. id=171, Mike06, pos=(-510.1, 3.0, -268.1), rot=(0.0, 64.7, 0.0), remote=True, health=100, 76561198011842231
Total of 1 in the game

 */
public class ListPlayers implements TelnetCommand {
    private final Pattern pattern = Pattern.compile(TelentLineUtil.TIME_STAMP
            + "(\\d+). id=(\\d+), (.?), pos=\\((.?)\\) ");

    private final Pattern finishedPattern = Pattern.compile("\n" +
            "Total of (\\d+) in the game");

    private boolean finished = false;

    private int count;

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public String getCommand() {
        return "lp";
    }

    @Override
    public Matcher[] matcher(String line) {
        return new Matcher[] {pattern.matcher(line)};
    }

    @Override
    public void handleInput(String input) {
        final Matcher matcher = matcher(input)[0];
        if (matcher.find()) {
            Player player = new Player();
            final String id = matcher.group(2).trim();
            final String name = matcher.group(3).trim();
            final String pos = matcher.group(4).trim();
            //playerRepository.nativeTelnetPlayerSave()
            count++;
        }
        if (pattern.matcher(input).find()) {
            final String count = matcher.group(1).trim();
            finished = true;
        }
    }
}
