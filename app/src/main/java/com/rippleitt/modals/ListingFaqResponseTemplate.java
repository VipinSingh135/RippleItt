package com.rippleitt.modals;

/**
 * Created by manishautomatic on 12/06/18.
 */

public class ListingFaqResponseTemplate {


    private String response_code="";
    private String response_message="";
    private ListingFaqTemplate[] data;


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

    public ListingFaqTemplate[] getData() {
        return data;
    }

    public void setData(ListingFaqTemplate[] data) {
        this.data = data;
    }
}
