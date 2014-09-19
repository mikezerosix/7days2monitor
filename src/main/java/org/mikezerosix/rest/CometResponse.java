package org.mikezerosix.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mikezerosix.AppConfiguration.PROTECTED_URL;
import static spark.Spark.get;

public class CometResponse {
    private static final Logger log = LoggerFactory.getLogger(CometResponse.class);

    public CometResponse() {


        get(PROTECTED_URL + "/comet", (request, response) -> "");

    }
}
