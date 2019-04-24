package com.rippleitt.modals;

import org.json.JSONException;
import org.json.JSONObject;

public class PojoFCMPush {

    private String to;
    private String priority;
    private String sound;
    private boolean content_available=true;
    private PojoFCMPushData data;
    private PojoFCMPushNotification notification;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public PojoFCMPushData getData() {
        return data;
    }

    public void setData(PojoFCMPushData data) {
        this.data = data;
    }

    public PojoFCMPushNotification getNotification() {
        return notification;
    }

    public void setNotification(PojoFCMPushNotification notification) {
        this.notification = notification;
    }

    public boolean isContent_available() {
        return content_available;
    }

    public void setContent_available(boolean content_available) {
        this.content_available = content_available;
    }

    public String toJSON(){

        JSONObject jsonObject= new JSONObject();
//        private String to;
//        private String priority;
//        private String sound;
//        private boolean content_available=true;
//        private PojoFCMPushData data;
//        private PojoFCMPushNotification notification;
        try {
            jsonObject.put("to", getTo());
            jsonObject.put("priority", getPriority());
            jsonObject.put("sound", getSound());
            jsonObject.put("content_available", isContent_available());
//            jsonObject.put("data", getData().toJSON());
            jsonObject.put("notification", getNotification());

            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

    }
}
