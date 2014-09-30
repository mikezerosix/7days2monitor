package org.mikezerosix.comet;

public class CometMessage {
    private long timestamp = System.currentTimeMillis();
    private MessageTarget messageTarget;
    private long index;
    private Object data;

    public CometMessage(MessageTarget messageTarget, Object data) {
        this.messageTarget = messageTarget;
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

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


}
