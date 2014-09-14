package org.mikezerosix.rest;

import com.google.common.collect.Lists;
import org.mikezerosix.entities.StatRepository;
import org.mikezerosix.rest.transformers.JsonTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mikezerosix.AppConfiguration.PROTECTED_URL;
import static spark.Spark.get;

public class StatResource {
    private static final Logger log = LoggerFactory.getLogger(StatResource.class);

    public StatResource(StatRepository statRepository) {

        get(PROTECTED_URL + "stats", (request, response) ->
                Lists.newArrayList(statRepository.findAll())
                , new JsonTransformer());

    }
}
