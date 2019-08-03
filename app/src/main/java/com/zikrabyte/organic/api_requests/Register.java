package com.zikrabyte.organic.api_requests;

/**
 * Created by Krish on 15-03-2018.
 */

public class Register {

    String email,password,fname,phone,referralcode;

    public Register(String email, String password, String fname, String phone, String referralcode) {
        this.email = email;
        this.password = password;
        this.fname = fname;
        this.phone = phone;
        this.referralcode = referralcode;
    }
}
