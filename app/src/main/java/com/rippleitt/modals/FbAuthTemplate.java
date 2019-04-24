package com.rippleitt.modals;

/**
 * Created by manishautomatic on 17/05/18.
 */

public class FbAuthTemplate {


    private String response_code="0";
    private String response_message="";
    private FbLoginResponseTemplate userinformation;


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

    public FbLoginResponseTemplate getUserinformation() {
        return userinformation;
    }

    public void setUserinformation(FbLoginResponseTemplate userinformation) {
        this.userinformation = userinformation;
    }
}
