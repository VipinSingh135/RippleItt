package com.rippleitt.modals;

/**
 * Created by manishautomatic on 19/05/18.
 */

public class NotificationsResponseTemplate {


    private String response_code="";
    private String response_message="";
    private String flag="";
    private NotificationTemplate[] data;
    private SystemBroadcastTemplate[] system_broadcasts;


    public SystemBroadcastTemplate[] getSystem_broadcasts() {
        return system_broadcasts;
    }

    public void setSystem_broadcasts(SystemBroadcastTemplate[] system_broadcasts) {
        this.system_broadcasts = system_broadcasts;
    }

    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public String getResponse_message() {
        return response_message;
    }

    public void setResponse_message(String response_message) {
        this.response_message = response_message;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public NotificationTemplate[] getData() {
        return data;
    }

    public void setData(NotificationTemplate[] data) {
        this.data = data;
    }
}
