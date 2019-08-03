package com.zikrabyte.organic.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zikrabyte.organic.R;
import com.zikrabyte.organic.adapters.CoupensAdapter;
import com.zikrabyte.organic.api_requests.AllCoupens;
import com.zikrabyte.organic.api_requests.ViewUserProfile;
import com.zikrabyte.organic.api_responses.allcoupens.AllCoupensResponse;
import com.zikrabyte.organic.api_responses.allcoupens.CouponCode;
import com.zikrabyte.organic.api_responses.viewuserprofile.ViewUserProfileResponse;
import com.zikrabyte.organic.customs.CircleImageView;
import com.zikrabyte.organic.utils.EzzShoppUtils;
import com.zikrabyte.organic.utils.OnResponseListener;
import com.zikrabyte.organic.utils.WebServices;

import java.util.ArrayList;
import java.util.List;

public class CoupensActivity extends AppCompatActivity implements View.OnClickListener,OnResponseListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    ImageView mNavigationMenuIcon,mNavigationBack;

    LinearLayout mHome,mMyProfileLayout,mMyAddressLayout,mMyCartLayout,mMyOrdersLayout,mMyCouponsLayout,mAboutUsLayout,mContactUsLayout,mShareLayout,mRateUsLayout,mLogOutLayout;
    CircleImageView mProfilePic;
    TextView mProfileName;

    String authKey,userId,userName,userEmail,userPhoneNumber,userPicture;


    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    RecyclerView recyclerView;
    ImageView mActionBack;
    LinearLayout mEmptyLayout;
    TextView mReferalCode,mRewardCode,mReferalAmount,mRewardAmount;
    RelativeLayout mReferalCodeLayout,mRewardCodeLayout;
    Button mApply;/*mReferralApply*/;
    CheckBox mReferalCodeCheckBox,mRewardCodeCheckBox;
    LinearLayout mReferralRewardLayout;

    LinearLayoutManager layoutManager;
    CoupensAdapter adapter;
    List<CouponCode> mList=new ArrayList<>();

    boolean isFromNavigationDrawer=false;
    String fromINtent=null;
    int selectedPosition=0;
    boolean selectedFromActivity=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupens);
        initializeViews();
    }

    private void initializeViews() {

        mReferralRewardLayout=findViewById(R.id.vL_referral_reward_layout);
        mRewardCodeLayout=findViewById(R.id.reward_code_layout);
        mReferalCodeLayout=findViewById(R.id.referal_code_layout);
        //mReferralApply=findViewById(R.id.vT_ssa_referral_apply);
        //disableViews(mReferralApply);

        mRewardCodeCheckBox=findViewById(R.id.vC_ac_rewardcode_checkbox);
        mReferalCodeCheckBox=findViewById(R.id.vC_ac_referal_code_checkbox);

        mApply=findViewById(R.id.vT_ssa_coupen_apply);
        disableViews(mApply);

        mReferalCode=findViewById(R.id.vT_ac_referal_code);
        mRewardCode=findViewById(R.id.vT_ac_reward_code);
        mReferalAmount=findViewById(R.id.vT_ac_referal_amount);
        mRewardAmount=findViewById(R.id.vT_ac_reward_amount);

        recyclerView=findViewById(R.id.vR_ac_recycler_view);
       // mActionBack=findViewById(R.id.vI_ac_back_icon);
        mEmptyLayout=findViewById(R.id.vL_ac_empty_layout);

        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

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

        //mActionBack.setOnClickListener(this);
        mApply.setOnClickListener(this);
       // mReferralApply.setOnClickListener(this);

        if(receiveIntentData())
        {
            if(fromINtent!=null){
                if(fromINtent.equalsIgnoreCase("NAVIGATIONDRAWER")){
                    //Invisible
                    isFromNavigationDrawer=true;

                    mReferalCodeCheckBox.setVisibility(View.GONE);
                    mRewardCodeCheckBox.setVisibility(View.GONE);

                    mRewardCodeLayout.setClickable(false);
                    mReferalCodeLayout.setClickable(false);

                }else{
                    isFromNavigationDrawer=false;
                    mReferalCodeCheckBox.setVisibility(View.VISIBLE);
                    mRewardCodeCheckBox.setVisibility(View.VISIBLE);

                    mRewardCodeLayout.setClickable(true);
                    mReferalCodeLayout.setClickable(true);

                    mReferalCodeCheckBox.setOnClickListener(this);
                    mRewardCodeCheckBox.setOnClickListener(this);

                    mRewardCodeLayout.setOnClickListener(this);
                    mReferalCodeLayout.setOnClickListener(this);
                }
            }

        }
        else {

                isFromNavigationDrawer=false;
                mReferalCodeCheckBox.setVisibility(View.VISIBLE);
                mRewardCodeCheckBox.setVisibility(View.VISIBLE);

                mRewardCodeLayout.setClickable(true);
                mReferalCodeLayout.setClickable(true);

                mReferalCodeCheckBox.setOnClickListener(this);
                mRewardCodeCheckBox.setOnClickListener(this);

                mRewardCodeLayout.setOnClickListener(this);
                mReferalCodeLayout.setOnClickListener(this);
        }
        getSharedPreferenceData();

    }

    private void getSharedPreferenceData()
    {
        SharedPreferences preferences=getSharedPreferences("LOGIN_PREFERENCE",MODE_PRIVATE);
        authKey=preferences.getString("AUTH_KEY",null);
        userId=preferences.getString("USER_ID",null);
        userName=preferences.getString("USER_NAME",null);
        userEmail=preferences.getString("EMAIL_ID",null);

        mProfileName.setText(userName);

        callViewUserProfileAPI(authKey,userId);

        callGetAllCoupensAPI();

    }

    private boolean receiveIntentData()
    {
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            fromINtent=bundle.getString("FROM",null);
            return true;

        }
        return false;


    }

    private void callViewUserProfileAPI(String authKey,String userId)
    {
        ViewUserProfile viewUserProfile=new ViewUserProfile(userId);
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            WebServices<ViewUserProfileResponse> webServices = new WebServices<ViewUserProfileResponse>(CoupensActivity.this);
            webServices.viewUserProfile(WebServices.BASE_URL, WebServices.ApiType.viewUserProfile,authKey,userId);
        } else {

            Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet)+"", Toast.LENGTH_SHORT).show();
        }

    }

    private void callGetAllCoupensAPI() {

        AllCoupens allCoupens=new AllCoupens(userId);
        //LogIn logIn=new LogIn(mPhoneNumber.getText().toString(),mPassword.getText().toString());
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            WebServices<AllCoupensResponse> webServices = new WebServices<AllCoupensResponse>(CoupensActivity.this);
            webServices.getAllCoupens(WebServices.BASE_URL, WebServices.ApiType.allCoupens,authKey,userId,allCoupens);

        } else {
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, R.string.err_msg_nointernet+"", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
           /* case R.id.vI_ac_back_icon:
                onBackPressed();
                break;*/

            case R.id.referal_code_layout:
                enableViews(mApply);
                selectedPosition=1;
                if(mRewardCodeCheckBox.isChecked())
                {
                    mRewardCodeCheckBox.setChecked(false);
                }
                mReferalCodeCheckBox.setChecked(true);
                break;

            case R.id.vC_ac_referal_code_checkbox:
                if(mRewardCodeCheckBox.isChecked())
                {
                    mRewardCodeCheckBox.setChecked(false);
                }
                selectedPosition=1;
                enableViews(mApply);
                break;

            case R.id.reward_code_layout:
                selectedPosition=2;
                enableViews(mApply);
                if(mReferalCodeCheckBox.isChecked())
                {
                    mReferalCodeCheckBox.setChecked(false);
                }
               mRewardCodeCheckBox.setChecked(true);
                break;
            case R.id.vC_ac_rewardcode_checkbox:

                if(mReferalCodeCheckBox.isChecked())
                {
                    mReferalCodeCheckBox.setChecked(false);
                }
                selectedPosition=2;
                enableViews(mApply);
                break;

            case R.id.vT_ssa_coupen_apply:
               applySelectedCoupen();
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

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void startActivity(Class<?> tClass)
    {
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this,null);
        startActivity(new Intent(CoupensActivity.this,tClass),transitionActivityOptions.toBundle());
        //overridePendingTransition(R.anim.slide_in,R.anim.slide_in);
        //this.finish();
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

                ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(CoupensActivity.this,null);

                Intent intent=new Intent(CoupensActivity.this,LogInActivity.class);
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
    @Override
    protected void onRestart() {
        super.onRestart();
        callViewUserProfileAPI(authKey,userId);
    }



    @Override
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {

        switch (URL) {
            case allCoupens:
                if (isSucces) {
                    AllCoupensResponse allCoupensResponse = (AllCoupensResponse) response;
                    if (allCoupensResponse != null) {
                        if (allCoupensResponse.getResponsecode() != null)
                        {
                            if (allCoupensResponse.getResponsecode().equalsIgnoreCase("200"))
                            {
                                if (allCoupensResponse.getReferralAmt() != null) {
                                    if (!allCoupensResponse.getReferralAmt().equalsIgnoreCase("0")) {
                                        mList.add(new CouponCode(allCoupensResponse.getReferralCode(), allCoupensResponse.getReferralAmt(), "Referal code",allCoupensResponse.getReferralType()));
                                        // showReferalCodeLayout(allCoupensResponse);
                                    } else {
                                        hideReferalCodeLayout();
                                    }
                                }

                                    if (allCoupensResponse.getRewardAmt()!=null) {
                                        if (!allCoupensResponse.getRewardAmt().equalsIgnoreCase("0")) {
                                            mList.add(new CouponCode(allCoupensResponse.getEarnedRewardCode(), allCoupensResponse.getRewardAmt(), "Reward code",allCoupensResponse.getRewardDiscountType()));

                                            // swhoRewardCodeLayout(allCoupensResponse);
                                        } else {
                                            hideRewardCodeLayout();
                                        }
                                    }

                                if(allCoupensResponse.getCouponCodes()!=null)
                                {
                                  //  mList = allCoupensResponse.getCoupons();
                                    for (int i=0;i<allCoupensResponse.getCouponCodes().size();i++){

                                        mList.add(new CouponCode(allCoupensResponse.getCouponCodes().get(i).getCouponCode(),allCoupensResponse.getCouponCodes().get(i).getCouponAmt(),
                                                allCoupensResponse.getCouponCodes().get(i).getCouponDescription(),allCoupensResponse.getCouponCodes().get(i).getCouponType()));
                                    }

                                    if (mList.isEmpty()) {
                                        showEmptyLayout();
                                    } else {
                                        showCoupens(mList);
                                    }

                                }
                                else {
                                    showEmptyLayout();
                                    //showCoupens(mList);
                                }


                            } else {
                                //if response code not equal to 200
                                if (allCoupensResponse.getCouponsMessage() != null) {
                                    Toast.makeText(this, allCoupensResponse.getCouponsMessage(), Toast.LENGTH_SHORT).show();

                                }

                            }
                        }
                    }

                }
                else {
                    //API call failed
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

    private void hideRewardCodeLayout() {

        mRewardCodeLayout.setVisibility(View.GONE);
    }

    private void swhoRewardCodeLayout(AllCoupensResponse allCoupensResponse) {

        mReferralRewardLayout.setVisibility(View.VISIBLE);
        mRewardCodeLayout.setVisibility(View.VISIBLE);

       /* mReferalCode.setText(allCoupensResponse.getReferralCode());
        mReferalAmount.setText(String.valueOf(allCoupensResponse.getReferralAmt()));*/
        mRewardCode.setText(allCoupensResponse.getEarnedRewardCode());
        mRewardAmount.setText(allCoupensResponse.getRewardAmt());
    }

    private void hideReferalCodeLayout() {

        //mReferralRewardLayout.setVisibility(View.GONE);

        mReferalCodeLayout.setVisibility(View.GONE);
    }

    private void showReferalCodeLayout(AllCoupensResponse allCoupensResponse) {

        //mReferralRewardLayout.setVisibility(View.VISIBLE);
        mReferalCodeLayout.setVisibility(View.VISIBLE);

        mReferalCode.setText(allCoupensResponse.getReferralCode());
        mReferalAmount.setText(String.valueOf(allCoupensResponse.getReferralAmt()));
       /* mRewardCode.setText(allCoupensResponse.getEarnedRewardCode());
        mRewardAmount.setText(allCoupensResponse.getRewardDiscountAmt());*/
    }

    private void showCoupens(List<CouponCode> mList) {
        recyclerView.setVisibility(View.VISIBLE);
        mEmptyLayout.setVisibility(View.GONE);
        mApply.setVisibility(View.VISIBLE);

        adapter=new CoupensAdapter(mList,this,mApply,isFromNavigationDrawer);
        recyclerView.setAdapter(adapter);

    }
    private void showEmptyLayout() {

        mReferralRewardLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        mEmptyLayout.setVisibility(View.VISIBLE);
        mApply.setVisibility(View.GONE);

        //mEmptyLayout.setVisibility(View.VISIBLE);

    }

    private void applySelectedCoupen() {

        if(selectedPosition==1)
        {
           /* selectedFromActivity=true;*/
            gotoPaymentActivityWithCoupen(mReferalCode.getText().toString());

        }
        else if(selectedPosition==2)
        {
            gotoPaymentActivityWithCoupen(mRewardCode.getText().toString());
        }
        else {
            Toast.makeText(this, "Please select a Coupen to Apply", Toast.LENGTH_SHORT).show();
        }
    }

    private void gotoPaymentActivityWithCoupen(String coupen) {

        //Toast.makeText(this, coupen, Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this, PaymentDetailsActivity.class);
        intent.putExtra("COUPEN",coupen);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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

    private void enableViews(View... views) {
        for (View v : views) {
            v.setEnabled(true);
            v.setBackground(this.getResources().getDrawable(R.drawable.roundeded_button));

        }
    }

    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
            v.setBackground(this.getResources().getDrawable(R.drawable.disabled_button));
        }
    }

      @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        if(mReferalCodeCheckBox.isChecked())
        {
            savedInstanceState.putBoolean("referal_checkbox_checked", true);
        }
        else {
            savedInstanceState.putBoolean("referal_checkbox_checked", false);
        }
          if(mRewardCodeCheckBox.isChecked())
          {
              savedInstanceState.putBoolean("reward_checkbox_checked", true);
          }else {
              savedInstanceState.putBoolean("reward_checkbox_checked", false);
          }
       /* savedInstanceState.putDouble("myDouble", 1.9);
        savedInstanceState.putInt("MyInt", 1);
        savedInstanceState.putString("MyString", "Welcome back to Android");*/
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        if(savedInstanceState!=null)
        {
            boolean rewardCheck = savedInstanceState.getBoolean("reward_checkbox_checked");
            boolean referalCheck = savedInstanceState.getBoolean("referal_checkbox_checked");
            if(rewardCheck)
            {
                mRewardCodeCheckBox.setChecked(true);
            }
            else {
                mRewardCodeCheckBox.setChecked(false);
            }
            if(referalCheck)
            {
                mReferalCodeCheckBox.setChecked(true);
            }
            else {
                mReferalCodeCheckBox.setChecked(false);
            }

        }

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
}



