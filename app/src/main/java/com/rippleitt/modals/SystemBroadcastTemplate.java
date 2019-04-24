package com.rippleitt.modals;

/**
 * Created by manishautomatic on 12/06/18.
 */

public class SystemBroadcastTemplate {

    private String message="";
    private String timestamp="";
    private String listing_id="";


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getListing_id() {
        return listing_id;
    }

    public void setListing_id(String listing_id) {
        this.listing_id = listing_id;
    }
}
