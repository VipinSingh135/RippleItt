package com.rippleitt.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostalCodeApiResponse {

    @SerializedName("response_code")
    @Expose
    private String responseCode;
    @SerializedName("response_message")
    @Expose
    private String responseMessage;
    @SerializedName("data")
    @Expose
    private List<PostalCodeModal> data = null;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public List<PostalCodeModal> getData() {
        return data;
    }

    public void setData(List<PostalCodeModal> data) {
        this.data = data;
    }
}
