package org.mikezerosix.telnet.commands;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class ListPlayersRepeatingCommandTest {
    private static final Pattern pattern = Pattern.compile("(\\d+)\\. id=(\\d+), (.+), pos=\\((-?\\d+\\.\\d), (-?\\d+\\.\\d), (-?\\d+\\.\\d)\\), rot=\\(.*\\), remote=True, health=(\\d+), (\\d+)");

    public static final String one = "1. id=10416, -=DK=-Caretaker, pos=(1372.9, 69.0, 1213.1), rot=(-22.5, -16.9, 0.0), remote=True, health=61, 76561198011459194";
    public static final String two = "2. id=155901, bulldog(DK), pos=(1372.3, 69.7, 1209.9), rot=(-12.7, 334.1, 0.0), remote=True, health=100, 76561197983278724";
    public static final String three = "3. id=163438, Br3aK3R, pos=(1414.0, 64.0, 1026.5), rot=(-29.5, -243.3, 0.0), remote=True, health=81, 76561197972705298";
    static final String  b1 =  "1. id=10416, -=DK=-Caretaker, pos=(1464.2, 72.9, 1216.6), rot=(21.1, 115.4, 0.0), remote=True, health=55, 76561198011459194";
    static final String b2 =  "2. id=155901, bulldog(DK), pos=(1460.6, 70.9, 1209.3), rot=(15.3, 78.8, 0.0), remote=True, health=100, 76561197983278724";
    public static final String five = "Total of 3 in the game";

    @Test
    public void testPattern() throws Exception {
        assertTrue(pattern.matcher(three).matches());

    }

    @Test
    public void testMatcher() throws Exception {
        ListPlayersRepeatingCommand listPlayersRepeatingCommand = new ListPlayersRepeatingCommand(null);
        Matcher[] matchers = listPlayersRepeatingCommand.matcher(one);
        assertTrue(matchers[0].matches());
        assertFalse(matchers[1].matches());
    }
    @Test
    public void testMatcher2() throws Exception {
        ListPlayersRepeatingCommand listPlayersRepeatingCommand = new ListPlayersRepeatingCommand(null);
        Matcher[] matchers = listPlayersRepeatingCommand.matcher(b1);
        assertTrue(matchers[0].matches());
        assertFalse(matchers[1].matches());
        Matcher[] matchers2 = listPlayersRepeatingCommand.matcher(b2);
        assertTrue(matchers2[0].matches());
        assertFalse(matchers2[1].matches());
    }
}