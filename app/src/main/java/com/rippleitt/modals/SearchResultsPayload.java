package com.rippleitt.modals;

/**
 * Created by pc on password/17/2018.
 */

public class SearchResultsPayload {

    private String response_code="";
    private String response_message="";
    private int totalpages=0;
    private int currentpage=0;
    private int totalcount=0;
    private ListingTemplate[] data;


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

    public int getTotalpages() {
        return totalpages;
    }

    public void setTotalpages(int totalpages) {
        this.totalpages = totalpages;
    }

    public int getCurrentpage() {
        return currentpage;
    }

    public void setCurrentpage(int currentpage) {
        this.currentpage = currentpage;
    }

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    public ListingTemplate[] getData() {
        return data;
    }

    public void setData(ListingTemplate[] data) {
        this.data = data;
    }




}
