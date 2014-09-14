package org.mikezerosix.steam;

import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class SteamAPITest {
    SteamAPI steamAPI = new SteamAPI("");

    @Test
    public void testGetGameNews() throws Exception {
        JsonNode gameNews = steamAPI.getGameNews();
        assertThat(gameNews, CoreMatchers.notNullValue());
    }
}