package com.rippleitt.modals;

/**
 * Created by pc on password/17/2018.
 */

public class ContactSharingTemplate {
    private boolean islabel=false;
    private String userid;
    private String name;
    private String lastname;
    private String image;
    private int share=0;
    private String title="";
    private int type=1;
    private int is_external=-1;


    public int getIs_external() {
        return is_external;
    }

    public void setIs_external(int is_external) {
        this.is_external = is_external;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public boolean isIslabel() {
        return islabel;
    }

    public void setIslabel(boolean islabel) {
        this.islabel = islabel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
