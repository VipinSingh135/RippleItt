package com.rippleitt.modals;

/**
 * Created by pc on mail/6/2018.
 */

public class ReferralEntity {


    private String user_id="";
    private String fname="";
    private String lname="";
    private String email="";
    private String mobilenumber="";
    private String image="";
    private String is_external="0";


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getImage() {
        if(image==null)image="";
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIs_external() {
        return is_external;
    }

    public void setIs_external(String is_external) {
        this.is_external = is_external;
    }

    public String getFullName(){
        return fname+" "+lname;
    }
}
