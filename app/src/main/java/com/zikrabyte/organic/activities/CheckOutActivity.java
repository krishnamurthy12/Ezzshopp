package com.zikrabyte.organic.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.circulardialog.CDialog;
import com.example.circulardialog.extras.CDConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.squareup.picasso.Picasso;
import com.zikrabyte.organic.R;
import com.zikrabyte.organic.api_requests.CashOnDelivery;
import com.zikrabyte.organic.api_requests.ViewUserProfile;
import com.zikrabyte.organic.api_responses.cod.CODResponse;
import com.zikrabyte.organic.api_responses.deliverydate.DeliveryDateResponse;
import com.zikrabyte.organic.api_responses.deliverydate.DeliveryDay;
import com.zikrabyte.organic.api_responses.viewuserprofile.ViewUserProfileResponse;
import com.zikrabyte.organic.customs.CircleImageView;
import com.zikrabyte.organic.payumoney.MakePaymentActivity;
import com.zikrabyte.organic.payumoney.PayUMoneyConstants;
import com.zikrabyte.organic.payumoney.PayuMoneyUtils;
import com.zikrabyte.organic.utils.EzzShoppUtils;
import com.zikrabyte.organic.utils.OnResponseListener;
import com.zikrabyte.organic.utils.WebServices;

import org.joda.time.DateTime;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import noman.weekcalendar.WeekCalendar;
import noman.weekcalendar.listener.OnDateClickListener;
import noman.weekcalendar.listener.OnWeekChangeListener;

public class CheckOutActivity extends AppCompatActivity implements View.OnClickListener,OnResponseListener {

    public static final String TAG = "CheckOutActivity";
    boolean isLoggedIn=false;
    Toast mToast;

    /*Navigation drawer elements*/
    DrawerLayout drawer;
    NavigationView navigationView;
    ImageView mNavigationMenuIcon,mNavigationBack;

    LinearLayout mHome,mMyProfileLayout,mMyAddressLayout,mMyCartLayout,mMyOrdersLayout,mMyCouponsLayout,mAboutUsLayout,mContactUsLayout,mShareLayout,mRateUsLayout,mLogOutLayout;
    CircleImageView mProfilePic;
    TextView mProfileName;

    String authKey,userId,userName,userEmail,userPhoneNumber,userPicture;


    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    Button mProceedToPayment, mEditAddress;
    //Spinner mPaymentSpinner;
   // ImageView mActionBack;
    TextView pickDate, mSelectedDate;
    TextView mName, mPhoneNum, mCity, mHouseNum, mApartmentName, mPinCode;
    String receivedName, receivedPhoneNum, receivedCity, receivedHouseNum, receivedApartmentName, receivedPinCode;

    String address,selectedDate,selectedTime;

    private WeekCalendar weekCalendar;
    RadioGroup mRadioGroup;
    RadioButton mCOD,mOnlinePayment;

    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;

    String token_id, user_id, id, ClassSec, Standard, Section,student_id;
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
    String appliedId;

    // String mSurl = "http://onmoney.co.in/ebud/payment/successUrl";
    // String mFurl = "http://onmoney.co.in/ebud/payment/failureUrl";

    String mSurl = "http://mob-india.com/ezzshop/success.php";
    String mFurl = "http://mob-india.com/ezzshop/failure.php";



    String Currenttime;

   /* String[] paymentTypeArray={"Select payment method","Cash on delivery","Online payment"};*/
    String selectedPaymentType=null;

    ProgressBar mProgressbar;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        setupWindowAnimations();
        initializeViews();
        Currenttime=System.currentTimeMillis()+"";
    }

    private boolean receiveIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            receivedName = bundle.getString("name", null);
            receivedPhoneNum = bundle.getString("phone", null);
            receivedCity = bundle.getString("city", null);
            receivedHouseNum = bundle.getString("house", null);
            receivedApartmentName = bundle.getString("apartment", null);
            receivedPinCode = bundle.getString("pincode", null);

            address=receivedName+", "+receivedPhoneNum+", "+receivedHouseNum+", "+receivedApartmentName+", "+receivedCity+", "+receivedPinCode+".";

            return true;
        } else
            return false;

    }

    private void setValuesToViews() {
        mName.setText(receivedName);
        mPhoneNum.setText(receivedPhoneNum);
        mCity.setText(receivedCity);
        mApartmentName.setText(receivedApartmentName);
        mHouseNum.setText(receivedHouseNum);
        mPinCode.setText(receivedPinCode);

    }


    private void initializeViews() {

        mRadioGroup=findViewById(R.id.vR_aco_payment_radio_group);
        mCOD=findViewById(R.id.vR_aco_cod_radio_btn);
        mOnlinePayment=findViewById(R.id.vR_aco_online_pay_radio_btn);

        mProgressbar=findViewById(R.id.vP_aco_progressbar);

       // mPaymentSpinner=findViewById(R.id.vS_aco_payment_spinner);

        pickDate = findViewById(R.id.vT_aco_choose_date);
        mSelectedDate = findViewById(R.id.vT_aco_selected_date);

        mName = findViewById(R.id.vT_aco_name);
        mPhoneNum = findViewById(R.id.vT_aco_phone_number);
        mCity = findViewById(R.id.vT_aco_city);
        mApartmentName = findViewById(R.id.vT_aco_apartment_name);
        mHouseNum = findViewById(R.id.vT_aco_house_number);
        mPinCode = findViewById(R.id.vT_aco_pincode);

        mProceedToPayment = findViewById(R.id.vB_aco_proceed_payment);
        //mActionBack = findViewById(R.id.vI_aco_back_icon);

        mEditAddress = findViewById(R.id.vB_aco_edit);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        mHome=findViewById(R.id.vL_nh_home_layout);
        mMyProfileLayout=findViewById(R.id.vL_nh_myprofile_layout);
        mMyAddressLayout=findViewById(R.id.vL_nh_myaddress_layout);
        mMyCartLayout=findViewById(R.id.vL_nh_mycart_layout);
        mMyOrdersLayout=findViewById(R.id.vL_nh_myorders_layout);
        mMyCouponsLayout=findViewById(R.id.vL_nh_mycoupons_layout);
        mAboutUsLayout=findViewById(R.id.vL_nh_aboutus_layout);
        mContactUsLayout=findViewById(R.id.vL_nh_contactus_layout);
        mShareLayout=findViewById(R.id.vL_nh_share_layout);
        mLogOutLayout=findViewById(R.id.vL_nh_logout_layout);
        mRateUsLayout=findViewById(R.id.vL_nh_rateus_layout);

        mProfilePic=findViewById(R.id.vIV_nh_profile_pic);
        mProfileName=findViewById(R.id.vT_nh_profile_name);


        mNavigationMenuIcon=findViewById(R.id.homepage_navigation_menu_icon);
        mNavigationBack=findViewById(R.id.vI_nh_back);

        mNavigationMenuIcon.setOnClickListener(this);
        mNavigationBack.setOnClickListener(this);

        mHome.setOnClickListener(this);
        mMyProfileLayout.setOnClickListener(this);
        mMyOrdersLayout.setOnClickListener(this);
        mMyAddressLayout.setOnClickListener(this);
        mMyCartLayout.setOnClickListener(this);
        mMyCouponsLayout.setOnClickListener(this);
        mAboutUsLayout.setOnClickListener(this);
        mContactUsLayout.setOnClickListener(this);
        mShareLayout.setOnClickListener(this);
        mRateUsLayout.setOnClickListener(this);
        mLogOutLayout.setOnClickListener(this);

        mProceedToPayment.setOnClickListener(this);
        //mActionBack.setOnClickListener(this);
        mEditAddress.setOnClickListener(this);
        pickDate.setOnClickListener(this);

        if (receiveIntentData()) {
            setValuesToViews();
        } else {
            View v = LayoutInflater.from(this).inflate(R.layout.empty_cart_layout, null);
        }
        mProgressbar.setVisibility(View.GONE);

        getSharedPreferenceData();

      /*  ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,paymentTypeArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/

        //Setting the ArrayAdapter data on the Spinner
       // mPaymentSpinner.setAdapter(arrayAdapter);


        weekCalendar = (WeekCalendar) findViewById(R.id.weekCalendar);
        DateTime dateTime = new DateTime();

        int a = dateTime.getDayOfWeek();
        Log.d("checked", a + " ");
        if (a == 1 || a == 2) {


        } else {
            // weekCalendar.onchangin
        }

       /* mPaymentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               *//* Toast.makeText(CheckOutActivity.this, paymentTypeArray[position], Toast.LENGTH_SHORT).show();*//*
               selectedPaymentType=paymentTypeArray[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

       /* mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId==R.id.vR_aco_cod_radio_btn)
                {
                    Log.d("selectedmethod",checkedId+"");
                }
                else if (checkedId==R.id.vR_aco_online_pay_radio_btn)
                {
                    Log.d("selectedmethod",checkedId+"");
                }
                else {
                    Log.d("selectedmethod",checkedId+"");
                }

            }
        });*/


    }

    private void getSharedPreferenceData()
    {
        SharedPreferences preferences=getSharedPreferences("LOGIN_PREFERENCE",MODE_PRIVATE);
        authKey=preferences.getString("AUTH_KEY",null);
        userId=preferences.getString("USER_ID",null);
        userName=preferences.getString("USER_NAME",null);
        userEmail=preferences.getString("EMAIL_ID",null);
        isLoggedIn= preferences.getBoolean("IS_LOGGEDIN",false);

        Log.d("AUTHKEY",authKey+"");

        mProfileName.setText(userName);

        callViewUserProfileAPI(authKey,userId);

        callDeliveryDaysAPI(authKey,userId);
    }

    private void callViewUserProfileAPI(String authKey,String userId)
    {
        ViewUserProfile viewUserProfile=new ViewUserProfile(userId);
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            /*mInternetStatusText.setVisibility(View.GONE);*/
            WebServices<ViewUserProfileResponse> webServices = new WebServices<ViewUserProfileResponse>(CheckOutActivity.this);
            webServices.viewUserProfile(WebServices.BASE_URL, WebServices.ApiType.viewUserProfile,authKey,userId);
        } else {

          /*  mInternetStatusText.setVisibility(View.VISIBLE);*/
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            /*Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet)+"", Toast.LENGTH_SHORT).show();*/
            showToast(getResources().getString(R.string.err_msg_nointernet));
        }

    }

    public void callDeliveryDaysAPI(String authKey,String userId)
    {
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            WebServices<DeliveryDateResponse> webServices = new WebServices<DeliveryDateResponse>(CheckOutActivity.this);
            webServices.getDeliveryDate(WebServices.BASE_URL, WebServices.ApiType.deliveryDays,authKey,userId);
        } else {
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            /*Toast.makeText(this, R.string.err_msg_nointernet+"", Toast.LENGTH_SHORT).show();*/
            showToast(getResources().getString(R.string.err_msg_nointernet));
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vB_aco_proceed_payment:
                //goToConfirmCheckOutActivity();
                if(mSelectedDate.getText().equals(""))
                {
                    Snackbar.make(mProceedToPayment,"please select an Delivery date",Snackbar.LENGTH_LONG).show();
                }
                else if(!validatePaymentRadioGroup())
                {
                   /* Snackbar.make(mProceedToPayment,"please choose an payment method",Snackbar.LENGTH_LONG).show();*/
                   return;
                }
                else
                {
                    processPayment(selectedPaymentType);
                }

                break;

            /*case R.id.vI_aco_back_icon:
                onBackPressed();
                break;*/

            case R.id.vB_aco_edit:
                gotoAdressActivity();
                break;

            case R.id.vT_aco_choose_date:
                pickDateAndTime();
                break;

            case R.id.homepage_navigation_menu_icon:
                openNavigationDrawer();
                break;

            case R.id.vI_nh_back:
                closeNavigationDrawer();
                break;

            case R.id.vL_nh_home_layout:
                closeNavigationDrawer();
                startActivity(HomeActivity.class);
                break;

            case R.id.vL_nh_myprofile_layout:
                gotoMyProfileActivity();
                closeNavigationDrawer();
                break;

            case R.id.vL_nh_myaddress_layout:
                closeNavigationDrawer();
                goToAddressActivity();
                break;

            case R.id.vL_nh_myorders_layout:
                closeNavigationDrawer();
                goToMyOrdersActivity();
                break;

            case R.id.vL_nh_mycart_layout:
                closeNavigationDrawer();
                goToCartActivity();
                break;

            case R.id.vL_nh_mycoupons_layout:
                closeNavigationDrawer();
                goToMyCouponsActivity();
                break;

            case R.id.vL_nh_aboutus_layout:
                closeNavigationDrawer();

                Intent aboutus=new Intent(this,WebViewActivity.class);
                aboutus.putExtra("aboutUsUrl","http://www.ezzshopp.com/about-us.php");
                startActivity(aboutus);
                break;

            case R.id.vL_nh_contactus_layout:
                closeNavigationDrawer();

                Intent contactus=new Intent(this,WebViewActivity.class);
                contactus.putExtra("contactUsurl","http://www.ezzshopp.com/contact.php");
                startActivity(contactus);

                break;

            case R.id.vL_nh_share_layout:
                closeNavigationDrawer();
                startActivity(ApplyReferralCodeActivity.class);
                break;

            case R.id.vL_nh_rateus_layout:

                Intent play = new Intent(Intent.ACTION_VIEW);
                play.setData(Uri.parse("market://details?id=" + getPackageName()));
                startActivity(play);
                closeNavigationDrawer();
                break;

            case R.id.vL_nh_logout_layout:
                closeNavigationDrawer();
                showLogOutDialog();
                break;

            default:closeNavigationDrawer();
        }

    }

    private void pickDateAndTime() {

    }

    private boolean validatePaymentRadioGroup()
    {
       int selectedId= mRadioGroup.getCheckedRadioButtonId();
       if(selectedId==mCOD.getId())
       {

           selectedPaymentType="Cash on delivery";
           return true;

       }
       else if(selectedId==mOnlinePayment.getId())
       {
           selectedPaymentType="Online payment";
           return true;

       }
       else
        {
            Snackbar.make(mProceedToPayment,"Please choose an payment method", Snackbar.LENGTH_SHORT).show();
            return false;
        }

    }

    private void goToConfirmCheckOutActivity() {
        startActivity(ConfirmCheckOutActivity.class);

    }

    private void gotoAdressActivity() {

        startActivity(SelectAddressActivity.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Transition transition, returnTransition;
        //transition = buildEnterTransition();
        transition = TransitionInflater.from(this).inflateTransition(R.transition.slide_from_bottom);
        returnTransition = buildReturnTransition();

       /* if (type == TYPE_PROGRAMMATICALLY) {
            transition = buildEnterTransition();
        }  else {
            transition = TransitionInflater.from(this).inflateTransition(R.transition.explode);
        }*/
        getWindow().setEnterTransition(transition);
        getWindow().setReturnTransition(returnTransition);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Visibility buildReturnTransition() {
        Visibility enterTransition = new Slide();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        return enterTransition;
    }

    private void startActivity(Class<?> tClass) {
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, null);
        startActivity(new Intent(CheckOutActivity.this, tClass), transitionActivityOptions.toBundle());
    }

    private void openNavigationDrawer()
    {
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);

        }

    }
    private void closeNavigationDrawer()
    {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        }

    }
    private void showLogOutDialog() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.logout_dialog, null);
        Button cancel=dialogView.findViewById(R.id.vB_lod_cancel);
        Button logOut=dialogView.findViewById(R.id.vB_lod_logout);

        builder=new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(HomeActivity.this, "LogOut", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences = getSharedPreferences("LOGIN_PREFERENCE", MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.clear();
                editor.apply();

                SharedPreferences usePpreferences=getSharedPreferences("USER_DETAILS",MODE_PRIVATE);
                SharedPreferences.Editor editor1=usePpreferences.edit();
                editor1.clear();
                editor1.apply();

                SharedPreferences paymentDetails=getSharedPreferences("PAYMENT_DETAILS",MODE_PRIVATE);
                SharedPreferences.Editor editor2=paymentDetails.edit();
                editor2.clear();
                editor2.apply();

                ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(CheckOutActivity.this,null);

                Intent intent=new Intent(CheckOutActivity.this,LogInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finishAffinity();
                startActivity(intent,transitionActivityOptions.toBundle());
                alertDialog.dismiss();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(HomeActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();

            }
        });

    }

    private void gotoMyProfileActivity() {
        startActivity(UserProfileActivity.class);
    }
    private void goToCartActivity() {
        startActivity(CartActivity.class);
    }
    private void goToAddressActivity()
    {
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this,null);
        Intent coupenIntent=new Intent(this,SelectAddressActivity.class);
        coupenIntent.putExtra("FROM","NAVIGATIONDRAWER");
        startActivity(coupenIntent,transitionActivityOptions.toBundle());
        //startActivity(SelectAddressActivity.class);
    }

    private void goToMyOrdersActivity()
    {
        startActivity(OrderHistoryActivity.class);
    }
    private void goToMyCouponsActivity() {

        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this,null);
        Intent coupenIntent=new Intent(this,CoupensActivity.class);
        coupenIntent.putExtra("FROM","NAVIGATIONDRAWER");
        startActivity(coupenIntent,transitionActivityOptions.toBundle());
        //startActivity(CoupensActivity.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void processPayment(String selectedPaymentType) {
        if(selectedPaymentType.equalsIgnoreCase("Cash on delivery"))
        {
            CallGetProfileAPIForCOD();

        }
        else if(selectedPaymentType.equalsIgnoreCase("Online payment"))
        {
            CallGetProfileAPIForOnlinePayment();

        }
        else {
            Snackbar.make(mProceedToPayment,"Invalid payment type",Snackbar.LENGTH_LONG).show();
        }


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

  /*  private void initializeViews() {


        CallGetProfileAPI();
    }*/
  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  private void CallGetProfileAPIForCOD() {

      String txnId = System.currentTimeMillis() + "";
      SharedPreferences preferences = getSharedPreferences("USER_DETAILS", 0);
      firstname = preferences.getString("USERNAME", null);
      emailid = preferences.getString("EMAIL", null);
      mobile= preferences.getString("PHONE_NUMBER", null);
      user_id = preferences.getString("USER_ID", null);


      SharedPreferences paymentPreference = getSharedPreferences("PAYMENT_DETAILS", 0);
      Log.e("CheckOut",">>>"+paymentPreference.getString("GRAND_TOTAL", null));
      bill_PA = Double.parseDouble(paymentPreference.getString("GRAND_TOTAL", null));
      appliedId=paymentPreference.getString("APPLIED_ID",null);

      Log.d("checkparameters","firstname=>"+firstname+"\nemailid=>"+emailid+"\nmobile=>"+mobile+"\nuserid=>"+
              user_id+"\n amount=>"+bill_PA+"\nappliedid=>"+appliedId);

      prod_title = "Ezzshopp";
      Standard=address;
      Section=selectedDate;

      mTXNId=getMd5value(user_id +Standard+Section);
      Log.e("getMd5value Encrypted",mTXNId);

      CashOnDelivery cashOnDelivery=new CashOnDelivery(user_id,appliedId,userName,emailid,mobile,"pending",bill_PA+"",
              mTXNId,"Pending","COD","COD",Standard,Section,selectedTime);


      callCashOnDeliveryAPI(authKey,user_id,cashOnDelivery);


  }

    private void callCashOnDeliveryAPI(String authKey,String userId,CashOnDelivery cashOnDelivery)
    {
        if(cashOnDelivery!=null)
        {
            if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

                mProgressbar.setVisibility(View.VISIBLE);

                WebServices<CODResponse> webServices = new WebServices<CODResponse>(CheckOutActivity.this);
                webServices.cashOnDelivery(WebServices.BASE_URL, WebServices.ApiType.cod,authKey,userId,cashOnDelivery);
            } else {

                //mRetry.setVisibility(View.VISIBLE);
                //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
                /*Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet)+"", Toast.LENGTH_SHORT).show();*/
                showToast(getResources().getString(R.string.err_msg_nointernet));
            }

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void CallGetProfileAPIForOnlinePayment() {


        //  if (EbudUtils.isConnectingToInternet(PaymentActivity.this)) {

        String txnId = System.currentTimeMillis() + "";
        SharedPreferences preferences = getSharedPreferences("USER_DETAILS", 0);
        firstname = preferences.getString("USERNAME", null);
        emailid = preferences.getString("EMAIL", null);
        mobile= preferences.getString("PHONE_NUMBER", null);
        user_id = preferences.getString("USER_ID", null);


        SharedPreferences paymentPreference = getSharedPreferences("PAYMENT_DETAILS", 0);
        Log.e("CheckOut",">>>"+paymentPreference.getString("GRAND_TOTAL", null));
        bill_PA = Double.parseDouble(paymentPreference.getString("GRAND_TOTAL", null));
        appliedId=paymentPreference.getString("APPLIED_ID",null);

        Log.d("checkparameters","firstname=>"+firstname+"\nemailid=>"+emailid+"\nmobile=>"+mobile+"\nuserid=>"+
        user_id+"\n amount=>"+bill_PA+"\nappliedid=>"+appliedId);

        prod_title = "Ezzshopp";
        Standard=address;
        Section=selectedDate;

        mTXNId=getMd5value(user_id +Standard+Section);
        Log.e("getMd5value Encrypted",mTXNId);
        init();

        Intent actualPayment = new Intent(this, MakePaymentActivity.class);
        actualPayment.putExtra(PayUMoneyConstants.ENVIRONMENT, PayUMoneyConstants.ENV_DEV);
        actualPayment.putExtra(PayUMoneyConstants.PARAMS, params);

        startActivityForResult(actualPayment, PayUMoneyConstants.PAYMENT_REQUEST);



       /* } else {
            Toast.makeText(PaymentActivity.this, R.string.please_check, Toast.LENGTH_SHORT).show();
        }*/


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
        params.put(PayUMoneyConstants.UDF1, user_id);
        params.put(PayUMoneyConstants.UDF2, Standard);
        params.put(PayUMoneyConstants.UDF3, Section);
        params.put(PayUMoneyConstants.UDF4, selectedDate);
        if(appliedId!=null) {
            params.put(PayUMoneyConstants.UDF5, appliedId);
        }else{
            params.put(PayUMoneyConstants.UDF5, "000");
        }

//        params.put(PayUMoneyConstants.UDF4, "");
//        params.put(PayUMoneyConstants.UDF5, "");
        // params.put(PayUMoneyConstants.UDF6, type);

        String hash = PayuMoneyUtils.generateHash(params, Merchant_Salt);

        params.put(PayUMoneyConstants.HASH, hash);
        params.put(PayUMoneyConstants.SERVICE_PROVIDER, "payu_paisa");

    }



    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PayUMoneyConstants.PAYMENT_REQUEST) {
            if (resultCode == RESULT_OK) {

                if (data != null) {

                    Log.e("Pay u Pg data",""+data.toString());
                   /* Toast.makeText(this, "Payment Success,", Toast.LENGTH_SHORT).show();*/
                    showToast("Payment Success");

                  /*  Intent intent = new Intent(this, ParentHomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);*/
                }

            } else if (resultCode == RESULT_CANCELED) {

                if (data != null) {
                    String msg = (String) getIntent().getExtras().get("result");
                    Log.e("Pay u Pg msg ", msg);
                    finish();

                /*    RJSnackBar.makeText(this, msg + "--" + "Payment Failed | Cancelled.", RJSnackBar.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, ParentHomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);*/


                } else {
                    finish();

                  /*  Intent intent = new Intent(this, ParentHomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
*/
                }

            }
        }
    }

    @Override
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {
        switch (URL)
        {
            case deliveryDays:
                if (isSucces) {
                    DeliveryDateResponse deliveryDateResponse= (DeliveryDateResponse) response;
                    if(deliveryDateResponse!=null)
                    {
                        if(deliveryDateResponse.getResponsecode().equalsIgnoreCase("200"))
                        {

                            updateUIwithResponseData(deliveryDateResponse);


                        }
                    }


                }
                else
                {
                    //API call Fail
                }
                break;

            case viewUserProfile:
                ViewUserProfileResponse viewUserProfileResponse= (ViewUserProfileResponse) response;
                if(isSucces)
                {
                    if(viewUserProfileResponse!=null)
                    {
                        if(viewUserProfileResponse.getResponsecode()!=null) {
                            if (viewUserProfileResponse.getResponsecode().equalsIgnoreCase("200")) {
                                List<com.zikrabyte.organic.api_responses.viewuserprofile.Response> userinfoList = viewUserProfileResponse.getResponse();
                                if(userinfoList!=null) {
                                    updateUserProfile(userinfoList);
                                }else {
                                    if (viewUserProfileResponse.getProfileMessage() != null) {
                                       /* Toast.makeText(this, viewUserProfileResponse.getProfileMessage(), Toast.LENGTH_SHORT).show();*/

                                       showToast(viewUserProfileResponse.getProfileMessage());
                                    }

                                }
                            } else {
                                if(viewUserProfileResponse.getProfileMessage()!=null)
                                {
                                    /*Toast.makeText(this, viewUserProfileResponse.getProfileMessage(), Toast.LENGTH_SHORT).show();*/

                                    showToast(viewUserProfileResponse.getProfileMessage());
                                }

                            }
                        }
                    }
                    else
                    {

                        // Snackbar.make(recyclerView,"user information is empty",Snackbar.LENGTH_LONG).show();

                    }


                }
                else
                {
                    if(viewUserProfileResponse!=null) {
                        if (viewUserProfileResponse.getProfileMessage() != null) {
                           // Toast.makeText(this, viewUserProfileResponse.getProfileMessage(), Toast.LENGTH_SHORT).show();
                            showToast(viewUserProfileResponse.getProfileMessage());
                        }
                    }
                    //API call failed
                }
                break;

            case cod:

                if(mProgressbar.isShown())
                {
                    mProgressbar.setVisibility(View.GONE);

                }
                final CODResponse codResponse= (CODResponse) response;
                if(isSucces)
                {
                    if(codResponse!=null)
                    {
                        if(codResponse.getResponsecode()!=null)
                        {
                            if(codResponse.getResponsecode().equalsIgnoreCase("200"))
                            {
                                //Order placed


                                if(codResponse.getAddCartMessage()!=null)
                                {
                                    Snackbar.make(mProceedToPayment,codResponse.getAddCartMessage(),3000).show();

                                    new CDialog(this).createAlert("Success",
                                            CDConstants.SUCCESS,   // Type of dialog
                                            CDConstants.LARGE)
                                            .setPosition(CDConstants.CENTER)
                                            //  size of dialog
                                            .setAnimation(CDConstants.SCALE_FROM_BOTTOM_TO_TOP)     //  Animation for enter/exit
                                            .setDuration(3000)
                                            // in milliseconds
                                            .setTextSize(CDConstants.LARGE_TEXT_SIZE)
                                            .setBackgroundColor(getResources().getColor(R.color.blue))// CDConstants.LARGE_TEXT_SIZE, CDConstants.NORMAL_TEXT_SIZE
                                            .show();
                                }

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(HomeActivity.class);

                                    }
                                },3000);


                            }
                            else {

                                if(codResponse.getAddCartMessage()!=null)
                                {
                                    Snackbar.make(mProceedToPayment,codResponse.getAddCartMessage(),Snackbar.LENGTH_LONG).show();
                                }

                            }
                        }
                    }

                }
                else
                {
                    //API call failed
                    if(codResponse!=null)
                    {
                        if(codResponse.getAddCartMessage()!=null)
                        {
                            Snackbar.make(mProceedToPayment,codResponse.getAddCartMessage(),Snackbar.LENGTH_LONG).show();
                        }
                    }

                }
        }

    }

    private void updateUserProfile(List<com.zikrabyte.organic.api_responses.viewuserprofile.Response> mList) {

        userName=mList.get(0).getFname();
        userEmail=mList.get(0).getEmail();
        userPhoneNumber=mList.get(0).getPhone();
        userPicture=mList.get(0).getPicture();

        SharedPreferences preferences=getSharedPreferences("USER_DETAILS",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("USERNAME",userName);
        editor.putString("EMAIL",userEmail);
        editor.putString("PHONE_NUMBER",userPhoneNumber);
        editor.putString("USER_ID",userId);
        editor.apply();

        mProfileName.setText(userName);
        /*Glide.with(this)
                .asBitmap()
                .load(userPicture)
                .into(mUserProfilePic);*/

        if(userPicture==null || userPicture.isEmpty())
        {

            Picasso.get()
                    .load(R.drawable.logo)
                    .placeholder(R.drawable.background_drawable)
                    .error(R.drawable.user)
                    //.resize(150,150)
                    .into(mProfilePic);

        }
        else
        {
            Picasso.get()
                    .load(userPicture)
                    .placeholder(R.drawable.background_drawable)
                    .error(R.drawable.user)
                    //.resize(150,150)
                    .into(mProfilePic);

        }



    }

    private void updateUIwithResponseData(DeliveryDateResponse deliveryDateResponse) {

        final List<DeliveryDay> mList=deliveryDateResponse.getDeliveryDays();
        final String fromTime=deliveryDateResponse.getDeliveryTimeFrom();
        final String toTime=deliveryDateResponse.getDeliveryTimeTo();
        for(int i=0;i<mList.size();i++)
        {
            Log.d("deliverydays",mList.get(i).getDay());//sunday,monday,tuesday

        }

        weekCalendar.setStartDate(DateTime.now());
       // weekCalendar.setStartDate(System.currentTimeMillis());

        weekCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(DateTime dateTime) {
                if(!dateTime.isBeforeNow()) {
                    if (dateTime.getDayOfWeek() == 1 || dateTime.getDayOfWeek() == 2 || dateTime.getDayOfWeek() == 7) {

                    /*TextView selectedDate=findViewById(R.id.vT_aco_selected_date);

                    selectedDate.setText(dateTime.getDayOfMonth());*/
                   /* Toast.makeText(CheckOutActivity.this, "You Selected " + dateTime.toString(), Toast
                            .LENGTH_SHORT).show();*/
                        mSelectedDate.setText(dateTime.getDayOfMonth() + "-" + dateTime.getMonthOfYear() + "-" + dateTime.getYear() + "  " + fromTime + " PM to " + toTime + " PM");
                        selectedDate = dateTime.getDayOfMonth() + "-" + dateTime.getMonthOfYear() + "-" + dateTime.getYear();

                        selectedTime=fromTime + "PM to " + toTime + "PM";
                       /* SharedPreferences sharedPreferences=getSharedPreferences("DELIVERY_DATE_PREFERENCE",MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("DELIVERYDATE",selectedDate);
                        editor.apply();
*/
                        Log.d("checked", dateTime.toString() + " ");
                    }
                    else
                    {
                        mSelectedDate.setText("");
                       // mSelectedDate.setBackground(getResources().getDrawable(R.drawable.round));
                    }
                }else {
                    mSelectedDate.setText("");
                    //Toast.makeText(CheckOutActivity.this, "Delivery is not available on this day ", Toast.LENGTH_SHORT).show();

                    //showToast("Delivery is not available on this day");
                    Snackbar.make(mProceedToPayment,"Delivery is not available on this day",Snackbar.LENGTH_LONG).show();
                }

            }

        });

        weekCalendar.setOnWeekChangeListener(new OnWeekChangeListener() {
            @Override
            public void onWeekChange(DateTime firstDayOfTheWeek, boolean forward) {

               /* Toast.makeText(CheckOutActivity.this, "Week changed: " + firstDayOfTheWeek +
                        " Forward: " + forward, Toast.LENGTH_SHORT).show();*/
            }
        });
    }

    public void showToast (String message){
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
