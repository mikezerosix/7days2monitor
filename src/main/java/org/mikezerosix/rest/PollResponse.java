package org.mikezerosix.rest;

import com.fasterxml.jackson.databind.JsonNode;

public class PollResponse {
    private MessageTarget messageTarget;
    private String type;
    private JsonNode data;

    public PollResponse(MessageTarget messageTarget, String type, JsonNode data) {
        this.messageTarget = messageTarget;
        this.type = type;
        this.data = data;
    }

}
