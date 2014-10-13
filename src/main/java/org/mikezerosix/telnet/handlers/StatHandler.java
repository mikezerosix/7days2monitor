package org.mikezerosix.telnet.handlers;

import org.mikezerosix.entities.Stat;
import org.mikezerosix.entities.StatRepository;
import org.mikezerosix.service.StatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.PrimitiveIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mikezerosix.util.TelnetLineUtil.DECIMAL_GROUP;
import static org.mikezerosix.util.TelnetLineUtil.TIME_STAMP;
/*
5050.021 STATS: 84.03,79.01,142.0,384.2,9,0,0,0,0,199,20

167628.000 Time: 2787.16m FPS: 12.06 Heap: 1589.5MB Max: 1941.4MB Chunks: 2504 CGO: 170 Ply: 8 Zom: 63 Ent: 87 (425) Items: 162
167628.000 STATS: 2787.16,12.06,1589.5,1941.4,2504,170,8,63,87,425,162
 */

public class StatHandler implements TelnetOutputHandler {
    private static final Logger log = LoggerFactory.getLogger(StatHandler.class);
    public static final String STAT_TRIGGER = TIME_STAMP + "STATS: "
            + DECIMAL_GROUP + ","
            + DECIMAL_GROUP + ","
            + DECIMAL_GROUP + ","
            + DECIMAL_GROUP + ","
            + "(\\d+),"
            + "(\\d+),"
            + "(\\d+),"
            + "(\\d+),"
            + "(\\d+),"
            + "(\\d+),"
            + "(\\d+)";
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
        stat.setUptime(minutes2Seconds(Double.parseDouble(matcher.group(1))));
        stat.setFps(Double.parseDouble(matcher.group(2)));
        stat.setMemHeap(Double.parseDouble(matcher.group(3)));
        stat.setMemMax(Double.parseDouble(matcher.group(4)));
        stat.setChunks(Integer.parseInt(matcher.group(5)));
        stat.setCgo(Integer.parseInt(matcher.group(6)));
        stat.setPlayers(Integer.parseInt(matcher.group(7)));
        stat.setZombies(Integer.parseInt(matcher.group(8)));
        stat.setEntities(Integer.parseInt(matcher.group(9))-stat.getPlayers()-stat.getZombies());
        stat.setEntities2(Integer.parseInt(matcher.group(10)));
        stat.setItems(Integer.parseInt(matcher.group(11)));
        return stat;
    }

    private long minutes2Seconds(double time) {
        return (long) (60 * time);
    }

    public void setStatDays(long statDays) {
        statService.setStatDays(statDays);
    }
}
