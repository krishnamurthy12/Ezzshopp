
package com.zikrabyte.organic.api_responses.itemdescription;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemDescriptionResponse {

    @SerializedName("products_message")
    @Expose
    private String productsMessage;
    @SerializedName("Responsecode")
    @Expose
    private String responsecode;
    @SerializedName("response")
    @Expose
    private List<Response> response = null;


    public String getProductsMessage() {
        return productsMessage;
    }

    public void setProductsMessage(String productsMessage) {
        this.productsMessage = productsMessage;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }

}
