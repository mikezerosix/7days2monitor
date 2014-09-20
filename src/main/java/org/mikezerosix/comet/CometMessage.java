package org.mikezerosix.comet;

import com.fasterxml.jackson.databind.JsonNode;

public class CometMessage {
    private MessageTarget messageTarget;
    private String type;
    private JsonNode data;

    public CometMessage(MessageTarget messageTarget, String type, JsonNode data) {
        this.messageTarget = messageTarget;
        this.type = type;
        this.data = data;
    }

}
