package org.mikezerosix.telnet.handlers;

import org.junit.Test;
import org.mikezerosix.entities.Stat;

import java.util.regex.Matcher;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class StatHandlerTest {
    private StatHandler statHandler = new StatHandler(null);

    public static final String stat1 = "2015-09-22T22:13:25 817461.964 INF Time: 13624.29m FPS: 179.90 Heap: 834.8MB Max: 903.6MB Chunks: 1000 CGO: 21 Ply: 1 Zom: 7 Ent: 14 (47) Items: 20";


    @Test
    public void testMatcher() throws Exception {
        assertTrue(statHandler.matcher(stat1)[0].matches());
    }

    @Test
    public void testGettingStat1() throws Exception {
        Matcher matcher = statHandler.matcher(stat1)[0];
        assertTrue(matcher.find());
        Stat st = statHandler.getStat(matcher);
        assertThat(st.getUptime() , is(817461964L));
        assertThat(st.getFps(), is(179.90));
        assertThat(st.getMemHeap(), is(834.8));
        assertThat(st.getMemMax(), is(903.6));
        assertThat(st.getChunks() , is(1000));
        assertThat(st.getCgo() , is(21));
        assertThat(st.getPlayers() , is(1));
        assertThat(st.getZombies() , is(7));
        assertThat(st.getEntities() , is(14));
        assertThat(st.getEntities2() , is(47));
        assertThat(st.getItems() , is(20));
    }


}