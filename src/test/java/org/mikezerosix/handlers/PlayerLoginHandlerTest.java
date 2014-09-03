package org.mikezerosix.handlers;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PlayerLoginHandlerTest  {

    @Ignore
    @Test
    public void testMatch() throws Exception {
        assertTrue("167628.000 STATS: a".matches("\\d{6,}+\\.\\d{3}\\sSTATS:"));
        assertTrue("167628.000 STATS: sdfsdfsdf".matches("\\d{6,}+\\.\\d{3}\\sSTATS:(.*)"));

    }
}