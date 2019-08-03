
package com.zikrabyte.organic.api_responses.orderhistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PromoDetails {

    @SerializedName("promo_code")
    @Expose
    private String promoCode;
    @SerializedName("sub_total")
    @Expose
    private String subTotal;
    @SerializedName("gst")
    @Expose
    private String gst;
    @SerializedName("delivery_charges")
    @Expose
    private String deliveryCharges;
    @SerializedName("grand_total")
    @Expose
    private String grandTotal;

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(String deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

}
