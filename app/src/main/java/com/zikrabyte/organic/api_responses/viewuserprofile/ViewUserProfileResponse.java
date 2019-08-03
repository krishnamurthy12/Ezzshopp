
package com.zikrabyte.organic.api_responses.viewuserprofile;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ViewUserProfileResponse {

    @SerializedName("response")
    @Expose
    private List<Response> response = null;
    @SerializedName("profile_message")
    @Expose
    private String profileMessage;
    @SerializedName("Responsecode")
    @Expose
    private String responsecode;

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }

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
