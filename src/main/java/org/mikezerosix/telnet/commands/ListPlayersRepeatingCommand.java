package org.mikezerosix.telnet.commands;

import org.mikezerosix.entities.Player;
import org.mikezerosix.service.PlayerService;
import org.mikezerosix.util.TelnetLineUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
lp
1. id=171, Mike06, pos=(-510.1, 3.0, -268.1), rot=(0.0, 64.7, 0.0), remote=True, health=100, 76561198011842231
Total of 1 in the game

 */
public class ListPlayersRepeatingCommand extends RepeatingCommand {
    public static final String LP = "lp";
    private final Pattern pattern = Pattern.compile(TelnetLineUtil.TIME_STAMP
            + "(\\d+). id=(\\d+), (.?), pos=\\((\\d+), (\\d+), (\\d+)\\), rot=\\(.?\\), remote=True, health=(\\d+), (\\d+)");

    private final Pattern finishedPattern = Pattern.compile("\n" +
            "Total of (\\d+) in the game");

    private boolean finished = false;
    private PlayerService playerService;

    public ListPlayersRepeatingCommand(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public boolean isFinished() {
        return finished || (nextRun < System.currentTimeMillis());
    }

    @Override
    public String getCommand() {
        return LP;
    }

    @Override
    public Matcher[] matcher(String line) {
        return new Matcher[] {pattern.matcher(line), finishedPattern.matcher(line)};
    }

    @Override
    public void handleInput(String input) {
        final Matcher[] matchers = matcher(input);
        if (matchers[0].find()) {
            final long entityId = Long.parseLong(matchers[0].group(2).trim());
            Player player = playerService.getPlayerByEntityId(entityId);
            if (player == null) {
                player = new Player();
                player.setEntityId(entityId);
            }
            player.setEntityId(entityId);
            player.setName(matchers[0].group(3).trim());
            player.setX(Long.parseLong(matchers[0].group(4).trim()));
            player.setZ(Long.parseLong(matchers[0].group(5).trim()));
            player.setY(Long.parseLong(matchers[0].group(6).trim()));
            player.setHealth(Integer.parseInt(matchers[0].group(7).trim()));
            player.setSteamId(matchers[0].group(8));
            playerService.save(player);
        }
        if (matchers[1].matches()) {
            finished = true;
        }
    }
}
