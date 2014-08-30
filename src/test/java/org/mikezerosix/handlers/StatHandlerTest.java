package org.mikezerosix.handlers;

import junit.framework.TestCase;

public class StatHandlerTest extends TestCase {

    public void testMatch() throws Exception {
        assertTrue("167628.000 STATS: 2787.16,12.06,1589.5,1941.4,2504,170,8,63,87,425,162".matches(StatHandler.STAT_TRIGGER));
        assertFalse("167628.000 asjdfhsakjdhaskjdhaskjdh".matches(StatHandler.STAT_TRIGGER));
    }
}