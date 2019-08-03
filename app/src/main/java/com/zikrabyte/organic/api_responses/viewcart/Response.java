
package com.zikrabyte.organic.api_responses.viewcart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("new_price")
    @Expose
    private String newPrice;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("product_availablity")
    @Expose
    private String productAvailablity;
    @SerializedName("max_quantity")
    @Expose
    private String maxQuantity;
    @SerializedName("offer")
    @Expose
    private String offer;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("total_amt")
    @Expose
    private String totalAmt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getProductAvailablity() {
        return productAvailablity;
    }

    public void setProductAvailablity(String productAvailablity) {
        this.productAvailablity = productAvailablity;
    }

    public String getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(String maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

}
