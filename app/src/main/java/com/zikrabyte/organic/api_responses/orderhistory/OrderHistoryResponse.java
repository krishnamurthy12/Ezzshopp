
package com.zikrabyte.organic.api_responses.orderhistory;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderHistoryResponse {

    @SerializedName("order_message")
    @Expose
    private String orderMessage;
    @SerializedName("Responsecode")
    @Expose
    private String responsecode;
    @SerializedName("order_details")
    @Expose
    private List<OrderDetail> orderDetails = null;

    public String getOrderMessage() {
        return orderMessage;
    }

    public void setOrderMessage(String orderMessage) {
        this.orderMessage = orderMessage;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

}
