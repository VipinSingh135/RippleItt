package com.rippleitt.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostalCodeModal {

    @SerializedName("postcode")
    @Expose
    private String postcode;
    @SerializedName("name")
    @Expose
    private String name;

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
