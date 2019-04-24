package com.rippleitt.modals;

/**
 * Created by pc on password/16/2018.
 */

public class ListingTemplate {


    private String listing_id="";
    private CategoryTemplate category;

    public BidTemplate[] getBids() {
        return bids;
    }

    public void setBids(BidTemplate[] bids) {
        this.bids = bids;
    }
    private String listing_name="";



    private CategoryTemplate subcategory;
    private ListingLocationTemplate location;
    private String listing_price="";
    private String productname="";
    private String listing_description="";
    private String is_live="";
    private String refer_amount="";
    private String is_new="";
    private String has_voucher="";
    private String has_referral="";
    private VoucherTemplate voucher_details;

    private String is_disabled="";
    private String is_self_listing="0";
    private BidTemplate[] bids={};
    private String listing_flag_name="";
    private String listing_flag="";
    private int is_accepted=0;
    private int isaddedtowishlist=0;
    private String listing_type="1";
    private String my_bid_amount="";
    private String my_bid_quantity="";
    private String is_mybids_accepted="0";
    private String review_submitted="0";

    private String payment_mode="";
    private String quantity="";
    private String units="";
    private String direct_buy="";
    private String is_paid="0";
    private String order_value="0";
    private String voucher_value="0";
    private String order_net="0";
//"order_value":"50","voucher_value":"5","order_net":"43.75"
    private ListingPhotoTemplate[] listing_photos;
    private ListingOwnerTemplate userinformation;
    private ReferralEntity[] recipient_array;

    private OtherProductsTemplate[] otherproducts;
    private ReferralEntity[] sender_array;
    private ListingOwnerTemplate buyerarray;
    private ListingOwnerTemplate sellerarray;
    private String is_buying="1";
    private String bid_price="";
    private String order_id="";
    private String shipping_type="1";
    private String[] postal_codes;

    private String ripple_fee  ="";// - this is the amount kept by rippleitt as commission
    private String referral_fee = "";
    private String gateway_fee = "";

    public String getIs_paid() {
        return is_paid;
    }

    public void setIs_paid(String is_paid) {
        this.is_paid = is_paid;
    }

    public String getOrder_value() {
        return order_value;
    }

    public void setOrder_value(String order_value) {
        this.order_value = order_value;
    }

    public String getVoucher_value() {
        return voucher_value;
    }

    public void setVoucher_value(String voucher_value) {
        this.voucher_value = voucher_value;
    }

    public String getOrder_net() {
        return order_net;
    }

    public void setOrder_net(String order_net) {
        this.order_net = order_net;
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

    public String getMy_bid_quantity() {
        if(my_bid_quantity==null || my_bid_quantity.equalsIgnoreCase(""))
            my_bid_quantity="0";
        return my_bid_quantity;
    }

    public void setMy_bid_quantity(String my_bid_quantity) {
        this.my_bid_quantity = my_bid_quantity;
    }

    public String getRipple_fee() {
        return ripple_fee;
    }

    public void setRipple_fee(String ripple_fee) {
        this.ripple_fee = ripple_fee;
    }

    public String getReferral_fee() {
        return referral_fee;
    }

    public void setReferral_fee(String referral_fee) {
        this.referral_fee = referral_fee;
    }

    public String getDirect_buy() {
        return direct_buy;
    }

    public void setDirect_buy(String direct_buy) {
        this.direct_buy = direct_buy;
    }

    public ListingOwnerTemplate getBuyerarray() {
        return buyerarray;
    }

    public void setBuyerarray(ListingOwnerTemplate buyerarray) {
        this.buyerarray = buyerarray;
    }


    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getQuantity() {
        if(quantity==null)
            quantity="0";
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

    public ListingOwnerTemplate getSellerarray() {
        return sellerarray;
    }

    public void setSellerarray(ListingOwnerTemplate sellerarray) {
        this.sellerarray = sellerarray;
    }

    public String getIs_buying() {
        return is_buying;
    }

    public String getReview_submitted() {
        return review_submitted;
    }
    public void setReview_submitted(String review_submitted) {
        this.review_submitted = review_submitted;
    }
    public void setIs_buying(String is_buying) {
        this.is_buying = is_buying;
    }
    public String getBid_price() {
        return bid_price;
    }
    public void setBid_price(String bid_price) {
        this.bid_price = bid_price;
    }
    public String getOrder_id() {
        return order_id;
    }
    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
    public OtherProductsTemplate[] getOtherproducts() {
        return otherproducts;
    }

    public void setOtherproducts(OtherProductsTemplate[] otherproducts) {
        this.otherproducts = otherproducts;
    }
    public ReferralEntity[] getSender_array() {
        return sender_array;
    }

    public void setSender_array(ReferralEntity[] sender_array) {
        this.sender_array = sender_array;
    }

    public ReferralEntity[] getRecipient_array() {
        return recipient_array;
    }

    public void setRecipient_array(ReferralEntity[] recipient_array) {
        this.recipient_array = recipient_array;
    }

    public String getIs_new() {
        return is_new;
    }

    public void setIs_new(String is_new) {
        this.is_new = is_new;
    }

    public String getListing_name() {
        return listing_name;
    }

    public void setListing_name(String listing_name) {
        this.listing_name = listing_name;
    }

    public ListingOwnerTemplate getUserinformation() {
        return userinformation;
    }

    public void setUserinformation(ListingOwnerTemplate userinformation) {
        this.userinformation = userinformation;
    }

    public String getIs_mybids_accepted() {
        return is_mybids_accepted;
    }

    public void setIs_mybids_accepted(String is_mybids_accepted) {
        this.is_mybids_accepted = is_mybids_accepted;
    }

    public String getIs_self_listing() {
        return is_self_listing;
    }

    public void setIs_self_listing(String is_self_listing) {
        this.is_self_listing = is_self_listing;
    }

    public String getListing_flag_name() {
        return listing_flag_name;
    }

    public void setListing_flag_name(String listing_flag_name) {
        this.listing_flag_name = listing_flag_name;
    }

    public String getListing_flag() {
        return listing_flag;
    }

    public void setListing_flag(String listing_flag) {
        this.listing_flag = listing_flag;
    }

    public int getIs_accepted() {
        return is_accepted;
    }

    public void setIs_accepted(int is_accepted) {
        this.is_accepted = is_accepted;
    }

    public int getIsaddedtowishlist() {
        return isaddedtowishlist;
    }

    public void setIsaddedtowishlist(int isaddedtowishlist) {
        this.isaddedtowishlist = isaddedtowishlist;
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

    public String getIs_disabled() {
        return is_disabled;
    }

    public void setIs_disabled(String is_disabled) {
        this.is_disabled = is_disabled;
    }

    public ListingPhotoTemplate[] getListing_photos() {
        return listing_photos;
    }

    public void setListing_photos(ListingPhotoTemplate[] listing_photos) {
        this.listing_photos = listing_photos;
    }

    public String getListing_id() {
        return listing_id;
    }

    public void setListing_id(String listing_id) {
        this.listing_id = listing_id;
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

    public String getListing_price() {
        return ""+listing_price;
    }

    public void setListing_price(String listing_price) {
        this.listing_price = listing_price;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
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

    public String getRefer_amount() {
        if(refer_amount.equalsIgnoreCase("")){
            return "$0";
        }
        return "$"+refer_amount;
    }

    public void setRefer_amount(String refer_amount) {
        this.refer_amount = refer_amount;
    }

    public String getHas_voucher() {
        return has_voucher;
    }

    public void setHas_voucher(String has_voucher) {
        this.has_voucher = has_voucher;
    }

    public VoucherTemplate getVoucher_details() {
        return voucher_details;
    }

    public void setVoucher_details(VoucherTemplate voucher_details) {
        this.voucher_details = voucher_details;
    }

    public String getHas_referral() {
        return has_referral;
    }

    public void setHas_referral(String has_referral) {
        this.has_referral = has_referral;
    }

    public String getGateway_fee() {
        return gateway_fee;
    }

    public void setGateway_fee(String gateway_fee) {
        this.gateway_fee = gateway_fee;
    }
}
