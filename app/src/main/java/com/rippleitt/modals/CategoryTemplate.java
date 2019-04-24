package com.rippleitt.modals;

/**
 * Created by pc on password/15/2018.
 */

public class CategoryTemplate {

    private String id="";
    private String name="";
    private int isTicked=0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsTicked() {
        return isTicked;
    }

    public void setIsTicked(int isTicked) {
        this.isTicked = isTicked;
    }
}
