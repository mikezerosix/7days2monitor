package org.mikezerosix.service;


import org.mikezerosix.comet.CometMessage;
import org.mikezerosix.comet.CometSharedMessageQueue;
import org.mikezerosix.comet.MessageTarget;
import org.mikezerosix.entities.Player;
import org.mikezerosix.entities.PlayerPosition;
import org.mikezerosix.entities.PlayerPositionRepository;
import org.mikezerosix.entities.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class PlayerService {
    private  static  final Logger log = LoggerFactory.getLogger(PlayerService.class);
    private CometSharedMessageQueue cometSharedMessageQueue;
    private PlayerRepository playerRepository;
    private PlayerPositionRepository playerPositionRepository;
    private long playerDays;
    private int tick;

    public PlayerService(CometSharedMessageQueue cometSharedMessageQueue, PlayerRepository playerRepository, PlayerPositionRepository PlayerPositionRepository) {
        this.cometSharedMessageQueue = cometSharedMessageQueue;
        this.playerRepository = playerRepository;
        playerPositionRepository = PlayerPositionRepository;
    }

    public void login(long entityId, long clientId, String name) {
        Player player = getPlayerByEntityId(entityId, true);
        player.setClientId(clientId);
        player.setEntityId(entityId);
        player.setName(name);

        login(player);
    }

    private Player buildNewPlayer(long entityId) {
        Player player;
        player = new Player();
        player.setJoined(new Date());
        if (player.getSteamId() == null) {
            player.setSteamId("xxx-NOT-AN-ID-xxx" + entityId); // derby non nullable uniq hack
        }
        return player;
    }

    public void login(Player player) {
        player.setOnline(true);
        player.setLastSync(new Date());
        player.setLastLogin(new Date());
        player.setOnline(true);
        log.info(String.format("Player(%s) logged in: id=%d, entityId=%d, clientId=%d, steamId=%s", player.getName(), player.getId(), player.getEntityId(), player.getClientId(), player.getSteamId()));
        save(player);
    }

    public void logout(Player player) {
        if (player != null) {
            log.info(String.format(" Logging out Player %s  ", player.getName()));
            player.setOnline(false);
            player.setLastSync(new Date());
            save(player);
            return;
        }
        log.info(String.format(" Logout called for  non-existing player."));
    }

    public void logout(long entityId) {
        logout(getPlayerByEntityId(entityId, false));
    }

    public Player getPlayerByEntityId(long entityId, boolean build) {
        Player player = playerRepository.findByEntityId(entityId);
        if (player == null && build) {
            log.info("Building a new player :" + entityId);
            player = buildNewPlayer(entityId);
        }
        return player;
    }

    public void save(Player player) {
        log.info("saving player :" + player.getName());
        Player save = playerRepository.save(player);
        savePosition(save);
        cometSharedMessageQueue.addMessage(new CometMessage(MessageTarget.PLAYER, save));

    }

    private void savePosition(Player player) {
        if (player.getId() != null && player.getX() != null && player.getY() != null) {
            PlayerPosition playerPosition = new PlayerPosition();
            playerPosition.setPlayerId(player.getId());
            playerPosition.setX(player.getX());
            playerPosition.setY(player.getY());
            playerPosition.setZ(player.getZ());
            playerPositionRepository.save(playerPosition);
            runCleanUp();
        }
    }

    public void logoutStale(Date date) {
        playerRepository.findByOnlineAndLastSyncLessThan(true, date).forEach(this::logout);
    }

    private void runCleanUp() {
        if (playerDays > 0) {
            if (--tick < 1) {
                tick = 120;
                playerPositionRepository.cleanup(System.currentTimeMillis() - (playerDays * 60 * 60 * 1000) );
                log.info(String.format("pruning path db older than %d days", playerDays));
            }
        }
    }

    public void setPlayerDays(long playerDays) {
        this.playerDays = playerDays;
    }
}
