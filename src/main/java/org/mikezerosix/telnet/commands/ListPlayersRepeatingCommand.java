package org.mikezerosix.telnet.commands;

import org.mikezerosix.entities.Player;
import org.mikezerosix.entities.PlayerPosition;
import org.mikezerosix.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
lp
1. id=171, Mike06, pos=(-510.1, 3.0, -268.1), rot=(0.0, 64.7, 0.0), remote=True, health=100, 76561198011842231
Total of 1 in the game

 */
public class ListPlayersRepeatingCommand extends RepeatingCommand {
    private static final Logger log = LoggerFactory.getLogger(ListPlayersRepeatingCommand.class);
    public static final String LP = "lp";
    private static final Pattern pattern = Pattern.compile("(\\d+)\\. id=(\\d+), (.+), pos=\\((-?\\d+\\.\\d), (-?\\d+\\.\\d), (-?\\d+\\.\\d)\\), rot=\\(.*\\), remote=True, health=(\\d+), (\\d+)");
    private static final Pattern finishedPattern = Pattern.compile("Total of (\\d+) in the game");


    private PlayerService playerService;

    public ListPlayersRepeatingCommand(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public boolean isFinished() {
        if (finished) {
            return true;
        }
        if (nextRun < System.currentTimeMillis()) {
            log.warn(" command handler timed out ");
            return true;
        }
        return false;
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
            Player player = playerService.getPlayerByEntityId(entityId, true);
            player.setEntityId(entityId);
            player.setName(matchers[0].group(3).trim());
            player.setX(Double.parseDouble(matchers[0].group(4).trim()));
            player.setZ(Double.parseDouble(matchers[0].group(5).trim()));
            player.setY(Double.parseDouble(matchers[0].group(6).trim()));
            player.setHealth(Integer.parseInt(matchers[0].group(7).trim()));
            player.setSteamId(matchers[0].group(8));
            player.setLastSync(new Date(nextRun - delay));
            if (!player.isOnline()){
                playerService.login(player);
            } else {
                playerService.save(player);
            }
        }
        if (matchers[1].matches()) {
            playerService.logoutStale(new Date(nextRun- delay));
            finished = true;
        }
    }

    public void setPlayerDays(long playerDays) {
        playerService.setPlayerDays(playerDays);
    }
}
