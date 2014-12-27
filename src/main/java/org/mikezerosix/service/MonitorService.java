package org.mikezerosix.service;

import com.google.common.collect.Lists;
import org.mikezerosix.entities.Monitor;
import org.mikezerosix.entities.MonitorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MonitorService {
    private static final Logger log = LoggerFactory.getLogger(MonitorService.class);
    private MonitorRepository monitorRepository;


    public MonitorService(MonitorRepository monitorRepository) {
        this.monitorRepository = monitorRepository;
    }

    public List<Monitor> list() {
       return Lists.newArrayList(monitorRepository.findAll());
    }

    public Monitor create(Monitor monitor) {
       return monitorRepository.save(monitor);
    }

}
