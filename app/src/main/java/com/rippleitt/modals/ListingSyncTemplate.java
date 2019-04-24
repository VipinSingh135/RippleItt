package com.rippleitt.modals;

/**
 * Created by manishautomatic on 12/12/17.
 */
public class ListingSyncTemplate {


   private String productname;
    private String category;
    private String sub_category;
    private String description;
    private String price;
    private String Is_drafted;
    private String Longitude;
    private String Latitude;
    private String address;
    private String rewardAmount;
    private String listing_type="1";
    private String listingID;
    private String payment_mode="";
    private String quantity="";
    private String units="";
    private String shippingType="1";
    private String zipCodes="";
    private String direct_buy="";
    private String is_new="";

    private String zipCodesRemoved="";


    public String getDirect_buy() {
        return direct_buy;
    }

    public void setDirect_buy(String direct_buy) {
        this.direct_buy = direct_buy;
    }

    public String getIs_new() {
        return is_new;
    }

    public void setIs_new(String is_new) {
        this.is_new = is_new;
    }

    public String getZipCodesRemoved() {
        return zipCodesRemoved;
    }

    public void setZipCodesRemoved(String zipCodesRemoved) {
        this.zipCodesRemoved = zipCodesRemoved;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getZipCodes() {
        return zipCodes;
    }

    public void setZipCodes(String zipCodes) {
        this.zipCodes = zipCodes;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getListing_type() {
        return listing_type;
    }

    public void setListing_type(String listing_type) {
        this.listing_type = listing_type;
    }

    public String getListingID() {
        return listingID;
    }

    public void setListingID(String listingID) {
        this.listingID = listingID;
    }

    public String getRewardAmount() {
        return rewardAmount;
    }

    public void setRewardAmount(String rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIs_drafted() {
        return Is_drafted;
    }

    public void setIs_drafted(String is_drafted) {
        Is_drafted = is_drafted;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
