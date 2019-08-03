
package com.zikrabyte.organic.api_responses.apartmentlist;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApartmentListResponse {

    @SerializedName("Responsecode")
    @Expose
    private String responsecode;
    @SerializedName("view_address_message")
    @Expose
    private String viewAddressMessage;
    @SerializedName("response")
    @Expose
    private List<Response> response = null;

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

    public String getViewAddressMessage() {
        return viewAddressMessage;
    }

    public void setViewAddressMessage(String viewAddressMessage) {
        this.viewAddressMessage = viewAddressMessage;
    }

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }

}
