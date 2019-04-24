package com.rippleitt.modals;

/**
 * Created by manishautomatic on 12/06/18.
 */

public class PriceRangeResponseTemplate {

    private int response_code=0;
    private String response_message="";
    private PriceRangeTemplate[] data;

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

    public PriceRangeTemplate[] getData() {
        return data;
    }

    public void setData(PriceRangeTemplate[] data) {
        this.data = data;
    }
}
