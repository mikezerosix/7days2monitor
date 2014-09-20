package org.mikezerosix.comet;

import com.fasterxml.jackson.databind.JsonNode;
import org.mikezerosix.comet.MessageTarget;

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
