package org.mikezerosix.comet;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.mikezerosix.util.SafeUtil;

import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class CometSharedMessageQueue {
    private AtomicLong index = new AtomicLong(1);
    private Cache<Long, CometMessage> cache = CacheBuilder.newBuilder()
            .maximumSize(500).concurrencyLevel(1)
            .expireAfterWrite(10, TimeUnit.SECONDS).build();

    public java.util.SortedMap<Long, CometMessage> getQueuedMessages(long lastRead) {
        TreeMap<Long, CometMessage> sorted = new TreeMap<>(cache.asMap());
        return sorted.tailMap(lastRead, false);
    }

    public void addMessage(CometMessage cometMessage) {
        cometMessage.setTimestamp(index.getAndIncrement());
        cache.put(cometMessage.getTimestamp(), cometMessage);
    }

}