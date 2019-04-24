package com.rippleitt.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyDisputesData {

    @SerializedName("dispute_id")
    @Expose
    private String disputeId;
    @SerializedName("submitted_on")
    @Expose
    private String submittedOn;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("order_details")
    @Expose
    private ListingTemplate orderDetails;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("assigned_to")
    @Expose
    private String assignedTo;
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("assigned_on")
    @Expose
    private String assignedOn;
    @SerializedName("closed_on")
    @Expose
    private String closedOn;

    public String getDisputeId() {
        return disputeId;
    }

    public void setDisputeId(String disputeId) {
        this.disputeId = disputeId;
    }

    public String getSubmittedOn() {
        return submittedOn;
    }

    public void setSubmittedOn(String submittedOn) {
        this.submittedOn = submittedOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ListingTemplate getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(ListingTemplate orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getAssignedOn() {
        return assignedOn;
    }

    public void setAssignedOn(String assignedOn) {
        this.assignedOn = assignedOn;
    }

    public String getClosedOn() {
        return closedOn;
    }

    public void setClosedOn(String closedOn) {
        this.closedOn = closedOn;
    }
}
