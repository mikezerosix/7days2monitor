package org.mikezerosix.comet;

public class CometMessage {
    private long timestamp = System.currentTimeMillis();
    private MessageTarget messageTarget;
    private String type;
    private Object data;

    public CometMessage(MessageTarget messageTarget, String type, Object data) {
        this.messageTarget = messageTarget;
        this.type = type;
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
