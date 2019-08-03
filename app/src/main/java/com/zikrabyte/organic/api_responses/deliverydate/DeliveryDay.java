
package com.zikrabyte.organic.api_responses.deliverydate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryDay {

    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("value")
    @Expose
    private String value;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
