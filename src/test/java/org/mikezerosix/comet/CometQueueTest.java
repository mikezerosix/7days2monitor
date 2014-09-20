package org.mikezerosix.comet;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.junit.Test;

import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class CometQueueTest {

    LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue<Double>(10);
    Cache<Long, String> cache = CacheBuilder.newBuilder()
            .maximumSize(10).concurrencyLevel(1)
            .expireAfterWrite(2, TimeUnit.SECONDS).build();

    Consumer consumer1 = new Consumer("one", cache);
    Consumer consumer2 = new Consumer("two", cache);
    Consumer consumer3 = new Consumer("three", cache);

    @Test
    public void testExceedLimit() throws Exception {
        for (int i = 0; i < 20; i++) {
            cache.put((long) i, Integer.toString(i));
        }
        consumer1.run();
        consumer2.run();
        consumer3.run();
        System.out.println(cache.size());
    }

    class Consumer implements Runnable {

        private String name;
        private Cache<Long, String> cache;

        Consumer(String name, Cache<Long, String> cache) {
            this.name = name;
            this.cache = cache;
        }

        @Override
        public void run() {
            TreeMap<Long, String> sorted = new TreeMap<>(cache.asMap());
            sorted.tailMap(15L).forEach((k, v) -> System.out.println(name + " " + k + ":" + v));
        }
    }
}