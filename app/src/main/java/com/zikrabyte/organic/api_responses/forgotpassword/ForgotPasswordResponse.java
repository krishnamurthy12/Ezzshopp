
package com.zikrabyte.organic.api_responses.forgotpassword;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPasswordResponse {

    @SerializedName("forgot_password_message")
    @Expose
    private String forgotPasswordMessage;

    public String getUpdatePasswordMessage() {
        return updatePasswordMessage;
    }

    public void setUpdatePasswordMessage(String updatePasswordMessage) {
        this.updatePasswordMessage = updatePasswordMessage;
    }

    public String getCheckMessage() {
        return checkMessage;
    }

    public void setCheckMessage(String checkMessage) {
        this.checkMessage = checkMessage;
    }

    @SerializedName("check_message")
    @Expose
    private String checkMessage;

    @SerializedName("update_password_message")
    @Expose
    private String updatePasswordMessage;

    @SerializedName("fotp")
    @Expose
    private Integer fotp;
    @SerializedName("Responsecode")
    @Expose
    private String responsecode;

    public String getForgotPasswordMessage() {
        return forgotPasswordMessage;
    }

    public void setForgotPasswordMessage(String forgotPasswordMessage) {
        this.forgotPasswordMessage = forgotPasswordMessage;
    }

    public Integer getFotp() {
        return fotp;
    }

    public void setFotp(Integer fotp) {
        this.fotp = fotp;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

}
