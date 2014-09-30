package org.mikezerosix.rest.data;

public class ChatMessage {
    public String date;
    public String time;
    public String from;
    public String msg;


    public ChatMessage(String line) {
        String[] split = line.split(" ", 4);
        date = split[0];
        time = split[1];
        from = split[2];
        msg = split[3];
    }
}