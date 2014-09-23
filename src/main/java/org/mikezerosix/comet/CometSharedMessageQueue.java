package org.mikezerosix.comet;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class CometSharedMessageQueue {
    private Cache<Long, CometMessage> cache = CacheBuilder.newBuilder()
            .maximumSize(500).concurrencyLevel(1)
            .expireAfterWrite(10, TimeUnit.SECONDS).build();

    public java.util.SortedMap<Long, CometMessage> getQueuedMessages(long lastRead) {
        TreeMap<Long, CometMessage> sorted = new TreeMap<>(cache.asMap());
        return sorted.tailMap(lastRead, false);
    }

    public void addMessage(CometMessage cometMessage) {
        cometMessage.setTimestamp(System.currentTimeMillis());
        cache.put(cometMessage.getTimestamp(), cometMessage);
    }

}