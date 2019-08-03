
package com.zikrabyte.organic.api_responses.allcoupens;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllCoupensResponse {

    @SerializedName("coupons_message")
    @Expose
    private String couponsMessage;
    @SerializedName("referral_code")
    @Expose
    private String referralCode;
    @SerializedName("referral_amt")
    @Expose
    private String referralAmt;
    @SerializedName("referral_type")
    @Expose
    private String referralType;
    @SerializedName("earned_reward_code")
    @Expose
    private String earnedRewardCode;
    @SerializedName("reward_amt")
    @Expose
    private String rewardAmt;

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

    @SerializedName("reward_discount_type")
    @Expose

    private String rewardDiscountType;
    @SerializedName("coupon_codes")
    @Expose
    private List<CouponCode> couponCodes = null;
    @SerializedName("Responsecode")
    @Expose
    private String responsecode;


    public String getCouponsMessage() {
        return couponsMessage;
    }

    public void setCouponsMessage(String couponsMessage) {
        this.couponsMessage = couponsMessage;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getReferralAmt() {
        return referralAmt;
    }

    public void setReferralAmt(String referralAmt) {
        this.referralAmt = referralAmt;
    }

    public String getReferralType() {
        return referralType;
    }

    public void setReferralType(String referralType) {
        this.referralType = referralType;
    }

    public String getEarnedRewardCode() {
        return earnedRewardCode;
    }

    public void setEarnedRewardCode(String earnedRewardCode) {
        this.earnedRewardCode = earnedRewardCode;
    }

    public String getRewardAmt() {
        return rewardAmt;
    }

    public void setRewardAmt(String rewardAmt) {
        this.rewardAmt = rewardAmt;
    }

    public String getRewardDiscountType() {
        return rewardDiscountType;
    }

    public void setRewardDiscountType(String rewardDiscountType) {
        this.rewardDiscountType = rewardDiscountType;
    }

    public List<CouponCode> getCouponCodes() {
        return couponCodes;
    }

    public void setCouponCodes(List<CouponCode> couponCodes) {
        this.couponCodes = couponCodes;
    }
}
