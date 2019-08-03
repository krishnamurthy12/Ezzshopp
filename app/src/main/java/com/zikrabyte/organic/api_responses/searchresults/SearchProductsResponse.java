
package com.zikrabyte.organic.api_responses.searchresults;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchProductsResponse {

    @SerializedName("Responsecode")
    @Expose
    private String responsecode;
    @SerializedName("search_product_message")
    @Expose
    private String searchProductMessage;
    @SerializedName("response")
    @Expose
    private List<Response> response = null;

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

    public String getSearchProductMessage() {
        return searchProductMessage;
    }

    public void setSearchProductMessage(String searchProductMessage) {
        this.searchProductMessage = searchProductMessage;
    }

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }

}
