package com.zikrabyte.organic.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zikrabyte.organic.R;
import com.zikrabyte.organic.api_requests.CoupenAppliedCart;
import com.zikrabyte.organic.api_requests.ViewCart;
import com.zikrabyte.organic.api_requests.ViewUserProfile;
import com.zikrabyte.organic.api_responses.coupenapplied.CoupenAppliedResponse;
import com.zikrabyte.organic.api_responses.viewcart.ViewCartResponse;
import com.zikrabyte.organic.api_responses.viewuserprofile.Response;
import com.zikrabyte.organic.api_responses.viewuserprofile.ViewUserProfileResponse;
import com.zikrabyte.organic.customs.CircleImageView;
import com.zikrabyte.organic.utils.EzzShoppUtils;
import com.zikrabyte.organic.utils.OnResponseListener;
import com.zikrabyte.organic.utils.WebServices;

import java.util.List;

public class PaymentDetailsActivity extends AppCompatActivity implements View.OnClickListener,OnResponseListener{


    DrawerLayout drawer;
    NavigationView navigationView;
    ImageView mNavigationMenuIcon,mNavigationBack;

    LinearLayout mHome,mMyProfileLayout,mMyAddressLayout,mMyCartLayout,mMyOrdersLayout,mMyCouponsLayout,mAboutUsLayout,mContactUsLayout,mShareLayout,mRateUsLayout,mLogOutLayout;
    CircleImageView mProfilePic;
    TextView mProfileName;

    String authKey,userId,userName,userEmail,userPhoneNumber,userPicture;


    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    Button mCheckOut;
    ImageView mActionBack;
    TextView mApplyCoupen,mSubTotal,mGST,mDeliveryCharge,mTotalAmount,mCoupenAppliedText;

    String appliedCoupen=null;
    String subTotal,gst,deliveryCharge,grandTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_datails);
        //setupWindowAnimations();
        initializeViews();
    }

    private void initializeViews() {
        mCheckOut=findViewById(R.id.vB_aac_checkout);
        mApplyCoupen=findViewById(R.id.vT_aac_apply_coupen);
        //mActionBack=findViewById(R.id.vI_aac_back_icon);
        
        mSubTotal=findViewById(R.id.vT_aac_subtotal);
        mGST=findViewById(R.id.vT_aac_gst);
        mDeliveryCharge=findViewById(R.id.vT_aac_delivery_charge);
        mTotalAmount=findViewById(R.id.vT_aac_total_amount);
        mCoupenAppliedText=findViewById(R.id.vT_aac_coupen_applied_text);

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

        mCheckOut.setVisibility(View.VISIBLE);
        mCheckOut.setOnClickListener(this);
        //mActionBack.setOnClickListener(this);
        mApplyCoupen.setOnClickListener(this);

        if(receiveIntentData())
        {

            setValuesToViews();
           //toggleVisibility(mCoupenAppliedText);
            if(appliedCoupen!=null) {

                mCheckOut.setVisibility(View.VISIBLE);
                //mCoupenAppliedText.setVisibility(View.VISIBLE);
                //Toast.makeText(this, appliedCoupen+"", Toast.LENGTH_SHORT).show();
            }
            else
            {
                mCoupenAppliedText.setVisibility(View.GONE);
            }
            //Toast.makeText(this, appliedCoupen+"", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mCoupenAppliedText.setVisibility(View.GONE);
        }

        getSharedPreferenceData();
    }

    private void setValuesToViews() {
        mSubTotal.setText(subTotal);
        mTotalAmount.setText(grandTotal);
        mGST.setText(gst);
        mDeliveryCharge.setText(deliveryCharge);

        SharedPreferences preferences=getSharedPreferences("PAYMENT_DETAILS",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("GRAND_TOTAL",grandTotal);
        editor.apply();
    }

    private boolean receiveIntentData() {
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            subTotal=bundle.getString("SUB_TOTAL",null);
            gst=bundle.getString("GST",null);
            deliveryCharge=bundle.getString("DELIVERY_CHARGE",null);
            grandTotal=bundle.getString("GRAND_TOTAL",null);
            appliedCoupen=bundle.getString("COUPEN",null);

            return true;
        }
        else
            return false;

    }
    private void getSharedPreferenceData()
    {
        SharedPreferences preferences=getSharedPreferences("LOGIN_PREFERENCE",MODE_PRIVATE);
        authKey=preferences.getString("AUTH_KEY",null);
        userId=preferences.getString("USER_ID",null);
        userName=preferences.getString("USER_NAME",null);
        userEmail=preferences.getString("EMAIL_ID",null);

        mProfileName.setText(userName);

        //callViewCartAPI(authKey,userId);
        callViewUserProfileAPI(authKey,userId);

        if(appliedCoupen!=null) {
            callViewCartAfterCoupenAppliedAPI(appliedCoupen);
        }
        else {
            callViewCartAPI(authKey,userId);

           // Snackbar.make(mCheckOut,"Coupen is empty",Snackbar.LENGTH_LONG).show();

        }


    }

    private void callViewUserProfileAPI(String authKey,String userId)
    {
        ViewUserProfile viewUserProfile=new ViewUserProfile(userId);
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            /*mInternetStatusText.setVisibility(View.GONE);*/
            WebServices<ViewUserProfileResponse> webServices = new WebServices<ViewUserProfileResponse>(PaymentDetailsActivity.this);
            webServices.viewUserProfile(WebServices.BASE_URL, WebServices.ApiType.viewUserProfile,authKey,userId);
        } else {

          /*  mInternetStatusText.setVisibility(View.VISIBLE);*/
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet)+"", Toast.LENGTH_SHORT).show();
        }

    }

    public void callViewCartAPI(String authKey,String userId)
    {
        ViewCart viewCart=new ViewCart(userId,"");
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            WebServices<ViewCartResponse> webServices = new WebServices<ViewCartResponse>(PaymentDetailsActivity.this);
            webServices.viewCart(WebServices.BASE_URL, WebServices.ApiType.viewCart,authKey,userId,viewCart);
        } else {
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet)+"", Toast.LENGTH_SHORT).show();
        }

    }


    public void callViewCartAfterCoupenAppliedAPI(String appliedCoupen)
    {
        if(appliedCoupen!=null)
        {
            mCoupenAppliedText.setVisibility(View.VISIBLE);
            CoupenAppliedCart appliedCart=new CoupenAppliedCart(userId,appliedCoupen);
            if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

                WebServices<CoupenAppliedResponse> webServices = new WebServices<CoupenAppliedResponse>(PaymentDetailsActivity.this);
                webServices.coupenAppliedResponse(WebServices.BASE_URL, WebServices.ApiType.coupenApplied,authKey,userId,appliedCart);
            } else {
                //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
                Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet)+"", Toast.LENGTH_SHORT).show();
            }

        }
        else {
            mCoupenAppliedText.setVisibility(View.GONE);
        }



    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        mCheckOut.setVisibility(View.VISIBLE);
        callViewUserProfileAPI(authKey,userId);

       //initializeViews();
       // callViewCartAPI(authKey,userId);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.vB_aac_checkout:
                if(!mTotalAmount.getText().toString().equalsIgnoreCase(""))
                {
                   /* startActivity(SelectAddressActivity.class);*/

                    ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this,null);
                    Intent coupenIntent=new Intent(this,SelectAddressActivity.class);
                    coupenIntent.putExtra("FROM","PAYMENTDETAILS");
                    startActivity(coupenIntent,transitionActivityOptions.toBundle());

                }
                else
                {
                    Snackbar.make(mCheckOut,"Insufficient Amount",Snackbar.LENGTH_LONG).show();
                }

                break;
                
            case R.id.vT_aac_apply_coupen:
                applyCoupen();
                break;
                
           /* case R.id.vI_aac_back_icon:
                onBackPressed();
                break;*/

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

    private void openNavigationDrawer()
    {
        hideKeyBoard();
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);

        }

    }
    private void closeNavigationDrawer()
    {
        hideKeyBoard();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        }

    }

    private void hideKeyBoard()
    {
        try {

            //InputMethodManager is used to hide the virtual keyboard from the user after finishing the user input
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isAcceptingText()) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        } catch (NullPointerException e) {
            Log.e("Exception", e.getMessage() + ">>");

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

                ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(PaymentDetailsActivity.this,null);

                Intent intent=new Intent(PaymentDetailsActivity.this,LogInActivity.class);
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

    private void applyCoupen() {

        mCheckOut.setVisibility(View.GONE);

        startActivity(CoupensActivity.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Transition transition;
        //transition = buildEnterTransition();
        transition = TransitionInflater.from(this).inflateTransition(R.transition.slide_and_changebounds_sequential);

       /* if (type == TYPE_PROGRAMMATICALLY) {
            transition = buildEnterTransition();
        }  else {
            transition = TransitionInflater.from(this).inflateTransition(R.transition.explode);
        }*/
        getWindow().setEnterTransition(transition);
        //getWindow().setReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.explode));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Visibility buildReturnTransition() {
        Visibility enterTransition = new Slide();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        return enterTransition;
    }
    private void startActivity(Class<?> tClass)
    {
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this,null);
        startActivity(new Intent(PaymentDetailsActivity.this,tClass),transitionActivityOptions.toBundle());
        //overridePendingTransition(R.anim.slide_in,R.anim.slide_in);
        //this.finish();
    }

    private void toggleVisibility(View... views)
    {
        for (View v:views)
        {
            if(v.getVisibility()==View.VISIBLE)
            {
                v.setVisibility(View.GONE);
            }
            else {
                v.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {

        switch (URL)
        {
            case viewCart:
                if(isSucces) {
                    ViewCartResponse cartResponse = (ViewCartResponse) response;
                    if (cartResponse != null) {
                        if(cartResponse.getResponsecode()!=null) {

                            if (cartResponse.getResponsecode().equalsIgnoreCase("200")) {

                                updatePrice(cartResponse);

                                //mTotalAmount.setText(String.valueOf(cartResponse.getGrandTotal()));
                                //Something went Wrong
                            }
                        }
                    }

                } else {
                    //API call Failure
                    //Snackbar.make(mStartShopping, "API call failed", Snackbar.LENGTH_LONG).show();
                }

                break;

            case coupenApplied:
                CoupenAppliedResponse coupenAppliedResponse = (CoupenAppliedResponse) response;
                if (isSucces) {

                    if (coupenAppliedResponse != null) {
                        if (coupenAppliedResponse.getResponsecode() != null) {
                            if (coupenAppliedResponse.getResponsecode().equalsIgnoreCase("200")) {

                                    //updatePricesAfterApplyingCoupen(coupenAppliedResponse);

                                    if(coupenAppliedResponse.getViewCartMessage()!=null)
                                    {
                                        if(coupenAppliedResponse.getViewCartMessage().equalsIgnoreCase("Min cart failed"))
                                        {
                                            callViewCartAPI(authKey,userId);

                                        }
                                        else {
                                            updatePricesAfterApplyingCoupen(coupenAppliedResponse);

                                        }


                                        //Snackbar.make(mCheckOut, coupenAppliedResponse.getViewCartMessage(), Snackbar.LENGTH_LONG).show();

                                    }
                                    else {
                                        updatePricesAfterApplyingCoupen(coupenAppliedResponse);

                                    }

                                //updatePricesAfterApplyingCoupen(coupenAppliedResponse);
                                if(coupenAppliedResponse.getCouponMessage()!=null)
                                {

                                    Snackbar.make(mCheckOut, coupenAppliedResponse.getCouponMessage(), Snackbar.LENGTH_LONG).show();
                                }
                                //Snackbar.make(mCheckOut, "Coupen applied", Snackbar.LENGTH_LONG).show();
                                //mTotalAmount.setText(String.valueOf(cartResponse.getGrandTotal()));
                                //Something went Wrong
                            }
                            else if(coupenAppliedResponse.getResponsecode().equalsIgnoreCase("501"))
                            {
                                //updatePricesAfterApplyingCoupen(coupenAppliedResponse);
                                callViewCartAPI(authKey,userId);
                                if(coupenAppliedResponse.getViewCartMessage()!=null)
                                {
                                    Snackbar.make(mCheckOut, coupenAppliedResponse.getViewCartMessage(), Snackbar.LENGTH_LONG).show();

                                }

                            }
                                else {
                                if(coupenAppliedResponse.getCouponMessage()!=null)
                                {
                                    Snackbar.make(mCheckOut, coupenAppliedResponse.getCouponMessage(), Snackbar.LENGTH_LONG).show();
                                }
                                updateViewsForFailureCase();
                                //Snackbar.make(mCheckOut, "Coupen can not be applied", Snackbar.LENGTH_LONG).show();

                            }
                        }
                    } else {
                        //Snackbar.make(mCheckOut, "null response", Snackbar.LENGTH_LONG).show();

                    }

                } else {
                    //API call Failure
                    if(coupenAppliedResponse!=null)
                    {
                        if(coupenAppliedResponse.getCouponMessage()!=null)
                        {
                            Snackbar.make(mCheckOut, coupenAppliedResponse.getCouponMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }

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
                                List<Response> userinfoList = viewUserProfileResponse.getResponse();
                                if(userinfoList!=null) {
                                    updateUserProfile(userinfoList);
                                }else {
                                    if (viewUserProfileResponse.getProfileMessage() != null) {
                                        Toast.makeText(this, viewUserProfileResponse.getProfileMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                if(viewUserProfileResponse.getProfileMessage()!=null)
                                {
                                    Toast.makeText(this, viewUserProfileResponse.getProfileMessage(), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(this, viewUserProfileResponse.getProfileMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    //API call failed
                }
                break;
        }

    }

    private void updateViewsForFailureCase() {

        mCoupenAppliedText.setVisibility(View.GONE);
        callViewCartAPI(authKey,userId);
    }

    private void updatePricesAfterApplyingCoupen(CoupenAppliedResponse coupenAppliedResponse) {

        appliedCoupen=null;
       /* if(coupenAppliedResponse.getViewCartMessage()!=null)
        {
            if(coupenAppliedResponse.getViewCartMessage().equalsIgnoreCase("View cart success"))
            {
                mCheckOut.setVisibility(View.VISIBLE);
            }

        }*/
        mCoupenAppliedText.setVisibility(View.VISIBLE);

        mCheckOut.setVisibility(View.VISIBLE);
        mSubTotal.setText(String.valueOf(coupenAppliedResponse.getSubTotal()));
        mGST.setText(String.valueOf(coupenAppliedResponse.getGst()));
        mTotalAmount.setText(String.valueOf(coupenAppliedResponse.getGrandTotal()));
        mDeliveryCharge.setText(String.valueOf(coupenAppliedResponse.getDeliveryCharges()));

        Log.d("discountPrices"," sub total=>"+mSubTotal+"\n total=>"+mTotalAmount);

        SharedPreferences preferences=getSharedPreferences("PAYMENT_DETAILS",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("GRAND_TOTAL",String.valueOf(coupenAppliedResponse.getGrandTotal()));
        editor.putString("APPLIED_ID",String.valueOf(coupenAppliedResponse.getAppliedId()));
        editor.apply();
    }

    private void updatePrice(ViewCartResponse cartResponse) {

        mCoupenAppliedText.setVisibility(View.GONE);

        mCheckOut.setVisibility(View.VISIBLE);
        mSubTotal.setText(String.valueOf(cartResponse.getSubTotal()));
        mGST.setText(String.valueOf(cartResponse.getGst()));
        mTotalAmount.setText(String.valueOf(cartResponse.getGrandTotal()));
        mDeliveryCharge.setText(String.valueOf(cartResponse.getDeliveryCharges()));


        SharedPreferences preferences=getSharedPreferences("PAYMENT_DETAILS",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        //editor.putString("GRAND_TOTAL",grandTotal);
        editor.putString("GRAND_TOTAL",String.valueOf(cartResponse.getGrandTotal()));

        editor.putString("APPLIED_ID",String.valueOf(cartResponse.getAppliedId()));
        editor.apply();
    }

    private void updateUserProfile(List<com.zikrabyte.organic.api_responses.viewuserprofile.Response> mList) {

        userName=mList.get(0).getFname();
        userEmail=mList.get(0).getEmail();
        userPhoneNumber=mList.get(0).getPhone();
        userPicture=mList.get(0).getPicture();

      /*  SharedPreferences preferences=getSharedPreferences("USER_DETAILS",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("USERNAME",userName);
        editor.putString("EMAIL",userEmail);
        editor.putString("PHONE_NUMBER",userPhoneNumber);
        editor.putString("USER_ID",userId);
        editor.apply();*/

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

   /* @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean("MyBoolean", true);
        savedInstanceState.putDouble("myDouble", 1.9);
        savedInstanceState.putInt("MyInt", 1);
        savedInstanceState.putString("MyString", "Welcome back to Android");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        if(savedInstanceState!=null)
        {
            boolean myBoolean = savedInstanceState.getBoolean("MyBoolean");
            double myDouble = savedInstanceState.getDouble("myDouble");
            int myInt = savedInstanceState.getInt("MyInt");
            String myString = savedInstanceState.getString("MyString");

        }

    }*/
}
