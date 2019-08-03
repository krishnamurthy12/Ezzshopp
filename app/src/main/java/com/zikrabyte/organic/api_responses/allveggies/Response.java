
package com.zikrabyte.organic.api_responses.allveggies;

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
    @SerializedName("old_price")
    @Expose
    private String oldPrice;
    @SerializedName("new_price")
    @Expose
    private String newPrice;
    @SerializedName("product_availablity")
    @Expose
    private String productAvailablity;
    @SerializedName("max_quantity")
    @Expose
    private String maxQuantity;
    @SerializedName("offer")
    @Expose
    private String offer;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("total_amt")
    @Expose
    private double totalAmt;

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

    public String getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(double totalAmt) {
        this.totalAmt = totalAmt;
    }

}
