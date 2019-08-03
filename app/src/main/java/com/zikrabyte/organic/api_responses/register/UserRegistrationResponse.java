
package com.zikrabyte.organic.api_responses.register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRegistrationResponse {

    @SerializedName("register_message")
    @Expose
    private String registerMessage;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("auth_key")
    @Expose
    private String authKey;
    @SerializedName("Responsecode")
    @Expose
    private String responsecode;

    public String getRegisterMessage() {
        return registerMessage;
    }

    public void setRegisterMessage(String registerMessage) {
        this.registerMessage = registerMessage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
