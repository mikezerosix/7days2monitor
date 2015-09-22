package org.mikezerosix.util;

import org.junit.Test;

import java.util.Random;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Created by Michael Holopainen 22.9.2015  23:22
 *
 * @copyright Michael Holopainen 2013
 */
public class TelnetLineUtilTest {
    private final Pattern pattern = Pattern.compile(TelnetLineUtil.TIME_STAMP);
    private final String line = "2015-09-22T22:13:25 817461.964 INF ";

    @Test
    public void testTimeStamp() throws Exception {

        assertTrue(pattern.matcher(line).matches());

    }
}