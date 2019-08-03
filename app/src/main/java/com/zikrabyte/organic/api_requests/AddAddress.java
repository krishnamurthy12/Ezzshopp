package com.zikrabyte.organic.api_requests;

/**
 * Created by Krish on 14-03-2018.
 */

public class AddAddress {

     String user_id,name,phone,address,apartment,city,pincode;

    public AddAddress(String user_id, String name, String phone, String address, String apartment, String city, String pincode) {
        this.user_id = user_id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.apartment = apartment;
        this.city = city;
        this.pincode = pincode;
    }
}
