
package com.zikrabyte.organic.api_responses.verifyotp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyOTPResponse {

    @SerializedName("verify_otp_message")
    @Expose
    private String verifyOtpMessage;
    @SerializedName("Responsecode")
    @Expose
    private String responsecode;

    public String getVerifyOtpMessage() {
        return verifyOtpMessage;
    }

    public void setVerifyOtpMessage(String verifyOtpMessage) {
        this.verifyOtpMessage = verifyOtpMessage;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

}
