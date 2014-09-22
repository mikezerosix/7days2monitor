package org.mikezerosix.rest;

import org.mikezerosix.comet.CometMessage;
import org.mikezerosix.comet.CometSharedMessageQueue;
import org.mikezerosix.rest.transformers.JsonTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import static org.mikezerosix.service.SettingsService.PROTECTED_URL;
import static spark.Spark.get;

public class CometResource {
    private static final Logger log = LoggerFactory.getLogger(CometResource.class);
    private CometSharedMessageQueue cometSharedMessageQueue;

    public CometResource(CometSharedMessageQueue cometSharedMessageQueue) {
        this.cometSharedMessageQueue = cometSharedMessageQueue;
    }

    public void registerRoutes() {

        get(PROTECTED_URL + "/comet/:timestamp", (request, response) -> {
            long timestamp = getTimestamp(request.params(":timestamp"));
            SortedMap<Long, CometMessage> queuedMessages = cometSharedMessageQueue.getQueuedMessages(timestamp);
            List<CometMessage> messages = new ArrayList<>(queuedMessages.values());
            return messages;
        } , new JsonTransformer());

    }

    private long getTimestamp(String timestamp) {
        try {
            return Long.parseLong(timestamp);
        } catch (Exception e) {
            log.warn("bad timestamp({}) in comet request, assuming 0", timestamp);
            return 0L;
        }
    }
}