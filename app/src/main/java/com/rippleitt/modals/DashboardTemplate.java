package com.rippleitt.modals;

/**
 * Created by manishautomatic on 19/05/18.
 */

public class DashboardTemplate {

    private String            response_code;
    private String        response_message;
    private String totalordermade;

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

    public String getTotalordermade() {
        if(totalordermade==null)totalordermade="";
        return totalordermade;
    }

    public void setTotalordermade(String totalordermade) {
        this.totalordermade = totalordermade;
    }

    public String getTotalreferralmade() {
        if(totalreferralmade==null)totalreferralmade="";
        return totalreferralmade;
    }

    public void setTotalreferralmade(String totalreferralmade) {
        this.totalreferralmade = totalreferralmade;
    }

    public String getTotalreferralmade7days() {
        if(totalreferralmade7days==null)totalreferralmade7days="";
        return totalreferralmade7days;
    }

    public void setTotalreferralmade7days(String totalreferralmade7days) {
        this.totalreferralmade7days = totalreferralmade7days;
    }

    public String getRewardsearned() {
        if(rewardsearned==null || rewardsearned.equalsIgnoreCase(""))rewardsearned="0";
        return rewardsearned;
    }

    public void setRewardsearned(String rewardsearned) {
        this.rewardsearned = rewardsearned;
    }

    public String getTotalviewedproducts() {
        if(totalviewedproducts==null)totalviewedproducts="";
        return totalviewedproducts;
    }

    public void setTotalviewedproducts(String totalviewedproducts) {
        this.totalviewedproducts = totalviewedproducts;
    }

    public String getTotalwishlistadded() {
        if(totalwishlistadded==null)totalwishlistadded="";
        return totalwishlistadded;
    }

    public void setTotalwishlistadded(String totalwishlistadded) {
        this.totalwishlistadded = totalwishlistadded;
    }

    public String getTotalorderreceived() {
        if(totalorderreceived==null)totalorderreceived="";
        return totalorderreceived;
    }

    public void setTotalorderreceived(String totalorderreceived) {
        this.totalorderreceived = totalorderreceived;
    }

    public String getTotalorderreceived7days() {
        if(totalorderreceived7days==null)totalorderreceived="";
        return totalorderreceived7days;
    }

    public void setTotalorderreceived7days(String totalorderreceived7days) {
        this.totalorderreceived7days = totalorderreceived7days;
    }

    public String getTotalpaymentreceived() {
        if(totalpaymentreceived==null)totalpaymentreceived="";
        return totalpaymentreceived;
    }

    public void setTotalpaymentreceived(String totalpaymentreceived) {
        this.totalpaymentreceived = totalpaymentreceived;
    }

    public String getTotalbuyers() {
        if(totalbuyers==null)totalbuyers="0";
        return totalbuyers;
    }

    public void setTotalbuyers(String totalbuyers) {
        this.totalbuyers = totalbuyers;
    }

    public String getWalletbalance() {
        if(walletbalance==null)walletbalance="0";
        return walletbalance;
    }

    public void setWalletbalance(String walletbalance) {
        this.walletbalance = walletbalance;
    }

    private String totalreferralmade;
    private String         totalreferralmade7days;
    private String rewardsearned;
    private String totalviewedproducts;
    private String totalwishlistadded;
    private String totalorderreceived;
    private String totalorderreceived7days;
    private String totalpaymentreceived;
    private String totalbuyers;
    private String walletbalance;





}
