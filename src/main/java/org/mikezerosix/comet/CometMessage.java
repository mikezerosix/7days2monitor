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

    public MessageTarget getMessageTarget() {
        return messageTarget;
    }

    public void setMessageTarget(MessageTarget messageTarget) {
        this.messageTarget = messageTarget;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
