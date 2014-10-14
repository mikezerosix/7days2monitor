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



allocs
-----------------------------------------
401404.200 Telnet executed "lpe" from: 77.64.181.245:50487
1. id=2614842, Wotan, pos=(1039.8, 64.3, 141.2), rot=(-32.3, -174.4, 0.0), remote=True, health=62, deaths=32, zombies=112, players=20, score=76, steamid=76561198078460866, ip=178.25.205.26, ping=24
2. id=2164235, ? Virus ?, pos=(1650.6, 73.0, -654.7), rot=(-37.8, 88.5, 0.0), remote=True, health=49, deaths=24, zombies=128, players=5, score=31, steamid=76561197989613976, ip=78.218.160.58, ping=54
3. id=377505, Greenstreet, pos=(-6598.7, 73.0, -4618.8), rot=(12.7, -33.8, 0.0), remote=True, health=72, deaths=16, zombies=936, players=0, score=856, steamid=76561197991440145, ip=134.3.41.138, ping=19
4. id=2472930, ex.pmw, pos=(-84.9, 90.0, -147.8), rot=(-60.1, -104.0, 0.0), remote=True, health=70, deaths=39, zombies=105, players=6, score=1, steamid=76561198034403785, ip=146.60.24.158, ping=29
5. id=2715174, DAF, pos=(-137.2, 66.0, -251.1), rot=(-8.5, 95.0, 0.0), remote=True, health=70, deaths=2, zombies=1, players=1, score=0, steamid=76561197960496332, ip=77.57.50.79, ping=28
6. id=119038, BoBMulligan72, pos=(859.7, 68.9, 822.1), rot=(-10.7, -5.5, 0.0), remote=True, health=35, deaths=62, zombies=1135, players=125, score=825, steamid=76561198104922026, ip=95.91.204.81, ping=29
Total of 6 in the game
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
