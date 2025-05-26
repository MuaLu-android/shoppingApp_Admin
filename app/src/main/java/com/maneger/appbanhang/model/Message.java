package com.maneger.appbanhang.model;
public class Message {
    private String token;
    Notification notification;
    public Message(String token, Notification notification) {
        this.token = token;
        this.notification = notification;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public Notification getNotification() {
        return notification;
    }
    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
