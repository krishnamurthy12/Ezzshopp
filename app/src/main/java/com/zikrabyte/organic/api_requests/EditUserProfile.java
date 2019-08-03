package com.zikrabyte.organic.api_requests;

/**
 * Created by Krish on 16-03-2018.
 */

public class EditUserProfile {
    String user_id,fname,email,phone,picture;

    public EditUserProfile(String user_id, String fname, String email, String phone, String picture) {
        this.user_id = user_id;
        this.fname = fname;
        this.email = email;
        this.phone = phone;
        this.picture = picture;
    }
}
