
package com.zikrabyte.organic.api_responses.categories;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoriesResponse {

    @SerializedName("response")
    @Expose
    private List<Response> response = null;
    @SerializedName("categories_message")
    @Expose
    private String categoriesMessage;
    @SerializedName("Responsecode")
    @Expose
    private String responsecode;

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    @SerializedName("response_message")
    @Expose
    private String responseMessage;

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }

    public String getCategoriesMessage() {
        return categoriesMessage;
    }

    public void setCategoriesMessage(String categoriesMessage) {
        this.categoriesMessage = categoriesMessage;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

}
