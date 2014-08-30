package org.mikezerosix.steam;

/**
 * Created by michael on 30.8.2014.
 *
 *  http://steamcommunity.com/dev/registerkey
 *
 *  https://developer.valvesoftware.com/wiki/Steam_Web_API
 */
public class SteamAPI {

    private String apiKey;

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
}