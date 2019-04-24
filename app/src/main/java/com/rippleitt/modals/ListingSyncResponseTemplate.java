package com.rippleitt.modals;

/**
 * Created by manishautomatic on 11/06/17.
 */
public class ListingSyncResponseTemplate {

    private String response_code="";
    private String response_message="";
    private String is_card_available="";
    private String draft_flag="";
    private String listing_id="";
    private String id="";


    public String getIs_card_available() {
        return is_card_available;
    }

    public void setIs_card_available(String is_card_available) {
        this.is_card_available = is_card_available;
    }

    public String getDraft_flag() {
        return draft_flag;
    }

    public void setDraft_flag(String draft_flag) {
        this.draft_flag = draft_flag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getLead_id() {
        return id;
    }

    public void setLead_id(String lead_id) {
        this.id = lead_id;
    }

    public String getListing_id() {
        return listing_id;
    }

    public void setListing_id(String listing_id) {
        this.listing_id = listing_id;
    }
}
