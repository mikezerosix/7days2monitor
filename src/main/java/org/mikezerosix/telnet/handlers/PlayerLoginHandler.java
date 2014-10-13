package org.mikezerosix.telnet.handlers;


/*
167507.800 OnPlayerConnected 99
167507.800 PlayerLogin: 905223526834455429/AllocatedID: 0/[FF00FF]Camalot/Alpha 9.1
167507.800 Token: FAAAALOAZS+l3WGnoK7jAAEAEAElOPtTGAAAAAEAAAACAAAAOsJd1QAAAAD8/BkAAgAAALIAAAAyAAAABAAAAKCu4wABABABstYDADrCXdUKAajAAAAAAOAN81NgvQ5UAQAVeAAAAAAAAEL7nBNenJLl6SRt3h25i3ciXFcQZ/v3mC+JSGkT7Akrc5cnPVdEp4vsNAimVhqFPGjnYobYTkVhtbMRfQX3fbjYlTFwBVMAeqx5+fkC766BftDv8NxxQd0RYYsHJFB3e6C9QwGKhE7fbnZ3xzvAyOBFWFIsczut/CUsGpqbIQnyaWFjbV92YWx2ZWdhbWVzAAEAAAACY29udmFyX2JhbGxvd2dtc3F1ZXJ5dmlhY21fdmFsdmVnb2xkc3JjZ2FtZXMAAQAAAAJjb252YXJfYmFsbG93Z21zcXVlcnl2aWFjbV96ZXJvYXBwaWQAAQAAAAJjb252YXJfYmNsaWVudGFsbG93aGFyZHdhcmVwcm9tb3MAAQAAAAJjb252YXJfYmNsaWVudHJlY29tbWVuZGF0aW9uc2VuYWJsZWQAAQAAAAJjb252YXJfYmNvbW1lbnRub3RpZmljYXRpb25zZW5hYmxlZAABAAAAAmNvbnZhcl9iY29tbXVuaXR5YmV0YQAAAAAAAmNvbnZhcl9iZW5hYmxlYXBwcwABAAAAAmNvbnZhcl9ib2ZmbGluZW1lc3NhZ2VzZW5hYmxlZAABAAAAAmNvbnZhcl9ic3RlYW0zbGltaXRlZHVzZXJlbmFibGUAAQAAAAJjb252YXJfYnRlbXBvcmFyeWNvbnZhcmhpZGVtb2JpbGVkZXZpY2VzdGF0dXMAAAAAAAJjb252YXJfbWFuYWdlZ2lmdHNvbndlYgABAAAAAmNvbnZhcl9zZXJ2ZXJicm93c2VycGluZ3N1cnZleXN1Ym1pdHBjdAABAAAAAmNvbnZhcl91c2VtbXMAAQAAAAJjb252YXJfdm9pY2VfcXVhbGl0eQAEAAAAAmNvbnZhcl9saWJyYXJ5X3NoYXJpbmdfYWNjb3VudF9tYXgABQAAAAJjb252YXJfbmNsaWVudGJhY2tncm91bmRhdXRvdXBkYXRldGltZXNwcmVhZG1pbnV0ZXMA8AAAAAFkZXZ3aWtpcmVwb3J0YmV0YWJ1Z3VybABodHRwOi8vc3RlYW1jb21tdW5pdHkuY29tL2Rpc2N1c3Npb25zL2ZvcnVtLzAvODQ2OTQxNzEwNjEzMjI2NDM3LwABbmV3c3VybABodHRwOi8vc3RvcmUuc3RlYW1wb3dlcmVkLmNvbS9uZXdzLwABc3RhdGUAZVN0YXRlQXZhaWxhYmxlAAEAAAAAAA==
167507.800 Authenticating player: [FF00FF]Camalot SteamId: 76561197975187104 TicketLen: 1024 Result: OK
167507.900 Started thread_CommReader: cl=99, ch=1
167507.900 Started thread_CommWriter: cl=99, ch=1
167507.900 Started thread_CommReader: cl=99, ch=2
167507.900 Start1e6d7 5t07h.r90e0a dAl_lCowoimngm Wplrayiert weirth:  idc l99=
99, ch=2
167515.300 RequestToEnterGame: 99/[FF00FF]Camalot
167515.300 GMSG: [FF00FF]Camalot joined the game


167515.900 RequestToSpawnPlayer: 221, 99, [FF00FF]Camalot, 11
167515.900 Created player with id=221
*

22207.830 Authenticating player: [FF00FF]Camalot SteamId: 76561197975187104 TicketLen: 1024 Result: OK

59858.590 Authenticating player: Mike06 SteamId: 76561198011842231 TicketLen: 10                      24 Result: OK
59864.080 RequestToEnterGame: 7/Mike06
59864.110 GMSG: Mike06 joined the game
59865.050 RequestToSpawnPlayer: 151327, 7, Mike06, 10
59865.080 Created player with id=151327
59865.090 Adding observed entity: 7, (-511.6, 4.0, -644.5), 10





* */

import org.mikezerosix.service.PlayerService;
import org.mikezerosix.util.TelnetLineUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerLoginHandler implements TelnetOutputHandler {
    private static final Logger log = LoggerFactory.getLogger(PlayerLoginHandler.class);
    public static final String REQUEST_TO_SPAWN_PLAYER = TelnetLineUtil.TIME_STAMP + "RequestToSpawnPlayer: (\\d+), (\\d+), (.*?), (\\d+).*";
    private final Pattern spawnPattern = Pattern.compile(REQUEST_TO_SPAWN_PLAYER);
    public static final String AUTHENTICATING_PLAYER = TelnetLineUtil.TIME_STAMP + "Authenticating player: (.*?)\\sSteamId: (\\d+) TicketLen: (\\d+) Result: OK";
    public static final String REMOVING_PLAYER = TelnetLineUtil.TIME_STAMP + "Removing player with id clientId=(\\d+), entityId=(\\d+).*";
    private final Pattern removingPattern = Pattern.compile(REMOVING_PLAYER);
    private PlayerService playerService;

    public PlayerLoginHandler(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public Matcher[] matcher(String line) {
        return new Matcher[]{spawnPattern.matcher(line), removingPattern.matcher(line)};
    }

    @Override
    public void handleInput(String input) {
        final Matcher[] matchers = matcher(input);
        if (matchers[0].find()) {
            final long entityId = Long.parseLong(matchers[0].group(1).trim());
            final long clientId = Long.parseLong(matchers[0].group(2).trim());
            final String name = matchers[0].group(3).trim();
            playerService.login(entityId, clientId, name);
            return;
        }
        if (matchers[1].find()) {
            final long entityId = Long.parseLong(matchers[1].group(2).trim());
            playerService.logout(entityId);
        }

    }
}
