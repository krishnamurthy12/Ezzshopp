package com.zikrabyte.organic.api_requests;

/**
 * Created by Krish on 14-03-2018.
 */

public class EditAddress {

    String id,name,phone,address,apartment,city,pincode;

    public EditAddress(String id, String name, String phone, String address, String apartment, String city, String pincode) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.apartment = apartment;
        this.city = city;
        this.pincode = pincode;
    }
}
