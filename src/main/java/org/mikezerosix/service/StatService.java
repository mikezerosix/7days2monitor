package org.mikezerosix.service;

import org.mikezerosix.comet.CometSharedMessageQueue;
import org.mikezerosix.entities.Stat;
import org.mikezerosix.entities.StatRepository;


public class StatService {

    private StatRepository statRepository;

    private CometSharedMessageQueue cometSharedMessageQueue;

    public StatService(StatRepository statRepository, CometSharedMessageQueue cometSharedMessageQueue) {
        this.statRepository = statRepository;
        this.cometSharedMessageQueue = cometSharedMessageQueue;
    }

    public void record(Stat stat) {
        statRepository.save(stat);

    }
}
