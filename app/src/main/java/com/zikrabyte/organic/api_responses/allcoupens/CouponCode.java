
package com.zikrabyte.organic.api_responses.allcoupens;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CouponCode {
    public CouponCode(String couponCode, String couponAmt, String couponDescription, String couponType) {
        this.couponCode = couponCode;
        this.couponAmt = couponAmt;
        this.couponDescription = couponDescription;
        this.couponType = couponType;
    }

    @SerializedName("coupon_code")
    @Expose
    private String couponCode;
    @SerializedName("coupon_amt")
    @Expose
    private String couponAmt;
    @SerializedName("coupon_description")
    @Expose
    private String couponDescription;
    @SerializedName("coupon_type")
    @Expose
    private String couponType;

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponAmt() {
        return couponAmt;
    }

    public void setCouponAmt(String couponAmt) {
        this.couponAmt = couponAmt;
    }

    public String getCouponDescription() {
        return couponDescription;
    }

    public void setCouponDescription(String couponDescription) {
        this.couponDescription = couponDescription;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

}
