
package com.zikrabyte.organic.api_responses.searchresults;

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
    @SerializedName("description")
    @Expose
    private String description;
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
    private Integer totalAmt;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("discount_price")
    @Expose
    private Integer discountPrice;

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

    public Integer getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Integer totalAmt) {
        this.totalAmt = totalAmt;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Integer discountPrice) {
        this.discountPrice = discountPrice;
    }

}
