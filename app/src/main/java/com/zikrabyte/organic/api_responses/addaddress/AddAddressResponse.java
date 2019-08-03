
package com.zikrabyte.organic.api_responses.addaddress;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddAddressResponse {

    @SerializedName("add_address_message")
    @Expose
    private String addAddressMessage;
    @SerializedName("response")
    @Expose
    private String response;

    public String getAddAddressMessage() {
        return addAddressMessage;
    }

    public void setAddAddressMessage(String addAddressMessage) {
        this.addAddressMessage = addAddressMessage;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

}
