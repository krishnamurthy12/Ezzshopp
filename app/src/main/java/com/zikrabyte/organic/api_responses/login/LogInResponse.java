
package com.zikrabyte.organic.api_responses.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogInResponse {

    @SerializedName("login_message")
    @Expose
    private String loginMessage;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("referral_code")
    @Expose
    private String referralCode;
    @SerializedName("applied_referral_code")
    @Expose
    private String appliedReferralCode;
    @SerializedName("auth_key")
    @Expose
    private String authKey;
    @SerializedName("Responsecode")
    @Expose
    private String responsecode;

    public String getLoginMessage() {
        return loginMessage;
    }

    public void setLoginMessage(String loginMessage) {
        this.loginMessage = loginMessage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getAppliedReferralCode() {
        return appliedReferralCode;
    }

    public void setAppliedReferralCode(String appliedReferralCode) {
        this.appliedReferralCode = appliedReferralCode;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

}
