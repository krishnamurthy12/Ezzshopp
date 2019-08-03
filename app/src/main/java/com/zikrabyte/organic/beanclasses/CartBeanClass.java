package com.zikrabyte.organic.beanclasses;

/**
 * Created by Krish on 02-03-2018.
 */

public class CartBeanClass {
    int image;
    String name;
    int quantity,cost;

    public CartBeanClass(int image, String name, int quantity, int cost) {
        this.image = image;
        this.name = name;
        this.quantity = quantity;
        this.cost = cost;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
