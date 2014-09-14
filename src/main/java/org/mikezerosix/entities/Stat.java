package org.mikezerosix.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Stat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    long uptime;
    int players;
    int zombies;
    int entities;
    int entities2;
    int items;
    double memHeap;
    double memMax;
    int chunks;
    int gco;
    double fps;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUptime() {
        return uptime;
    }

    public void setUptime(long uptime) {
        this.uptime = uptime;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public int getZombies() {
        return zombies;
    }

    public void setZombies(int zombies) {
        this.zombies = zombies;
    }

    public int getEntities() {
        return entities;
    }

    public void setEntities(int entities) {
        this.entities = entities;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public double getMemHeap() {
        return memHeap;
    }

    public void setMemHeap(double memHeap) {
        this.memHeap = memHeap;
    }

    public double getMemMax() {
        return memMax;
    }

    public void setMemMax(double memMax) {
        this.memMax = memMax;
    }

    public int getChunks() {
        return chunks;
    }

    public void setChunks(int chunks) {
        this.chunks = chunks;
    }

    public int getGco() {
        return gco;
    }

    public void setGco(int gco) {
        this.gco = gco;
    }

    public double getFps() {
        return fps;
    }

    public void setFps(double fps) {
        this.fps = fps;
    }

    public int getEntities2() {
        return entities2;
    }

    public void setEntities2(int entities2) {
        this.entities2 = entities2;
    }
}
