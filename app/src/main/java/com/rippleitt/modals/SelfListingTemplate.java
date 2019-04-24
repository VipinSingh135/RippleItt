package com.rippleitt.modals;

/**
 * Created by pc on password/17/2018.
 */

public class SelfListingTemplate {

    private String listing_id;
    private String listing_name;
    private CategoryTemplate category;
    private CategoryTemplate subcategory;
    private ListingLocationTemplate location;
    private String is_accepted;
    private String listing_price;
    private String listing_rating;
    private String listing_description;
    private String is_live;
    private String is_new;
    private String refer_amount="0";
    private String is_disabled;
    private ListingOwnerTemplate userinformation;
    private int isaddedtowishlist;
    private BidTemplate[] bids;
    private String listing_type="";
    private String my_bid_amount="";
    private String payment_mode="";
    private String quantity="";
    private String units="";
    private String shipping_type="1";
    private String[] postal_codes;


    public String getIs_new() {
        return is_new;
    }

    public void setIs_new(String is_new) {
        this.is_new = is_new;
    }

    public String getShipping_type() {
        return shipping_type;
    }

    public void setShipping_type(String shipping_type) {
        this.shipping_type = shipping_type;
    }

    public String[] getPostal_codes() {
        return postal_codes;
    }

    public void setPostal_codes(String[] postal_codes) {
        this.postal_codes = postal_codes;
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

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    private ListingPhotoTemplate[] listing_photos;

    public String getRefer_amount() {
        return refer_amount;
    }

    public void setRefer_amount(String refer_amount) {
        this.refer_amount = refer_amount;
    }

    public String getListing_type() {
        return listing_type;
    }

    public void setListing_type(String listing_type) {
        this.listing_type = listing_type;
    }

    public String getMy_bid_amount() {
        return my_bid_amount;
    }

    public void setMy_bid_amount(String my_bid_amount) {
        this.my_bid_amount = my_bid_amount;
    }

    public String getListing_id() {
        return listing_id;
    }

    public void setListing_id(String listing_id) {
        this.listing_id = listing_id;
    }

    public String getListing_name() {
        return listing_name;
    }

    public void setListing_name(String listing_name) {
        this.listing_name = listing_name;
    }

    public CategoryTemplate getCategory() {
        return category;
    }

    public void setCategory(CategoryTemplate category) {
        this.category = category;
    }

    public CategoryTemplate getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(CategoryTemplate subcategory) {
        this.subcategory = subcategory;
    }

    public ListingLocationTemplate getLocation() {
        return location;
    }

    public void setLocation(ListingLocationTemplate location) {
        this.location = location;
    }

    public String getIs_accepted() {
        return is_accepted;
    }

    public void setIs_accepted(String is_accepted) {
        this.is_accepted = is_accepted;
    }

    public String getListing_price() {
        return listing_price;
    }

    public void setListing_price(String listing_price) {
        this.listing_price = listing_price;
    }

    public String getListing_rating() {
        return listing_rating;
    }

    public void setListing_rating(String listing_rating) {
        this.listing_rating = listing_rating;
    }

    public String getListing_description() {
        return listing_description;
    }

    public void setListing_description(String listing_description) {
        this.listing_description = listing_description;
    }

    public String getIs_live() {
        return is_live;
    }

    public void setIs_live(String is_live) {
        this.is_live = is_live;
    }

    public String getIs_disabled() {
        return is_disabled;
    }

    public void setIs_disabled(String is_disabled) {
        this.is_disabled = is_disabled;
    }

    public ListingOwnerTemplate getUserinformation() {
        return userinformation;
    }

    public void setUserinformation(ListingOwnerTemplate userinformation) {
        this.userinformation = userinformation;
    }

    public int getIsaddedtowishlist() {
        return isaddedtowishlist;
    }

    public void setIsaddedtowishlist(int isaddedtowishlist) {
        this.isaddedtowishlist = isaddedtowishlist;
    }

    public BidTemplate[] getBids() {
        return bids;
    }

    public void setBids(BidTemplate[] bids) {
        this.bids = bids;
    }

    public ListingPhotoTemplate[] getListing_photos() {
        return listing_photos;
    }

    public void setListing_photos(ListingPhotoTemplate[] listing_photos) {
        this.listing_photos = listing_photos;
    }
}
