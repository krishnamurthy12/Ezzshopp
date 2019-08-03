
package com.zikrabyte.organic.api_responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckUserResponse {

    @SerializedName("check_message")
    @Expose
    private String checkMessage;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("Responsecode")
    @Expose
    private String responsecode;

    public String getCheckMessage() {
        return checkMessage;
    }

    public void setCheckMessage(String checkMessage) {
        this.checkMessage = checkMessage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

}
