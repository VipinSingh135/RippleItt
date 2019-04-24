package com.rippleitt.modals;

import org.json.JSONException;
import org.json.JSONObject;

public class PojoFCMPushData {


    private String title;
    private String body;

    private PojoFCMPushDataResp resp;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public PojoFCMPushDataResp getResp() {
        return resp;
    }

    public void setResp(PojoFCMPushDataResp resp) {
        this.resp = resp;
    }


}
