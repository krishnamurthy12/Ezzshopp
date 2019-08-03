
package com.zikrabyte.organic.api_responses.itemdescription;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("new_price")
    @Expose
    private String newPrice;
    @SerializedName("product_availablity")
    @Expose
    private String productAvailablity;
    @SerializedName("delivery_type")
    @Expose
    private String deliveryType;
    @SerializedName("delivery_charge")
    @Expose
    private String deliveryCharge;
    @SerializedName("offer")
    @Expose
    private String offer;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("total_amt")
    @Expose
    private String totalAmt;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("discount_price")
    @Expose
    private Double discountPrice;

    public String getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(String maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    @SerializedName("max_quantity")
    @Expose
    private String maxQuantity;

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    @SerializedName("response_message")
    @Expose
    private String responseMessage;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public String getProductAvailablity() {
        return productAvailablity;
    }

    public void setProductAvailablity(String productAvailablity) {
        this.productAvailablity = productAvailablity;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

}
