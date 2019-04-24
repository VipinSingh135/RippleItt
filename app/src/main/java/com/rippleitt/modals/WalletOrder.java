package com.rippleitt.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WalletOrder {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("details")
    @Expose
    private List<WalletOrderDetail> details = null;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<WalletOrderDetail> getDetails() {
        return details;
    }

    public void setDetails(List<WalletOrderDetail> details) {
        this.details = details;
    }
}
