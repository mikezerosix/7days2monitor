package org.mikezerosix.rest;

import com.google.common.collect.Lists;
import org.mikezerosix.comet.CometMessage;
import org.mikezerosix.comet.CometSharedMessageQueue;
import org.mikezerosix.rest.transformers.JsonTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;

import static org.mikezerosix.service.SettingsService.PROTECTED_URL;
import static org.mikezerosix.util.SafeUtil.safeSleep;
import static org.mikezerosix.util.SafeUtil.safeParseLong;
import static spark.Spark.get;

public class CometResource {
    private static final Logger log = LoggerFactory.getLogger(CometResource.class);
    private CometSharedMessageQueue queue;
    public static final int WAIT = 500;
    public static final int TIMEOUT = 20000;

    public CometResource(CometSharedMessageQueue cometSharedMessageQueue) {
        this.queue = cometSharedMessageQueue;
    }

    public void registerRoutes() {

        get(PROTECTED_URL + "/comet/:timestamp", (request, response) -> {
            List<CometMessage> messages = Lists.newArrayList();
            final long end = TIMEOUT + System.currentTimeMillis();
            while (System.currentTimeMillis() < end) {
                SortedMap<Long, CometMessage> queuedMessages = queue.getQueuedMessages(safeParseLong(request.params(":timestamp")));
                if (queuedMessages.isEmpty()) {
                    safeSleep(WAIT);
                } else {
                    messages.addAll(queuedMessages.values());
                    break;
                }
            }
            return messages;
        }, new JsonTransformer());
    }



}
