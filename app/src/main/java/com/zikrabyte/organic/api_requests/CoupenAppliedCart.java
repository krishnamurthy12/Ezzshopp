package com.zikrabyte.organic.api_requests;

/**
 * Created by Krish on 19-03-2018.
 */

public class CoupenAppliedCart {
    String user_id,promo_code;

    public CoupenAppliedCart(String user_id, String promo_code) {
        this.user_id = user_id;
        this.promo_code = promo_code;
    }
}
