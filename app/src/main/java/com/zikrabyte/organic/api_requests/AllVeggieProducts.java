package com.zikrabyte.organic.api_requests;

/**
 * Created by Krish on 26-03-2018.
 */

public class AllVeggieProducts {

    String category_id,user_id,product_id;

    public AllVeggieProducts(String category_id, String user_id, String product_id) {
        this.category_id = category_id;
        this.user_id = user_id;
        this.product_id = product_id;
    }
}
