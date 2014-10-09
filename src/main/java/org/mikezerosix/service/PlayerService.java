package org.mikezerosix.service;


import org.mikezerosix.comet.CometMessage;
import org.mikezerosix.comet.CometSharedMessageQueue;
import org.mikezerosix.comet.MessageTarget;
import org.mikezerosix.entities.Player;
import org.mikezerosix.entities.PlayerRepository;
import org.mikezerosix.telnet.TelnetRunner;

import java.util.Date;

public class PlayerService {
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
        player.setOnline(false);
        player.setLastSync(new Date());
        player.setLastLogin(new Date());
        player.setClientId(clientId);
        player.setEntityId(entityId);
        player.setOnline(true);
        player.setName(name);
        playerRepository.save(player);
        cometSharedMessageQueue.addMessage(new CometMessage(MessageTarget.PLAYER, player));
        //TODO: run lp command
    }

    public void logout(long entityId) {
        Player player = playerRepository.findByEntityId(entityId);
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
