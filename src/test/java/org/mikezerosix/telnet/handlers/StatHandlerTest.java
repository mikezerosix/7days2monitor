package org.mikezerosix.telnet.handlers;

import org.junit.Test;

import static org.junit.Assert.*;

public class StatHandlerTest {
    private StatHandler statHandler = new StatHandler();
    @Test
    public void testMatcher() throws Exception {
        assertTrue(statHandler.matcher(
                "167628.000 STATS: 2787.16,12.06,1589.5,1941.4,2504,170,8,63,87,425,162")[0].matches());
    }

    @Test
    public void testHandleInput() throws Exception {

    }
}