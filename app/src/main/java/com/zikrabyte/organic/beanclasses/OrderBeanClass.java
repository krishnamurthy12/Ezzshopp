package com.zikrabyte.organic.beanclasses;

/**
 * Created by Krish on 08-03-2018.
 */

public class OrderBeanClass {

    String itemName;
    int quantity,cost;

    public OrderBeanClass(String itemName, int quantity, int cost) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.cost = cost;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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
