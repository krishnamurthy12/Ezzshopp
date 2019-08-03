
package com.zikrabyte.organic.api_responses.coupenapplied;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoupenAppliedResponse {

    @SerializedName("view_cart_message")
    @Expose
    private String viewCartMessage;
    @SerializedName("Responsecode")
    @Expose
    private String responsecode;
    @SerializedName("coupon_message")
    @Expose
    private String couponMessage;
    @SerializedName("sub_total")
    @Expose
    private Double subTotal;
    @SerializedName("gst")
    @Expose
    private Double gst;
    @SerializedName("delivery_charges")
    @Expose
    private String deliveryCharges;
    @SerializedName("grand_total")
    @Expose
    private Double grandTotal;
    @SerializedName("applied_id")
    @Expose
    private String appliedId;
    @SerializedName("response")
    @Expose
    private List<Response> response = null;

    public String getViewCartMessage() {
        return viewCartMessage;
    }

    public void setViewCartMessage(String viewCartMessage) {
        this.viewCartMessage = viewCartMessage;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

    public String getCouponMessage() {
        return couponMessage;
    }

    public void setCouponMessage(String couponMessage) {
        this.couponMessage = couponMessage;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getGst() {
        return gst;
    }

    public void setGst(Double gst) {
        this.gst = gst;
    }

    public String getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(String deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public Double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getAppliedId() {
        return appliedId;
    }

    public void setAppliedId(String appliedId) {
        this.appliedId = appliedId;
    }

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }

}
