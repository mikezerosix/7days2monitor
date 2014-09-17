package org.mikezerosix.rest;

import com.google.common.collect.Lists;
import org.mikezerosix.entities.Stat;
import org.mikezerosix.entities.StatRepository;
import org.mikezerosix.rest.transformers.JsonTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mikezerosix.AppConfiguration.PROTECTED_URL;
import static spark.Spark.get;

public class StatResource {
    private static final Logger log = LoggerFactory.getLogger(StatResource.class);
    private final StatRepository statRepository;

    public StatResource(StatRepository statRepository) {
        this.statRepository = statRepository;
        get(PROTECTED_URL + "stats/days/:days", (request, response) ->
                Lists.newArrayList(statRepository.findAll())
                , new JsonTransformer());

        get(PROTECTED_URL + "stats", (request, response) ->
                new StatReport(statRepository.getLastStat(), statRepository.getMaxStat())
                , new JsonTransformer());

    }

    private class StatReport {
        public Stat current;
        public Object[] max;
        public long ts = System.currentTimeMillis();

        public StatReport(Stat current, Object[] max) {
            this.current = current;
            this.max = max;
        }
    }

}
