package org.mikezerosix.handlers;

import org.junit.Test;
import org.mikezerosix.telnet.handlers.StatHandler;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class StatHandlerTest  {
    private StatHandler statHandler;

    @Test
    public void testMatch() throws Exception {
        assertTrue(statHandler.matcher("167628.000 STATS: 2787.16,12.06,1589.5,1941.4,2504,170,8,63,87,425,162").matches());
        assertFalse(statHandler.matcher("167628.000 asjdfhsakjdhaskjdhaskjdh").matches());
    }
}