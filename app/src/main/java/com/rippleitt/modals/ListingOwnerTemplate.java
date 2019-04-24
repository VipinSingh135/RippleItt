package com.rippleitt.modals;

/**
 * Created by pc on password/17/2018.
 */

public class ListingOwnerTemplate {



    private String fname;
    private String lname;
    private String email;
    private String mobilenumber;
    private String image;
    private String rating="0";
    private UserReviewTemplate[] reviews;
    private String user_id="";
    private String fcm_token="";
    private String device_type="";


    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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


    public String getRating() {
        if(rating==null)rating="0";
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public UserReviewTemplate[] getReviews() {
        return reviews;
    }

    public void setReviews(UserReviewTemplate[] reviews) {
        //if(reviews==null)reviews={};
        this.reviews = reviews;
    }

    public String getFullName(){

        String suffix="";
        if(lname.toCharArray()==null ){
            suffix="";
        }else if(lname.toCharArray().length>1){
           suffix =Character.toString(lname.toCharArray()[0]);
        }else{
            suffix=lname;
        }
        return fname+" "+suffix;
    }
}
