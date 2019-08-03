
package com.zikrabyte.organic.api_responses.cancelorder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CancelOrderResponse {

    @SerializedName("cancel_order_message")
    @Expose
    private String cancelOrderMessage;
    @SerializedName("Responsecode")
    @Expose
    private String responsecode;

    public String getCancelOrderMessage() {
        return cancelOrderMessage;
    }

    public void setCancelOrderMessage(String cancelOrderMessage) {
        this.cancelOrderMessage = cancelOrderMessage;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

}
