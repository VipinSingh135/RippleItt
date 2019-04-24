package com.rippleitt.modals;

/**
 * Created by manishautomatic on 19/05/18.
 */

public class CardAccountsTemplate {

    private boolean active=false;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String created_at="";
    private String id="";



}
