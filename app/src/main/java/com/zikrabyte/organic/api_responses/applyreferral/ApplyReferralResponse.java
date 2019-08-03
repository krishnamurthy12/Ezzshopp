
package com.zikrabyte.organic.api_responses.applyreferral;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApplyReferralResponse {

    @SerializedName("Referral_message")
    @Expose
    private String referralMessage;
    @SerializedName("Referral Code")
    @Expose
    private String referralCode;
    @SerializedName("Responsecode")
    @Expose
    private String responsecode;

    public String getReferralMessage() {
        return referralMessage;
    }

    public void setReferralMessage(String referralMessage) {
        this.referralMessage = referralMessage;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

}
