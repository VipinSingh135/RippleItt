package com.rippleitt.modals;

/**
 * Created by pc on password/15/2018.
 */

public class SubCategoryListTemplate {

    private int response_code=0;
    private String response_message="";
    private CategoryTemplate[] data;


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

    public CategoryTemplate[] getData() {
        return data;
    }

    public void setData(CategoryTemplate[] data) {
        this.data = data;
    }

}
