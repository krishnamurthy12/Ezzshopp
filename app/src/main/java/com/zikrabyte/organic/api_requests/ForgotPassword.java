package com.zikrabyte.organic.api_requests;

/**
 * Created by KRISH on 4/3/2018.
 */

public class ForgotPassword {
    String phone,password;

    public ForgotPassword(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }
}
