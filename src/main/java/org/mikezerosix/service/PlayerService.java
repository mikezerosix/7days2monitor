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
        Player player = playerRepository.findByEntityId(entityId);
        if (player == null) {
            player = new Player();
        }
        player.setOnline(true);
        player.setLastSync(new Date());
        player.setLastLogin(new Date());
        player.setClientId(clientId);
        player.setEntityId(entityId);
        player.setSteamId("" + entityId); // derby non nullable uniq hack
        player.setOnline(true);
        player.setName(name);
        log.info(String.format("Saving player(%s) login: id=%d, entityId=%d, clientId=%d, steamId=%s", name, player.getId(), entityId, clientId, player.getSteamId()));
        playerRepository.save(player);
        cometSharedMessageQueue.addMessage(new CometMessage(MessageTarget.PLAYER, player));
        //TODO: run lp command
    }

    public void logout(long entityId) {
        Player player = playerRepository.findByEntityId(entityId);
        log.info(String.format("Player :%s logging out exists=%s logged out", entityId, player != null));
        if (player != null) {
            player.setOnline(false);
            playerRepository.save(player);
            cometSharedMessageQueue.addMessage(new CometMessage(MessageTarget.PLAYER, player));
        }
        //TODO: run lp command
    }

    public Player getPlayerByEntityId(long entityId) {
        return playerRepository.findByEntityId(entityId);
    }

    public void save(Player player) {
        // notify if ....
        playerRepository.save(player);
    }
}
