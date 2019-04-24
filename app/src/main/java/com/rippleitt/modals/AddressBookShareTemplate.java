package com.rippleitt.modals;

/**
 * Created by pc on password/17/2018.
 */

public class AddressBookShareTemplate {

    private String response_code="";
    private String response_message="";
    private ContactSharingTemplate[] data;

    private ContactSharingTemplate[]   nonrippleittuserdata;


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

    public ContactSharingTemplate[] getData() {
        return data;
    }

    public void setData(ContactSharingTemplate[] data) {
        this.data = data;
    }

    public ContactSharingTemplate[] getNonrippleittuserdata() {
        return nonrippleittuserdata;
    }

    public void setNonrippleittuserdata(ContactSharingTemplate[] nonrippleittuserdata) {
        this.nonrippleittuserdata = nonrippleittuserdata;
    }
}
