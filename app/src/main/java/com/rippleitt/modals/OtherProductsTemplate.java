package com.rippleitt.modals;

/**
 * Created by pc on mail/6/2018.
 */

public class OtherProductsTemplate {

    private String listing_id="";
    private String listing_name="";
    private String listing_type="1";
    private String price="";
    private ListingPhotoTemplate[] listing_photos;

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ListingPhotoTemplate[] getListing_photos() {
        return listing_photos;
    }

    public void setListing_photos(ListingPhotoTemplate[] listing_photos) {
        this.listing_photos = listing_photos;
    }

    public String getListing_type() {
        return listing_type;
    }

    public void setListing_type(String listing_type) {
        this.listing_type = listing_type;
    }
}
