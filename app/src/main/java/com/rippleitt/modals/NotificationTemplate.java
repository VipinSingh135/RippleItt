package com.rippleitt.modals;

/**
 * Created by manishautomatic on 19/05/18.
 */

public class NotificationTemplate {


    private String listing_id="";
    private String type="";
    private String order_id="";
    private String messsage="";
    private String time="";


    public String getListing_id() {
        return listing_id;
    }

    public void setListing_id(String listing_id) {
        this.listing_id = listing_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getMessage() {
        return messsage;
    }

    public void setMessage(String message) {
        this.messsage = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
