package org.mikezerosix.monitors.action;

import org.mikezerosix.entities.Player;
import org.mikezerosix.monitors.events.PlayerLoginEvent;
import org.mikezerosix.telnet.TelnetRunner;

import java.io.IOException;


public class Say implements Action {

    @Override
    public boolean accepts(Object o) {
        return o instanceof PlayerLoginEvent;
    }

    private String msg;

    public Say(String msg) {
        this.msg = msg;
    }


    public void trigger(PlayerLoginEvent param) {

    }

    public void sayAction (TelnetRunner telnetRunner, Player  player) throws IOException {
        telnetRunner.write(msg);
    }
}
