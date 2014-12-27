package org.mikezerosix.monitors.action;

import org.mikezerosix.entities.Player;

/**
 * Created by michael on 19.10.2014.
 */
public interface PlayerMonitor extends Monitor {


    public void trigger(Player player);
}
