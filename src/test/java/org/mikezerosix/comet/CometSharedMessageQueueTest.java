package org.mikezerosix.comet;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.SortedMap;

import static org.hamcrest.MatcherAssert.assertThat;

public class CometSharedMessageQueueTest {

    CometSharedMessageQueue q = new CometSharedMessageQueue();


    @Test
    public void testMessage() throws Exception {
        q.addMessage(new CometMessage(MessageTarget.STAT, "1", "one"));
        Thread.sleep(1);
        q.addMessage(new CometMessage(MessageTarget.STAT, "2", "two"));
        Thread.sleep(1);
        q.addMessage(new CometMessage(MessageTarget.STAT, "3", "three"));
        SortedMap<Long, CometMessage> queuedMessages = q.getQueuedMessages(0);
        assertThat(queuedMessages.size(), CoreMatchers.is(3));
        SortedMap<Long, CometMessage> queuedMessages2 = q.getQueuedMessages(queuedMessages.firstKey());
        assertThat(queuedMessages2.size(), CoreMatchers.is(2));

    }
}