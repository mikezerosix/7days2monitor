package org.mikezerosix.monitors.events;

import org.mikezerosix.entities.Player;
import org.mikezerosix.telnet.TelnetRunner;

public class PlayerLoginEvent {
    private final TelnetRunner telnetRunner;
    private final Player player;

    public PlayerLoginEvent(TelnetRunner telnetRunner, Player player){
        this.telnetRunner = telnetRunner;
        this.player = player;
    }
}
