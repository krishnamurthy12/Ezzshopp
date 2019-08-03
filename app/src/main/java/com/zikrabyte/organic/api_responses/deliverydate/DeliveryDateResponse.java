
package com.zikrabyte.organic.api_responses.deliverydate;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryDateResponse {

    @SerializedName("delivery_days_message")
    @Expose
    private String deliveryDaysMessage;
    @SerializedName("delivery_days")
    @Expose
    private List<DeliveryDay> deliveryDays = null;
    @SerializedName("delivery_time_from")
    @Expose
    private String deliveryTimeFrom;
    @SerializedName("delivery_time_to")
    @Expose
    private String deliveryTimeTo;
    @SerializedName("Responsecode")
    @Expose
    private String responsecode;

    public String getDeliveryDaysMessage() {
        return deliveryDaysMessage;
    }

    public void setDeliveryDaysMessage(String deliveryDaysMessage) {
        this.deliveryDaysMessage = deliveryDaysMessage;
    }

    public List<DeliveryDay> getDeliveryDays() {
        return deliveryDays;
    }

    public void setDeliveryDays(List<DeliveryDay> deliveryDays) {
        this.deliveryDays = deliveryDays;
    }

    public String getDeliveryTimeFrom() {
        return deliveryTimeFrom;
    }

    public void setDeliveryTimeFrom(String deliveryTimeFrom) {
        this.deliveryTimeFrom = deliveryTimeFrom;
    }

    public String getDeliveryTimeTo() {
        return deliveryTimeTo;
    }

    public void setDeliveryTimeTo(String deliveryTimeTo) {
        this.deliveryTimeTo = deliveryTimeTo;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

}
