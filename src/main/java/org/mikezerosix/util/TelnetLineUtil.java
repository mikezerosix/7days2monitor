package org.mikezerosix.util;

/*
2015-09-22T22:13:25 817461.964 INF

 */
public class TelnetLineUtil {
    public static final String INTEGER = "(\\d+)";
    public static final String DECIMAL_GROUP = "(\\d+\\.\\d+)";
    public static final String TIME_STAMP = "-?(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2})\\s"+DECIMAL_GROUP+"\\s([A-Z]{3})\\s";


}
