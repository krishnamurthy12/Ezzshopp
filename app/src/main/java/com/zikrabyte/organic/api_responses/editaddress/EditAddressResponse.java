
package com.zikrabyte.organic.api_responses.editaddress;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditAddressResponse {

    @SerializedName("edit_address_message")
    @Expose
    private String editAddressMessage;
    @SerializedName("response")
    @Expose
    private String response;

    public String getEditAddressMessage() {
        return editAddressMessage;
    }

    public void setEditAddressMessage(String editAddressMessage) {
        this.editAddressMessage = editAddressMessage;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

}
