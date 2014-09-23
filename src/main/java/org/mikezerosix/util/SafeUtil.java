package org.mikezerosix.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SafeUtil {
    private static final Logger log = LoggerFactory.getLogger(SafeUtil.class);

    public static void safeSleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.warn("SafeSleep Exception  ", e);
        }
    }

    public static long safeParseLong(String longString) {
        try {
            return Long.parseLong(longString);
        } catch (Exception e) {
            log.warn("bad string long value ({}), assuming 0", longString);
            return 0L;
        }
    }
}
