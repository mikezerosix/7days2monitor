package org.mikezerosix.telnet.handlers;

import org.junit.Test;
import org.mikezerosix.entities.Stat;

import java.util.regex.Matcher;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class StatHandlerTest {
    private StatHandler statHandler = new StatHandler(null);

    public static final String stat1 = "5050.021 STATS: 84.03,79.01,142.0,384.2,9,1,2,3,4,199,20";
    public static final String wrong = "167628.000 Time: 2787.16m FPS: 12.06 Heap: 1589.5MB Max: 1941.4MB Chunks: 2504 CGO: 170 Ply: 8 Zom: 63 Ent: 87 (425) Items: 162";
    public static final String stat2 = "167628.000 STATS: 2787.16,12.06,1589.5,1941.4,2504,170,8,63,87,425,162";



    @Test
    public void testMatcher() throws Exception {
        assertTrue(statHandler.matcher(stat1)[0].matches());
        assertTrue(statHandler.matcher(stat2)[0].matches());
        assertFalse(statHandler.matcher(wrong)[0].matches());
    }

    @Test
    public void testGettingStat1() throws Exception {
        Matcher matcher = statHandler.matcher(stat1)[0];
        assertTrue(matcher.find());
        Stat st = statHandler.getStat(matcher);
        assertThat(st.getUptime() , is(5041L));
        assertThat(st.getFps(), is(79.01));
        assertThat(st.getMemHeap(), is(142.0));
        assertThat(st.getMemMax(), is(384.2));
        assertThat(st.getChunks() , is(9));
        assertThat(st.getCgo() , is(1));
        assertThat(st.getPlayers() , is(2));
        assertThat(st.getZombies() , is(3));
        assertThat(st.getEntities() , is(4));
        assertThat(st.getEntities2() , is(199));
        assertThat(st.getItems() , is(20));
    }

    @Test
    public void testGettingStat2() throws Exception {
        Matcher matcher = statHandler.matcher(stat2)[0];
        assertTrue(matcher.find());

        Stat st = statHandler.getStat(matcher);
    }
}