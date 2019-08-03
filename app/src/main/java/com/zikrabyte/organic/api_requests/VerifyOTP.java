package com.zikrabyte.organic.api_requests;

/**
 * Created by Krish on 15-03-2018.
 */

public class VerifyOTP {

    String phone,otp;

    public VerifyOTP(String phone, String otp) {
        this.phone = phone;
        this.otp = otp;
    }
}
