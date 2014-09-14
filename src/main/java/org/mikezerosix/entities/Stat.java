package org.mikezerosix.entities;

import javax.persistence.Entity;
import javax.persistence.Id;


public class Stat {
    @Id
    long id;

    long uptime;

    byte players;

    int zombies;
    int entities;
    int items;

    double memHeap;
    double memMax;

    int chunks;
    int gco;
    int fps;


}
