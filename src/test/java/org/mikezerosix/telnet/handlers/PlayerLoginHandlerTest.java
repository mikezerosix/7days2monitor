package org.mikezerosix.telnet.handlers;

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
        final Matcher matcher = playerLoginHandler.matcher("2015-09-22T22:03:37 816874.191 INF RequestToSpawnPlayer: 171, Mike06, 11\n")[1];
        assertTrue(matcher.matches());

        assertThat(Long.parseLong(matcher.group(1).trim()), equalTo(221L));
        final Matcher matcher2 = playerLoginHandler.matcher("2015-09-22T22:03:37 816874.191 INF RequestToSpawnPlayer: 171, Mike06, 11\n")[1];
    }
}