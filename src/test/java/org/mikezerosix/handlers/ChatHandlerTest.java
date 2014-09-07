package org.mikezerosix.handlers;

import junit.framework.TestCase;
import org.junit.Test;
import org.mikezerosix.telnet.handlers.ChatHandler;

import java.util.regex.Matcher;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class ChatHandlerTest {
    private ChatHandler chatHandler = new ChatHandler();

    @Test
    public void testMatch() throws Exception {
        assertTrue(chatHandler.matcher("236661.100 GMSG: [FF00FF]Camalot: nope").matches());
        assertFalse(chatHandler.matcher("236661.100 STAT: sad fsd sdf").matches());
        assertTrue(chatHandler.matcher("236664.200 GMSG: Server:  hello").matches());
        assertTrue(chatHandler.matcher("167612.300 GMSG: ShoC: they know where I am... eeek").matches());
    }

}