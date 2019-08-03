package com.zikrabyte.organic.api_requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by KRISH on 4/7/2018.
 */

public class BulkAPI {


    String user_id;
    JSONArray products;

    public BulkAPI(String user_id, JSONArray products) {
        this.user_id = user_id;
        this.products = products;
        //this.products.put(products);
    }
}
