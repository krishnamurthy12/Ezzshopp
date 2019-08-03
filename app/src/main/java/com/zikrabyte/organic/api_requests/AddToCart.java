package com.zikrabyte.organic.api_requests;

/**
 * Created by Krish on 13-03-2018.
 */

public class AddToCart {

    String product_id,user_id,quantity;

    public AddToCart(String product_id, String user_id, String quantity) {
        this.product_id = product_id;
        this.user_id = user_id;
        this.quantity = quantity;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
