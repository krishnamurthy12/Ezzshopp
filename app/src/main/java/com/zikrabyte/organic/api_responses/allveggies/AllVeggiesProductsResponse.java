
package com.zikrabyte.organic.api_responses.allveggies;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllVeggiesProductsResponse {

    @SerializedName("Responsecode")
    @Expose
    private String responsecode;
    @SerializedName("products_message")
    @Expose
    private String productsMessage;

    @SerializedName("response_message")
    @Expose
    private String responseMessage;
    @SerializedName("response")
    @Expose
    private List<Response> response = null;

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getProductsMessage() {
        return productsMessage;
    }

    public void setProductsMessage(String productsMessage) {
        this.productsMessage = productsMessage;
    }

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }

}
