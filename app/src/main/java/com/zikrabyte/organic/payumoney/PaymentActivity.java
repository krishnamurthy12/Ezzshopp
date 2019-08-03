package com.zikrabyte.organic.payumoney;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.zikrabyte.organic.R;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity {

    int SelectedPersona;
    String token_id, school_id, id, ClassSec, Standard, Section,student_id;
    private HashMap<String, String> params = new HashMap<>();


   /* final String Merchant_ID = "4934580";
    final String Merchant_Key = "rjQUPktU";
    final String Merchant_Salt = "e5iIg1jwi8";*/

    final String Merchant_ID = "5547794";
    final String Merchant_Key = "MLSHomg4";
    final String Merchant_Salt = "Bt5BCaSyfS";



    String firstname;
    String emailid;
    String mobile,type,prod_title;
    String hashkeyUser;
    double bill_PA;
    String status;
    String mTXNId;

   // String mSurl = "http://onmoney.co.in/ebud/payment/successUrl";
   // String mFurl = "http://onmoney.co.in/ebud/payment/failureUrl";

    String mSurl = "http://mob-india.com/ezzshop/success.php";
    String mFurl = "http://mob-india.com/ezzshop/failure.php";



    String Currenttime;

    private String TAG = PaymentActivity.class.getSimpleName();

    String[] parts;

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        initializeViews();

        Currenttime=System.currentTimeMillis()+"";

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getMd5value(String MD5string){
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md5.update(StandardCharsets.UTF_8.encode(MD5string));
        return String.format("%032x", new BigInteger(1, md5.digest()));
    }

    private void initializeViews() {


        CallGetProfileAPI();
    }

    private void CallGetProfileAPI() {


      //  if (EbudUtils.isConnectingToInternet(PaymentActivity.this)) {

            firstname = "abd";
            emailid = "10rudresh@gmail.com";
            mobile = "9741644341";
            prod_title = "Ezzshopp";
            bill_PA = 200.00;
        school_id="46";
        Standard="BTM Layout";
        Section="25March2018";

            Log.e("getMd5value",school_id+Standard+Section);
            mTXNId=getMd5value(school_id+Standard+Section);
            Log.e("getMd5value Encrypted",mTXNId);
            init();

            Intent actualPayment = new Intent(this, MakePaymentActivity.class);
            actualPayment.putExtra(PayUMoneyConstants.ENVIRONMENT, PayUMoneyConstants.ENV_DEV);
            actualPayment.putExtra(PayUMoneyConstants.PARAMS, params);

            startActivityForResult(actualPayment, PayUMoneyConstants.PAYMENT_REQUEST);



       *//* } else {
            Toast.makeText(PaymentActivity.this, R.string.please_check, Toast.LENGTH_SHORT).show();
        }*//*


    }

    private synchronized void init() {

        params.put(PayUMoneyConstants.KEY, Merchant_Key);
        params.put(PayUMoneyConstants.TXN_ID, mTXNId);
        params.put(PayUMoneyConstants.AMOUNT, bill_PA + "");
        params.put(PayUMoneyConstants.PRODUCT_INFO, prod_title);
        params.put(PayUMoneyConstants.FIRST_NAME, firstname);
        params.put(PayUMoneyConstants.EMAIL, emailid);
        params.put(PayUMoneyConstants.PHONE, mobile);
        params.put(PayUMoneyConstants.SURL, mSurl);
        params.put(PayUMoneyConstants.FURL, mFurl);
        params.put(PayUMoneyConstants.UDF1, school_id);
        params.put(PayUMoneyConstants.UDF2, Standard+"-"+Section);
        params.put(PayUMoneyConstants.UDF3, "845");
        params.put(PayUMoneyConstants.UDF4, "");
        params.put(PayUMoneyConstants.UDF5, "");
       // params.put(PayUMoneyConstants.UDF6, type);

        String hash = PayuMoneyUtils.generateHash(params, Merchant_Salt);

        params.put(PayUMoneyConstants.HASH, hash);
        params.put(PayUMoneyConstants.SERVICE_PROVIDER, "payu_paisa");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PayUMoneyConstants.PAYMENT_REQUEST) {
            if (resultCode == RESULT_OK) {

                if (data != null) {

                    Log.e("Pay u Pg data",""+data.toString());
                    Toast.makeText(this, "Payment Success,", Toast.LENGTH_SHORT).show();

                  *//*  Intent intent = new Intent(this, ParentHomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);*//*
                }

            } else if (resultCode == RESULT_CANCELED) {

                if (data != null) {
                    String msg = (String) getIntent().getExtras().get("result");
                    Log.e("Pay u Pg msg ", msg);
                   finish();

                *//*    RJSnackBar.makeText(this, msg + "--" + "Payment Failed | Cancelled.", RJSnackBar.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, ParentHomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);*//*


                } else {
                    finish();

                  *//*  Intent intent = new Intent(this, ParentHomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
*//*
                }

            }
        }
    }*/


}