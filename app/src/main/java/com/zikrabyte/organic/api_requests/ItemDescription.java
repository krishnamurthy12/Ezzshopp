package com.zikrabyte.organic.api_requests;

/**
 * Created by Krish on 12-03-2018.
 */

public class ItemDescription {
    String category_id,user_id,product_id;

    public ItemDescription(String category_id, String user_id, String product_id) {
        this.category_id = category_id;
        this.user_id = user_id;
        this.product_id = product_id;
    }
}
