
package com.zikrabyte.organic.api_responses.sendotp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendOTPResponse {

    @SerializedName("get_otp_message")
    @Expose
    private String getOtpMessage;
    @SerializedName("otp")
    @Expose
    private Integer otp;
    @SerializedName("Responsecode")
    @Expose
    private String responsecode;

    public String getGetOtpMessage() {
        return getOtpMessage;
    }

    public void setGetOtpMessage(String getOtpMessage) {
        this.getOtpMessage = getOtpMessage;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

}
