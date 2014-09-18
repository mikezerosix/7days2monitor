package org.mikezerosix.steam;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by michael on 30.8.2014.
 * <p>
 * http://steamcommunity.com/dev/registerkey
 * <p>
 * https://developer.valvesoftware.com/wiki/Steam_Web_API
 */
public class SteamAPI {

    private String apiKey;
    private static final String GAME_NEWS_URL = "http://api.steampowered.com/ISteamNews/GetNewsForApp/v0002/?appid=251570&count=1&maxlength=300&format=json";
    private static final long CACHE_TIME = 1000 * 60 * 30;
    private static long cacheTTL = System.currentTimeMillis();
    private static JsonNode cache;

    protected static JsonNode readJsonNode(String uri) throws IOException {
        final Response response = Request.Get(uri).execute();
        final InputStream inputStream = response.returnResponse().getEntity().getContent();
        return new ObjectMapper().readTree(inputStream);
    }

    public SteamAPI(String apiKey) {
        this.apiKey = apiKey;
    }


    public String GetPlayerSummaries(String steamId) {
        String url = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=" + apiKey + "&steamids=" + steamId;
 /*
 Public ( always visible)
				"profilestate": 1,
				"personaname": "Robin",
				"lastlogoff": 1409332784,

Private
                "realname": "Robin Walker",
                "primaryclanid": "103582791429521412",
                "timecreated": 1063407589,
                "personastateflags": 0,
                "loccountrycode": "US",
                "locstatecode": "WA",
                "loccityid": 3961
                *
                */
        throw new UnsupportedOperationException("todo");
    }

    public String GetFriendList(String steamId) {

        String url = "http://api.steampowered.com/ISteamUser/GetFriendList/v0001/?key=" + apiKey + "&steamid=" + steamId + "&relationship=all";
/*
The user's friends list, as an array of friends. Nothing will be returned if the profile is private.
steamid
 */
        throw new UnsupportedOperationException("todo");
    }

    public String getUserStatsForGame(String steamId) {
        String url = "http://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v0002/?appid=251570&key=" + apiKey + "&steamid=" + steamId;
        throw new UnsupportedOperationException("todo: returns nothing useful");
    }

    public String GetOwnedGames(String steamId) {
        String url = " http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=" + apiKey + "&steamid=" + steamId;
        throw new UnsupportedOperationException("todo: returns nothing useful");
    }

    public JsonNode getGameNews() throws IOException {
        if (System.currentTimeMillis() > cacheTTL) {
            cache = readJsonNode(GAME_NEWS_URL);
            cacheTTL = System.currentTimeMillis() + CACHE_TIME;
        }
        return cache;
    }
}