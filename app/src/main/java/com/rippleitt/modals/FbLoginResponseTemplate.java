package com.rippleitt.modals;

/**
 * Created by manishautomatic on 17/05/18.
 */

public class FbLoginResponseTemplate {


    private String fname="";
    private String lname="";
    private String token="";
    private String email="";
    private String user_id="";
    private String mobilenumber="";
    private String latitude="";
    private String longitude="";
    private String address1="";
    private String address2="";
    private String gender="";
    private String image="";
    private String fnreferalcodeame="";
    private String is_card_available="0";
    private String email_verified="0";
    private String phone_verified="0";


    public String getEmail_verified() {
        if(email_verified==null)email_verified="0";
        return email_verified;
    }

    public void setEmail_verified(String email_verified) {
        this.email_verified = email_verified;
    }

    public String getPhone_verified() {
        if(phone_verified==null)phone_verified="0";
        return phone_verified;
    }

    public void setPhone_verified(String phone_verified) {
        this.phone_verified = phone_verified;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFnreferalcodeame() {
        return fnreferalcodeame;
    }

    public void setFnreferalcodeame(String fnreferalcodeame) {
        this.fnreferalcodeame = fnreferalcodeame;
    }

    public String getIs_card_available() {
        return is_card_available;
    }

    public void setIs_card_available(String is_card_available) {
        this.is_card_available = is_card_available;
    }
}
