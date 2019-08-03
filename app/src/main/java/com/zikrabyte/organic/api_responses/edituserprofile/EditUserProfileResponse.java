
package com.zikrabyte.organic.api_responses.edituserprofile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditUserProfileResponse {

    @SerializedName("profile_message")
    @Expose
    private String profileMessage;
    @SerializedName("Responsecode")
    @Expose
    private String responsecode;

    public String getProfileMessage() {
        return profileMessage;
    }

    public void setProfileMessage(String profileMessage) {
        this.profileMessage = profileMessage;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

}
