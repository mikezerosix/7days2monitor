package org.mikezerosix.handlers;

import junit.framework.TestCase;

public class ChatHandlerTest extends TestCase {
    ChatHandler chatHandler = new ChatHandler();

    public void testMatch() throws Exception {
        assertTrue(chatHandler.match("167612.300 GMSG: ShoC: they know where I am... eeek"));
    }

    public void testHandleInput() throws Exception {
        chatHandler.handleInput("236661.100 GMSG: [FF00FF]Camalot: nope");
        chatHandler.handleInput("236661.100 STAT: sad fsd sdf");
        chatHandler.handleInput("236664.200 GMSG: Server:  hello");

    }
}