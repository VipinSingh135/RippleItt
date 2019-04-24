package com.rippleitt.modals;

/**
 * Created by pc on password/17/2018.
 */

public class MyListingDetailsPayload {

    private String response_code="";
    private String response_message="";
    private SelfListingTemplate data;

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

    public SelfListingTemplate getData() {
        return data;
    }

    public void setData(SelfListingTemplate data) {
        this.data = data;
    }




}
