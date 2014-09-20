package org.mikezerosix.service;

import org.mikezerosix.comet.CometMessage;
import org.mikezerosix.comet.CometSharedMessageQueue;
import org.mikezerosix.comet.MessageTarget;
import org.mikezerosix.entities.Stat;
import org.mikezerosix.entities.StatRepository;
import org.mikezerosix.rest.data.StatReport;


public class StatService {

    private StatRepository statRepository;

    private CometSharedMessageQueue cometSharedMessageQueue;

    public StatService(StatRepository statRepository, CometSharedMessageQueue cometSharedMessageQueue) {
        this.statRepository = statRepository;
        this.cometSharedMessageQueue = cometSharedMessageQueue;
    }

    public void record(Stat stat) {
        statRepository.save(stat);
        Object[] maxStat = statRepository.getMaxStat();
        CometMessage statReport = new CometMessage(MessageTarget.STAT, "StatReport", new StatReport(stat, maxStat));
        cometSharedMessageQueue.addMessage(statReport);
    }
}
