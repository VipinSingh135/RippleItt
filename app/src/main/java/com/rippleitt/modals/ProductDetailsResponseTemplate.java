package com.rippleitt.modals;

/**
 * Created by pc on password/24/2018.
 */

public class ProductDetailsResponseTemplate {

    private int response_code=0;
    private String response_message="";
    private ListingTemplate data;




    public int getResponse_code() {
        return response_code;
    }

    public void setResponse_code(int response_code) {
        this.response_code = response_code;
    }

    public String getResponse_message() {
        return response_message;
    }

    public void setResponse_message(String response_message) {
        this.response_message = response_message;
    }

    public ListingTemplate getData() {
        return data;
    }

    public void setData(ListingTemplate data) {
        this.data = data;
    }
}
