package org.mikezerosix.comet;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.SortedMap;

import static org.hamcrest.MatcherAssert.assertThat;

public class CometSharedMessageQueueTest {

    CometSharedMessageQueue q = new CometSharedMessageQueue();


    @Test
    public void testMessage() throws Exception {
        q.addMessage(new CometMessage(MessageTarget.STAT, "one"));
        Thread.sleep(1);
        q.addMessage(new CometMessage(MessageTarget.STAT, "two"));
        Thread.sleep(1);
        q.addMessage(new CometMessage(MessageTarget.STAT, "three"));
        SortedMap<Long, CometMessage> queuedMessages = q.getQueuedMessages(0);
        assertThat(queuedMessages.size(), CoreMatchers.is(3));
        SortedMap<Long, CometMessage> queuedMessages2 = q.getQueuedMessages(queuedMessages.firstKey());
        assertThat(queuedMessages2.size(), CoreMatchers.is(2));

    }
}