package org.mikezerosix.service;


import org.mikezerosix.comet.CometMessage;
import org.mikezerosix.comet.CometSharedMessageQueue;
import org.mikezerosix.comet.MessageTarget;
import org.mikezerosix.entities.Player;
import org.mikezerosix.entities.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class PlayerService {
    Logger log = LoggerFactory.getLogger(PlayerService.class);
    private CometSharedMessageQueue cometSharedMessageQueue;
    private PlayerRepository playerRepository;

    public PlayerService(CometSharedMessageQueue cometSharedMessageQueue, PlayerRepository playerRepository) {
        this.cometSharedMessageQueue = cometSharedMessageQueue;
        this.playerRepository = playerRepository;

    }

    public void login(long entityId, long clientId, String name) {
        Player player =  getPlayerByEntityId(entityId,true);
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
        playerRepository.save(player);
        cometSharedMessageQueue.addMessage(new CometMessage(MessageTarget.PLAYER, player));
    }

    public void logout(Player player) {
        if (player != null) {
            log.info(String.format(" Logging out Player %s  ", player.getName()));
            player.setOnline(false);
            player.setLastSync(new Date());
            playerRepository.save(player);
            cometSharedMessageQueue.addMessage(new CometMessage(MessageTarget.PLAYER, player));
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
            player = buildNewPlayer(entityId);
        }
        return player;
    }

    public void save(Player player) {
        playerRepository.save(player);
    }

    public void logoutStale(Date date) {
        for (Player player : playerRepository.findByOnlineAndLastSyncLessThan(true,date)) {
            logout(player);
        }
    }
}
