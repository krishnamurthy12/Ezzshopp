package com.zikrabyte.organic.api_requests;

/**
 * Created by Krish on 13-03-2018.
 */

public class RemoveFromCart {
    String user_id,product_id;

    public RemoveFromCart(String user_id, String product_id) {
        this.user_id = user_id;
        this.product_id = product_id;
    }
}
