package org.mikezerosix.handlers;

import junit.framework.TestCase;
import org.junit.Test;

public class PlayerLoginHandlerTest extends TestCase {

    @Test
    public void testMatch() throws Exception {
        assertTrue("167628.000 STATS: a".matches("\\d{6,}+\\.\\d{3}\\sSTATS:"));
        assertTrue("167628.000 STATS: sdfsdfsdf".matches("\\d{6,}+\\.\\d{3}\\sSTATS:(.*)"));

    }
}