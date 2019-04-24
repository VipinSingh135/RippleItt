package com.rippleitt.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletOrderDetail {

    @SerializedName("listing_name")
    @Expose
    private String listingName;
    @SerializedName("listing_image")
    @Expose
    private String listingImage;
    @SerializedName("listing_id")
    @Expose
    private String listingId;
    @SerializedName("order_value")
    @Expose
    private String orderValue;
    @SerializedName("ripple_fee")
    @Expose
    private String rippleFee;
    @SerializedName("referral_fee")
    @Expose
    private String referralFee;
    @SerializedName("amount")
    @Expose
    private String amount;

    public String getListingName() {
        return listingName;
    }

    public void setListingName(String listingName) {
        this.listingName = listingName;
    }

    public String getListingImage() {
        return listingImage;
    }

    public void setListingImage(String listingImage) {
        this.listingImage = listingImage;
    }

    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public String getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(String orderValue) {
        this.orderValue = orderValue;
    }

    public String getRippleFee() {
        return rippleFee;
    }

    public void setRippleFee(String rippleFee) {
        this.rippleFee = rippleFee;
    }

    public String getReferralFee() {
        return referralFee;
    }

    public void setReferralFee(String referralFee) {
        this.referralFee = referralFee;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
