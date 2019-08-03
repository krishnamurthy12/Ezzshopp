package com.zikrabyte.organic.api_requests;

/**
 * Created by KRISH on 4/7/2018.
 */

public class CancelOrder {

    String user_id,order_id, phone,amount;

    public CancelOrder(String user_id, String order_id, String phone, String amount) {
        this.user_id = user_id;
        this.order_id = order_id;
        this.phone = phone;
        this.amount = amount;
    }
}
