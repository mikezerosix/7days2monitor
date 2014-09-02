package org.mikezerosix.handlers;

/** Performs action at given interval. run ins called when added to handler chain.
 *
 */
public interface ActiveHander {

    public void setInterval(long seconds);
    public void run();
}
