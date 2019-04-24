package com.rippleitt.modals;

/**
 * Created by manishautomatic on 12/06/18.
 */

public class PriceRangeTemplate {


    private String range_id="";
    private String title="";
    private int isTicked=0;


    public int getIsTicked() {
        return isTicked;
    }

    public void setIsTicked(int isTicked) {
        this.isTicked = isTicked;
    }

    public String getRange_id() {
        return range_id;
    }

    public void setRange_id(String range_id) {
        this.range_id = range_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
