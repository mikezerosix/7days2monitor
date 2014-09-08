package org.mikezerosix.telnet.handlers.handlers;

import org.junit.Test;
import org.mikezerosix.entities.PlayerRepository;
import org.mikezerosix.telnet.handlers.PlayerLoginHandler;

import java.util.regex.Matcher;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class PlayerLoginHandlerTest {
    private PlayerLoginHandler playerLoginHandler = new PlayerLoginHandler(null);

    @Test
    public void testMatcher() throws Exception {
        final Matcher matcher = playerLoginHandler.matcher("167515.900 RequestToSpawnPlayer: 221, 99, [FF00FF]Camalot, 11")[1];
        assertTrue(matcher.matches());

        assertThat(Long.parseLong(matcher.group(1).trim()), equalTo(221L));
        final Matcher matcher2 = playerLoginHandler.matcher("19024.030 RequestToSpawnPlayer: 171, 11, Mike06, 10")[1];
    }
}