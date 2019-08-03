
package com.zikrabyte.organic.api_responses.removefromcart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RemoveFromCartResponse {

    @SerializedName("remove_cart_message")
    @Expose
    private String removeCartMessage;
    @SerializedName("Responsecode")
    @Expose
    private String responsecode;

    public String getRemoveCartMessage() {
        return removeCartMessage;
    }

    public void setRemoveCartMessage(String removeCartMessage) {
        this.removeCartMessage = removeCartMessage;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

}
