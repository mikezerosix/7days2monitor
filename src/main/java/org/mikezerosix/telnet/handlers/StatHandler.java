package org.mikezerosix.telnet.handlers;


/*
167628.000 Time: 2787.16m FPS: 12.06 Heap: 1589.5MB Max: 1941.4MB Chunks: 2504 CGO: 170 Ply: 8 Zom: 63 Ent: 87 (425) Items: 162
167628.000 STATS: 2787.16,12.06,1589.5,1941.4,2504,170,8,63,87,425,162
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatHandler implements TelnetOutputHandler {
    public static final String STAT_TRIGGER = "\\d+\\.\\d{3}\\sSTATS: (.*)";
    private final Pattern pattern = Pattern.compile(STAT_TRIGGER);

    @Override
    public Matcher matcher(String line) {
        return pattern.matcher(line) ;
    }


    @Override
    public void handleInput(String input) {

    }
}
