package org.mikezerosix.telnet.handlers;

import org.mikezerosix.entities.Stat;
import org.mikezerosix.service.StatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mikezerosix.util.TelnetLineUtil.*;
/*
2015-09-22T22:13:25 817461.964 INF Time: 13624.29m FPS: 179.90 Heap: 834.8MB Max: 903.6MB Chunks: 1000 CGO: 21 Ply: 1 Zom: 7 Ent: 14 (47) Items: 20
*/

public class StatHandler implements TelnetOutputHandler {
    private static final Logger log = LoggerFactory.getLogger(StatHandler.class);
    public static final String STAT_TRIGGER = TIME_STAMP
            + "Time:\\s" + DECIMAL_GROUP + "m\\s"
            + "FPS:\\s" + DECIMAL_GROUP + "\\s"
            + "Heap:\\s" + DECIMAL_GROUP + "MB\\s"
            + "Max:\\s" + DECIMAL_GROUP + "MB\\s"
            + "Chunks:\\s" + INTEGER + "\\s"
            + "CGO:\\s" + INTEGER + "\\s"
            + "Ply:\\s" + INTEGER + "\\s"
            + "Zom:\\s" + INTEGER + "\\s"
            + "Ent:\\s" + INTEGER + "\\s"
            + "\\(" + INTEGER + "\\)\\s"
            + "Items:\\s" + INTEGER;
    private final Pattern pattern = Pattern.compile(STAT_TRIGGER);
    private StatService statService;

    public StatHandler(StatService statService) {
        this.statService = statService;
    }

    @Override
    public Matcher[] matcher(String line) {
        return new Matcher[]{pattern.matcher(line)};
    }

    @Override
    public void handleInput(String input) {
        final Matcher matcher = matcher(input)[0];
        if (matcher.find()) {
            log.debug("Matching stat line : " + input);
            Stat stat = getStat(matcher);
            statService.record(stat);
        }
    }

    protected Stat getStat(Matcher matcher) {
        Stat stat = new Stat();
        stat.setUptime(Math.round(1000L * Double.parseDouble(matcher.group(2))));
        stat.setFps(Double.parseDouble(matcher.group(5)));
        stat.setMemHeap(Double.parseDouble(matcher.group(6)));
        stat.setMemMax(Double.parseDouble(matcher.group(7)));
        stat.setChunks(Integer.parseInt(matcher.group(8)));
        stat.setCgo(Integer.parseInt(matcher.group(9)));
        stat.setPlayers(Integer.parseInt(matcher.group(10)));
        stat.setZombies(Integer.parseInt(matcher.group(11)));
        stat.setEntities(Integer.parseInt(matcher.group(12)));
        stat.setEntities2(Integer.parseInt(matcher.group(13)));
        stat.setItems(Integer.parseInt(matcher.group(14)));
        return stat;
    }

    public void setStatDays(long statDays) {
        statService.setStatDays(statDays);
    }
}
