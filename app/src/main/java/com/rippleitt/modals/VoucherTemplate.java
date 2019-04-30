package com.rippleitt.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VoucherTemplate {

    @SerializedName("voucher_id")
    @Expose
    private String voucherId;
    @SerializedName("expiry")
    @Expose
    private String expiry;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("availed")
    @Expose
    private String availed;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("get_amount")
    @Expose
    private String get_amount;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("is_deleted")
    @Expose
    private String isDeleted;
    @SerializedName("is_expired")
    @Expose
    private String isExpired;
    @SerializedName("has_expiry")
    @Expose
    private String hasExpiry;
    @SerializedName("restricted_days")
    @Expose
    private String restrictedDays;
    @SerializedName("is_suspended")
    @Expose
    private String isSuspended;
    @SerializedName("available_quantity")
    @Expose
    private String availableQuantity;
    @SerializedName("deleted_on")
    @Expose
    private Object deletedOn;
    @SerializedName("suspended_on")
    @Expose
    private Object suspendedOn;
    @SerializedName("listing_ids")
    @Expose
    private String asignedListingId;

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getHasExpiry() {
        return hasExpiry;
    }

    public void setHasExpiry(String hasExpiry) {
        this.hasExpiry = hasExpiry;
    }

    public String getRestrictedDays() {
        return restrictedDays;
    }

    public void setRestrictedDays(String restrictedDays) {
        this.restrictedDays = restrictedDays;
    }

    public String getIsSuspended() {
        return isSuspended;
    }

    public void setIsSuspended(String isSuspended) {
        this.isSuspended = isSuspended;
    }

    public String getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(String availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Object getDeletedOn() {
        return deletedOn;
    }

    public void setDeletedOn(Object deletedOn) {
        this.deletedOn = deletedOn;
    }

    public Object getSuspendedOn() {
        return suspendedOn;
    }

    public void setSuspendedOn(Object suspendedOn) {
        this.suspendedOn = suspendedOn;
    }

    public String getAsignedListingId() {
        return asignedListingId;
    }

    public void setAsignedListingId(String asignedListingId) {
        this.asignedListingId = asignedListingId;
    }

    public String getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(String isExpired) {
        this.isExpired = isExpired;
    }

    public String getGet_amount() {
        return get_amount;
    }

    public void setGet_amount(String get_amount) {
        this.get_amount = get_amount;
    }

    public String getAvailed() {
        return availed;
    }

    public void setAvailed(String availed) {
        this.availed = availed;
    }
}
