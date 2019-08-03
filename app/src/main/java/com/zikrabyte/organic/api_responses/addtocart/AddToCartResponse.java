
package com.zikrabyte.organic.api_responses.addtocart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddToCartResponse {

    @SerializedName("update_cart_message")
    @Expose
    private String updateCartMessage;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("amt")
    @Expose
    private String amt;
    @SerializedName("total_amt")
    @Expose
    private double totalAmt;
    @SerializedName("Responsecode")
    @Expose
    private String responsecode;

    @SerializedName("response_message")
    @Expose
    private String responseMessage;

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getUpdateCartMessage() {
        return updateCartMessage;
    }

    public void setUpdateCartMessage(String updateCartMessage) {
        this.updateCartMessage = updateCartMessage;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

}
