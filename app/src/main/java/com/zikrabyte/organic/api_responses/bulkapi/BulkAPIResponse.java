
package com.zikrabyte.organic.api_responses.bulkapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BulkAPIResponse {

    @SerializedName("add_cart_message")
    @Expose
    private String addCartMessage;
    @SerializedName("Responsecode")
    @Expose
    private String responsecode;

    public String getAddCartMessage() {
        return addCartMessage;
    }

    public void setAddCartMessage(String addCartMessage) {
        this.addCartMessage = addCartMessage;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

}
