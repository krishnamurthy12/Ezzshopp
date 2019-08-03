package com.zikrabyte.organic.api_requests;

/**
 * Created by KRISH on 3/28/2018.
 */

public class ApplyReferral {
    String user_id,referralcode;

    public ApplyReferral(String user_id, String referralcode) {
        this.user_id = user_id;
        this.referralcode = referralcode;
    }
}
