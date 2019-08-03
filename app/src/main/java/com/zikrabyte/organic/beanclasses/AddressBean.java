package com.zikrabyte.organic.beanclasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Krish on 03-03-2018.
 */

public class AddressBean {
    String  name,phone_num, house_num,apartment_name,city ;

    public AddressBean(String name, String phone_num, String house_num, String apartment_name, String city) {
        this.name = name;
        this.phone_num = phone_num;
        this.house_num = house_num;
        this.apartment_name = apartment_name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getHouse_num() {
        return house_num;
    }

    public void setHouse_num(String house_num) {
        this.house_num = house_num;
    }

    public String getApartment_name() {
        return apartment_name;
    }

    public void setApartment_name(String apartment_name) {
        this.apartment_name = apartment_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

   }
