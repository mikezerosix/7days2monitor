package org.mikezerosix.telnet.handlers;


/*
5050.021 STATS: 84.03,79.01,142.0,384.2,9,0,0,0,0,199,20

167628.000 Time: 2787.16m FPS: 12.06 Heap: 1589.5MB Max: 1941.4MB Chunks: 2504 CGO: 170 Ply: 8 Zom: 63 Ent: 87 (425) Items: 162
167628.000 STATS: 2787.16,12.06,1589.5,1941.4,2504,170,8,63,87,425,162
 */

import org.mikezerosix.util.TelentLineUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatHandler implements TelnetOutputHandler {
    public static final String STAT_TRIGGER = TelentLineUtil.TIME_STAMP + "STATS: (.*)";
    private final Pattern pattern = Pattern.compile(STAT_TRIGGER);

    @Override
    public Matcher[] matcher(String line) {
        return new Matcher[]{pattern.matcher(line)};
    }


    @Override
    public void handleInput(String input) {
        final Matcher matcher = matcher(input)[0];
        if (matcher.find()) {
            final String data = matcher.group(1).trim();
            String[] dataArr = data.split(",");

        }
    }
}
