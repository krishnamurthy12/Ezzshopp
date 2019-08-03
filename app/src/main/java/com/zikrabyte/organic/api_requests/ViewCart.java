package com.zikrabyte.organic.api_requests;

/**
 * Created by Krish on 13-03-2018.
 */

public class ViewCart {
    String user_id;
    String promo_code;

    public ViewCart(String user_id, String promo_code) {
        this.user_id = user_id;
        this.promo_code = promo_code;
    }
}
