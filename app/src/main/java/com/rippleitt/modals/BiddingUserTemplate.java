package com.rippleitt.modals;

/**
 * Created by pc on password/17/2018.
 */

public class BiddingUserTemplate {


    private String fname="";
    private String lname="";
    private String userid="";
    private String image="";
    private String rating="";


    public String getFullName(){
        String suffix="";
        if(lname.toCharArray().length>1){
            suffix =Character.toString(lname.toCharArray()[0]);
        }else{
            suffix=lname;
        }
        return fname+" "+suffix;
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImage() {
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
}
