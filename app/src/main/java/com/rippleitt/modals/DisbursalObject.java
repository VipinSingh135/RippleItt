package com.rippleitt.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DisbursalObject {

    @SerializedName("disbursal_id")
    @Expose
    private String disbursalId;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("submitted_on")
    @Expose
    private String submittedOn;
    @SerializedName("processed_on")
    @Expose
    private Object processedOn;
    @SerializedName("reference_number")
    @Expose
    private String referenceNumber;

    public String getDisbursalId() {
        return disbursalId;
    }

    public void setDisbursalId(String disbursalId) {
        this.disbursalId = disbursalId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubmittedOn() {
        return submittedOn;
    }

    public void setSubmittedOn(String submittedOn) {
        this.submittedOn = submittedOn;
    }

    public Object getProcessedOn() {
        return processedOn;
    }

    public void setProcessedOn(Object processedOn) {
        this.processedOn = processedOn;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

}
