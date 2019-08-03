
package com.zikrabyte.organic.api_responses.removeaddress;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RemoveAddressResponse {

    @SerializedName("delete_address_message")
    @Expose
    private String deleteAddressMessage;
    @SerializedName("Responsecode")
    @Expose
    private String responsecode;

    public String getDeleteAddressMessage() {
        return deleteAddressMessage;
    }

    public void setDeleteAddressMessage(String deleteAddressMessage) {
        this.deleteAddressMessage = deleteAddressMessage;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

}
