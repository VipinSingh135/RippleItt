package com.rippleitt.modals;

/**
 * Created by pc on password/17/2018.
 */

public class BidTemplate {

    private String bidid;
    private String bidprice;
    private String quantity="";
    private String minutes="";

    private String hideAccept="0";
    private String is_accepted="0";
    private BiddingUserTemplate userinformation;
    private String is_my_bid="0";

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getIs_my_bid() {
        return is_my_bid;
    }

    public void setIs_my_bid(String is_my_bid) {
        this.is_my_bid = is_my_bid;
    }

    public String getBidid() {
        return bidid;
    }

    public void setBidid(String bidid) {
        this.bidid = bidid;
    }

    public String getBidprice() {
        return bidprice;
    }

    public void setBidprice(String bidprice) {
        this.bidprice = bidprice;
    }

    public String getIs_accepted() {
        return is_accepted;
    }

    public void setIs_accepted(String is_accepted) {
        this.is_accepted = is_accepted;
    }

    public BiddingUserTemplate getUserinformation() {
        return userinformation;
    }

    public void setUserinformation(BiddingUserTemplate userinformation) {
        this.userinformation = userinformation;
    }

    public String getHideAccept() {
        return hideAccept;
    }

    public void setHideAccept(String hideAccept) {
        this.hideAccept = hideAccept;
    }


    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
