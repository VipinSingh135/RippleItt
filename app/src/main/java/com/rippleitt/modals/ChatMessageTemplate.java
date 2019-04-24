package com.rippleitt.modals;

/**
 * Created by pc on mail/30/2018.
 */

public class ChatMessageTemplate {

    private String userID="";
    private String text="";
    private Object timestamp;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
