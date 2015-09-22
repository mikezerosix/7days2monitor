package org.mikezerosix.telnet.handlers;


/*
2015-09-22T22:03:28 816864.879 INF Token length: 1368
2015-09-22T22:03:28 816864.879 INF [Steamworks.NET] Auth.AuthenticateUser()
2015-09-22T22:03:28 816864.879 INF [Steamworks.NET] Authenticating player: Mike06 SteamId: 76561198011842231 TicketLen: 1024 Result: k_EBeginAuthSessionResultOK
2015-09-22T22:03:28 816864.879 INF [EAC] Registering user: id=76561198011842231, owner=76561198011842231
2015-09-22T22:03:28 816864.879 INF Allowing player with id 76561198011842231
2015-09-22T22:03:28 816864.891 INF [EAC] UserStatusHandler callback. Status: UserAuthenticated GUID: 76561198011842231 ReqKick: False Message:
2015-09-22T22:03:28 816865.242 INF [Steamworks.NET] Authentication callback. ID: 76561198011842231, owner: 76561198011842231, result: k_EAuthSessionResponseOK
2015-09-22T22:03:28 816865.330 INF RequestToEnterGame: 76561198011842231/Mike06
2015-09-22T22:03:29 816866.520 INF [EAC] Log: Backend connection established.
2015-09-22T22:03:30 816866.771 INF [EAC] Log: User status changed: 76561198011842231. Status: Authenticated Message: N/A
2015-09-22T22:03:30 816866.782 INF [EAC] UserStatusHandler callback. Status: UserAuthenticated GUID: 76561198011842231 ReqKick: False Message:
2015-09-22T22:03:37 816874.191 INF RequestToSpawnPlayer: 171, Mike06, 11
2015-09-22T22:03:37 816874.218 INF Created player with id=171
2015-09-22T22:03:37 816874.218 INF Adding observed entity: 185, (-3211.5, 66.1, -11527.1), 11
2015-09-22T22:03:37 816874.219 INF GMSG: Mike06 joined the game



Allocs
--------------------------------------------
401430.800 OnPlayerConnected 1686
401430.900 PlayerLogin: 639511147302639545/AllocatedID: 0/Paufi/Alpha 9.3
401430.900 Token: FAAAAEyYfikU7Q55U57nCQEAEAHN1TxUGAAAAAEAAAACAAAAvTsQHwAAAABFrQAAAQAAALIAAAAyAAAABAAAAFOe5wkBABABstYDAL07EB9mAKjAAAAAAInGOFQJdlRUAQAVeAAAAAAAALC2mQlDkSC/rEPcKN1WsG7giO+Ho9FxCOGOteYgnXpDeEyAE8FG4m5R08JKw0sjjw8GCcM5IcaCAGKjuGjUyPTgeU2Dgv1ZdfNB9ugddjxBWjXan0hYsFB9cHyLwBlAwnlipU5QuS0JxuVXZ4mCQrXgxyYYU5D9cEsffdUIt1tBbWUiCQkiMSIKCQkiU291bmRzX1BsYXlJbmdhbWUiCQkiMCIKCQkiTm90aWZpY2F0aW9uc19TaG93T25saW5lIgkJIjAiCgkJIlNvdW5kc19QbGF5T25saW5lIgkJIjAiCgkJIk5vdGlmaWNhdGlvbnNfU2hvd01lc3NhZ2UiCQkiMSIKCQkiU291bmRzX1BsYXlNZXNzYWdlIgkJIjEiCgkJIkF1dG9TaWduSW50b0ZyaWVuZHMiCQkiMSIKCQkiU2hvd1RpbWVJbkNoYXRMb2dDaGVjayIJCSIxIgoJCSJBbHdheXNOZXdDaGF0V2luZG93IgkJIjAiCgkJIkNoYXRGbGFzaE1vZGUiCQkiMCIKCQkiMTM1OTMwMDc1IgoJCXsKCQkJIm5hbWUiCQkiU3BsaWZmc3RhciIKCQkJIk5hbWVIaXN0b3J5IgoJCQl7CgkJCQkiMCIJCSJTcGxpZmZzdGFyIgoJCQl9CgkJCSJhdmF0YXIiCQkiYThjYWZkODU5YjJjYmQ3Y2IwYmU1Mzk1OWU1OTQ1Mjk5Y2FjZTM0MyIKCQl9CgkJIjE2NjA5NTY1MCIKCQl7CgkJCSJuYW1lIgkJIkhhdGVjb3JlNExpZmUiCgkJCSJOYW1lSGlzdG9yeSIKCQkJewoJCQkJIjAiCQkiSGF0ZWNvcmU0TGlmZSIKCQkJCSIxIgkJIkg4Y29yZTRMaWZlIgoJCQl9CgkJfQoJCSJTaG93RnJpZW5kc1BhbmVsSW5PdmVybGF5IgkJIjAiCgkJIlZvaWNlUmVjZWl2ZVZvbHVtZSIJCSIwIgoJCSIyNjU2MjQ2OSIKCQl7CgkJCSJuYW1lIgkJIjIwY20gdW5idWZmZWQiCgkJCSJOYW1lSGlzdG9yeSIKCQkJewoJCQkJIjAiCQkiMjBjbSB1bmJ1ZmZlZCIKCQkJfQoJCQkiYXZhdGFyIgkJIjU3MTEzMjQxYmI1OTMyMWMzMzViMjNkNDIzN2E5OTM3YjhkMzM3MjYiCgkJfQoJCSI0NzQwNjQ4MCIKCQl7CgkJCSKvAA==
401430.900 Authenticating player: Paufi SteamId: 76561198126440019 TicketLen: 1024 Result: OK
401430.900 Started thread_CommReader: cl=1672, ch=1
401430.900 Started thread_CommWriter: cl=1672, ch=1
401430.900 Started thread_CommReader: cl=1672, ch=2
401430.900 Started thread_CommWriter: cl=1672, ch=2
401430.900 Allowing player with id 1672
401456.100 RequestToEnterGame: 1672/Paufi
401456.100 GMSG: Paufi joined the game
401458.800 RequestToSpawnPlayer: 2039080, 1672, Paufi, 11
401458.900 Created player with id=2039080
401458.900 Adding observed entity: 1703, (-997.4, 78.0, -96.7), 11
401458.900 Player connected, clientid=1672, entityid=2039080, name=Paufi, steamid=76561198126440019, ip=31.16.59.189
401458.900 Player set to online: 76561198126440019



2015-09-23T00:21:49 825165.640 INF Player disconnected: EntityID=171, PlayerID='76561198011842231', OwnerID='76561198011842231', PlayerName='Mike06'

* */

import org.mikezerosix.service.PlayerService;
import org.mikezerosix.util.TelnetLineUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerLoginHandler implements TelnetOutputHandler {
    private static final Logger log = LoggerFactory.getLogger(PlayerLoginHandler.class);
    public static final String REQUEST_TO_ENTER = TelnetLineUtil.TIME_STAMP + "RequestToEnterGame: (.*?)\\/(.*)";
    public static final String REQUEST_TO_SPAWN_PLAYER = TelnetLineUtil.TIME_STAMP + "RequestToSpawnPlayer: (\\d+), (.*?), (\\d+)";
    private final Pattern spawnPattern = Pattern.compile(REQUEST_TO_SPAWN_PLAYER);
    public static final String REMOVING_PLAYER = TelnetLineUtil.TIME_STAMP + "Player disconnected: EntityID=(\\d+), PlayerID=\\'(\\d+)\\', OwnerID=\\'(\\d+)\\'";
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
            final String name = matchers[0].group(3).trim();
            playerService.login(entityId, name);
            return;
        }
        if (matchers[1].find()) {
            final long entityId = Long.parseLong(matchers[1].group(2).trim());
            playerService.logout(entityId);
        }

    }
}
