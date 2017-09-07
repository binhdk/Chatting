package com.vanbinh.chatting.models;

/**
 * Created by vanbinh on 8/10/2017.
 *
 */

public class Message {

    private int id;
    private String message;
    private String username;
    private long time;

    public Message() {
    }

    public Message(String message, String username) {
        this.message = message;
        this.username = username;
        this.time = System.currentTimeMillis();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
