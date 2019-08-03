package com.zikrabyte.organic.beanclasses;

/**
 * Created by Krish on 28-02-2018.
 */

public class ItemDescription {
    int image;
    String name,quantityType,price,rating,discount,description;

    public ItemDescription(int image, String name, String quantityType, String price, String rating, String discount, String description) {
        this.image = image;
        this.name = name;
        this.quantityType = quantityType;
        this.price = price;
        this.rating = rating;
        this.discount = discount;
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(String quantityType) {
        this.quantityType = quantityType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
