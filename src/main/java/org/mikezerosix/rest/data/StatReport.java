package org.mikezerosix.rest.data;

import org.mikezerosix.entities.Stat;

public class StatReport {
        public Stat current;
        public Object[] max;
    //TODO: remove this if for old legacy polling
    public long ts = System.currentTimeMillis();
        public StatReport(Stat current, Object[] max) {
            this.current = current;
            this.max = max;
        }
    }
