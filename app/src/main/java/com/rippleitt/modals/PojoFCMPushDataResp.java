package com.rippleitt.modals;

import org.json.JSONException;
import org.json.JSONObject;

public class PojoFCMPushDataResp {

    private String id="";
    private Integer is_deleted=0;
    private String user_id="";
    private String username="";
    private String icon="";
    private String type="";
    private String message="";
    private String device_token="";
    private String device="";
    private String user_image="";
    private Integer status=0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(Integer is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String toJSON(){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("id", getId());
            jsonObject.put("user_id", getUser_id());
            jsonObject.put("username", getUsername());
            jsonObject.put("icon", getIcon());
            jsonObject.put("type", getType());
            jsonObject.put("message", getMessage());
            jsonObject.put("device_token", getDevice_token());
            jsonObject.put("device", getDevice());
            jsonObject.put("user_image", getUser_image());
            jsonObject.put("status", getStatus());

            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

    }
}
