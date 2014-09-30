package org.mikezerosix.service;

import org.mikezerosix.comet.CometMessage;
import org.mikezerosix.comet.CometSharedMessageQueue;
import org.mikezerosix.comet.MessageTarget;
import org.mikezerosix.entities.Stat;
import org.mikezerosix.entities.StatRepository;
import org.mikezerosix.rest.data.StatReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StatService {
    public static final Logger log = LoggerFactory.getLogger(StatService.class);
    public static final long DAY = 24 * 60 * 60 * 1000;
    private StatRepository statRepository;
    private CometSharedMessageQueue cometSharedMessageQueue;
    private long statDays = 0;
    private int tick = 0;

    public StatService(StatRepository statRepository, CometSharedMessageQueue cometSharedMessageQueue) {
        this.statRepository = statRepository;
        this.cometSharedMessageQueue = cometSharedMessageQueue;
    }

    public void record(Stat stat) {
        statRepository.save(stat);
        if (statDays > 0) {
            if (--tick < 1) {
                tick = 120;
                statRepository.cleanup(System.currentTimeMillis() - statDays);
                log.info("pruning stat db");
            }
        }
        Object[] maxStat = statRepository.getMaxStat();
        CometMessage statReport = new CometMessage(MessageTarget.STAT, new StatReport(stat, maxStat));
        cometSharedMessageQueue.addMessage(statReport);
    }

    public void setStatDays(long statDays) {
        this.statDays = statDays * DAY;
    }
}
