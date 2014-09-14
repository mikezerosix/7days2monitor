package org.mikezerosix.telnet.handlers;

import org.mikezerosix.telnet.TelnetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
*** Connected with 7DTD server.
*** Server version: Alpha 9.3 (b4) Compatibility Version: Alpha 9.3
*** Dedicated server only build

Server IP:   Any
Server port: 25045
Max players: 8
Game mode:   GameModeSurvivalMP
World:       Random Gen
Game name:   HC random map - limited PvP
Difficulty:  4

* Allocs server fixes loaded

*/
public class ServerGreetingHandler implements TelnetOutputHandler {
    public static final Logger log = LoggerFactory.getLogger(ServerGreetingHandler.class);
    private static Pattern[] patterns = new Pattern[11];
    private TelnetService.ServerInformation serverInformation;


    static {
        patterns[0] = Pattern.compile("\\*\\*\\* Connected with 7DTD server\\.");
        patterns[1] = Pattern.compile("\\*\\*\\* Server version: (.*?) Compatibility Version: (.*?)");
        patterns[2] = Pattern.compile("Server IP:(.*)");
        patterns[3] = Pattern.compile("Server port: (\\d+)");
        patterns[4] = Pattern.compile("Max players: (\\d+)");
        patterns[5] = Pattern.compile("Game mode: (.*)");
        patterns[6] = Pattern.compile("World: (.*)");
        patterns[7] = Pattern.compile("Game name: (.*)");
        patterns[8] = Pattern.compile("Difficulty: (\\d+)");
        patterns[9] = Pattern.compile("\\*\\*\\* Allocs server fixes loaded");
        patterns[10] = Pattern.compile("Press \\'help\\' to get a list of all commands. Press \\'exit\\' to end session\\.");
    }

    public ServerGreetingHandler(TelnetService.ServerInformation serverInformation) {
        this.serverInformation = serverInformation;
    }

    @Override
    public Matcher[] matcher(String line) {
        Matcher[] matchers = new Matcher[patterns.length];
        for (int i = 0; i < matchers.length; i++) {
            matchers[i] = patterns[i].matcher(line);

        }
        return matchers;
    }

    @Override
    public void handleInput(String input) {
        Matcher[] matchers = matcher(input);
        for (int i = 0; i < matchers.length; i++) {
            if (matchers[i].matches()) {
                switch (i) {
                    case 0:
                        serverInformation.connected = true;
                        break;
                    case 1:
                        serverInformation.version = matchers[i].group(1).trim();
                        serverInformation.compatibility = matchers[i].group(2).trim();
                        break;
                    case 2:
                        serverInformation.ip = matchers[i].group(1).trim();
                        break;
                    case 3:
                        serverInformation.port = Integer.parseInt(matchers[i].group(1).trim());
                        break;
                    case 4:
                        serverInformation.maxPlayers = Integer.parseInt(matchers[i].group(1).trim());
                        break;
                    case 5:
                        serverInformation.mode = matchers[i].group(1).trim();
                        break;
                    case 6:
                        serverInformation.world = matchers[i].group(1).trim();
                        break;
                    case 7:
                        serverInformation.game = matchers[i].group(1).trim();
                        break;
                    case 8:
                        serverInformation.difficulty = Integer.parseInt(matchers[i].group(1).trim());
                        break;
                    case 9:
                        serverInformation.allocsExtension = true;
                        break;
                    case 10:
                        serverInformation.help = true;
                        break;
                }
            }
        }
    }

}
