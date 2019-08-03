package com.zikrabyte.organic.api_requests;

/**
 * Created by KRISH on 4/7/2018.
 */

public class CashOnDelivery {

    String user_id, applied_last_id, name, email, phone, payment_status, paid_amt, txnid, paid_on, bank_ref_num, cardnum, address,delivery_date,delivery_time;

    public CashOnDelivery(String user_id, String applied_last_id, String name, String email, String phone, String payment_status, String paid_amt, String txnid, String paid_on, String bank_ref_num, String cardnum, String address, String delivery_date, String delivery_time) {
        this.user_id = user_id;
        this.applied_last_id = applied_last_id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.payment_status = payment_status;
        this.paid_amt = paid_amt;
        this.txnid = txnid;
        this.paid_on = paid_on;
        this.bank_ref_num = bank_ref_num;
        this.cardnum = cardnum;
        this.address = address;
        this.delivery_date = delivery_date;
        this.delivery_time = delivery_time;
    }
}
