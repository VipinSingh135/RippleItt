package com.rippleitt.modals;

/**
 * Created by manishautomatic on 11/06/17.
 */
public class EditProfileSyncResponseTemplate{

    private String response_code="";
    private String response_message="";
    private String user_id="";
        private EditProfileDetailTemplate userinformation;


    public EditProfileDetailTemplate getUserinformation() {
        return userinformation;
    }

    public void setUserinformation(EditProfileDetailTemplate userinformation) {
        this.userinformation = userinformation;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

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
}
