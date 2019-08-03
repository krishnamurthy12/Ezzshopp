package com.zikrabyte.organic.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.squareup.picasso.Picasso;
import com.zikrabyte.organic.R;
import com.zikrabyte.organic.api_requests.ApplyReferral;
import com.zikrabyte.organic.api_requests.ViewUserProfile;
import com.zikrabyte.organic.api_responses.applyreferral.ApplyReferralResponse;
import com.zikrabyte.organic.api_responses.viewuserprofile.Response;
import com.zikrabyte.organic.api_responses.viewuserprofile.ViewUserProfileResponse;
import com.zikrabyte.organic.customs.CircleImageView;
import com.zikrabyte.organic.utils.EzzShoppUtils;
import com.zikrabyte.organic.utils.OnResponseListener;
import com.zikrabyte.organic.utils.WebServices;

import java.util.List;

public class ApplyReferralCodeActivity extends AppCompatActivity implements OnResponseListener,View.OnClickListener {

    /*Navigation drawer elements*/
    DrawerLayout drawer;
    NavigationView navigationView;
    ImageView mNavigationMenuIcon,mNavigationBack;
    LinearLayout mApplyNewReferralCodeLayout,mEnteringReferralCodeLayout;
    EditText mEnteredReferralCode;
    Button mApplyReferral;
    ImageView mCancel;
    ProgressBar mProgressBar;

    LinearLayout mHome,mMyProfileLayout,mMyAddressLayout,mMyCartLayout,mMyOrdersLayout,mMyCouponsLayout,mAboutUsLayout,mContactUsLayout,mShareLayout,mRateUsLayout,mLogOutLayout;
    CircleImageView mProfilePic;
    TextView mProfileName;


    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    TextView mReferralCode,mAppliedReferralCode;
    Button mShare;
    //ImageView mActionBack;
    CircleImageView mLogo;

    String authKey,userId,userName,userEmail,userPhoneNumber,userPicture,yourReferralCode,appliedRferralCode;
    String appliedCode=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_referral_code);
        initializeViews();
    }
    private void initializeViews()
    {
        mProgressBar=findViewById(R.id.vP_aar_progress_bar);
        mReferralCode=findViewById(R.id.vT_aar_referral_code);
        mAppliedReferralCode=findViewById(R.id.vT_aar_applied_referral_code);
        mApplyNewReferralCodeLayout =findViewById(R.id.vL_aar_add_new_referral_code);
        mApplyReferral=findViewById(R.id.vB_aar_apply);
        mEnteredReferralCode=findViewById(R.id.vE_aar_applied_referral_code);
        mEnteringReferralCodeLayout=findViewById(R.id.vL_aar_referral_applying_layout);
        mCancel=findViewById(R.id.vI_aar_cancel);

        mShare=findViewById(R.id.vT_aar_share);
        //mActionBack=findViewById(R.id.vI_aar_back_icon);
        mLogo=findViewById(R.id.logo);

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

        mCancel.setOnClickListener(this);

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
        mShare.setOnClickListener(this);
        mApplyNewReferralCodeLayout.setClickable(true);
        mApplyNewReferralCodeLayout.setOnClickListener(this);

        mProgressBar.setVisibility(View.GONE);

        getSharedPreferenceData();
    }

    private void getSharedPreferenceData()
    {
        SharedPreferences preferences=getSharedPreferences("LOGIN_PREFERENCE",MODE_PRIVATE);
        authKey=preferences.getString("AUTH_KEY",null);
        userId=preferences.getString("USER_ID",null);
        userName=preferences.getString("USER_NAME",null);
        userEmail=preferences.getString("EMAIL_ID",null);
        userPhoneNumber=preferences.getString("PHONE_NUMBER",null);
        yourReferralCode=preferences.getString("YOUR_REFERRAL_CODE",null);
        appliedRferralCode=preferences.getString("APPLIED_REFERRAL_CODE",null);

        Log.d("appliedrefferal",appliedRferralCode);

        mProfileName.setText(userName);

        if(yourReferralCode!=null)
        {
            mReferralCode.setText(yourReferralCode);
        }
        if(appliedRferralCode!=null)
        {
            if(appliedRferralCode.equalsIgnoreCase("No referral code"))
            {
                mAppliedReferralCode.setVisibility(View.GONE);
                mApplyNewReferralCodeLayout.setVisibility(View.VISIBLE);
                mEnteringReferralCodeLayout.setVisibility(View.GONE);

            }
            else
            {
                mAppliedReferralCode.setVisibility(View.VISIBLE);
                mApplyNewReferralCodeLayout.setVisibility(View.GONE);
                mEnteringReferralCodeLayout.setVisibility(View.GONE);
                mAppliedReferralCode.setText(appliedRferralCode);
            }

        }


        callViewUserProfileAPI(authKey,userId);

        //callApplyReferralAPI(authKey,userId);
    }

    private void callViewUserProfileAPI(String authKey,String userId)
    {
        ViewUserProfile viewUserProfile=new ViewUserProfile(userId);
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            /*mInternetStatusText.setVisibility(View.GONE);*/
            WebServices<ViewUserProfileResponse> webServices = new WebServices<ViewUserProfileResponse>(ApplyReferralCodeActivity.this);
            webServices.viewUserProfile(WebServices.BASE_URL, WebServices.ApiType.viewUserProfile,authKey,userId);
        } else {

          /*  mInternetStatusText.setVisibility(View.VISIBLE);*/
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet)+"", Toast.LENGTH_SHORT).show();
        }

    }

    private void callApplyReferralAPI(String yourReferralCode)
    {
        ApplyReferral applyReferral=new ApplyReferral(userId,yourReferralCode);
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())){
            mProgressBar.setVisibility(View.VISIBLE);

        WebServices<ApplyReferralResponse> webServices = new WebServices<ApplyReferralResponse>(ApplyReferralCodeActivity.this);
        webServices.applyReferralCode(WebServices.BASE_URL, WebServices.ApiType.applyReferralCode,authKey,userId,applyReferral);
    } else {

        //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
        Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet)+"", Toast.LENGTH_SHORT).show();
    }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        Intent backIntent=new Intent(this,HomeActivity.class);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(backIntent);
    }

    @Override
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {

        switch (URL)
        {
            case applyReferralCode:
                if(mProgressBar.isShown())
                {
                    mProgressBar.setVisibility(View.GONE);
                }
                ApplyReferralResponse applyReferralResponse = (ApplyReferralResponse) response;
                if (isSucces) {
                    if (applyReferralResponse != null)
                    {
                        if (applyReferralResponse.getReferralCode()!=null && applyReferralResponse.getResponsecode() != null) {

                            if (applyReferralResponse.getResponsecode().equalsIgnoreCase("200")) {

                                updateUI(applyReferralResponse);

                                if (applyReferralResponse.getReferralMessage() != null) {
                                    Snackbar.make(mShare,applyReferralResponse.getReferralMessage(),Snackbar.LENGTH_LONG).show();
                                }

                                //yourReferralCode=applyReferralResponse.getReferralCode();
                                //mAppliedReferralCode.setText(applyReferralResponse.getReferralCode());

                            }
                            else if(applyReferralResponse.getResponsecode().equalsIgnoreCase("200"))
                            {
                                if (applyReferralResponse.getReferralMessage() != null) {
                                    Snackbar.make(mShare,applyReferralResponse.getReferralMessage(),Snackbar.LENGTH_LONG).show();
                                }

                            }
                            else {
                                if (applyReferralResponse.getReferralMessage() != null) {
                                    Snackbar.make(mShare,applyReferralResponse.getReferralMessage(),Snackbar.LENGTH_LONG).show();
                                }
                            }
                        }

                    }

                } else {
                    //API call failed

                    if (applyReferralResponse != null) {
                        if (applyReferralResponse.getReferralMessage() != null) {
                            Snackbar.make(mShare,applyReferralResponse.getReferralMessage(),Snackbar.LENGTH_LONG).show();
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

    private void updateUI(ApplyReferralResponse applyReferralResponse) {

        appliedRferralCode=applyReferralResponse.getReferralCode();

        mAppliedReferralCode.setVisibility(View.VISIBLE);
        mApplyNewReferralCodeLayout.setVisibility(View.GONE);
        mEnteringReferralCodeLayout.setVisibility(View.GONE);
        mAppliedReferralCode.setText(appliedRferralCode);

        SharedPreferences preferences=getSharedPreferences("LOGIN_PREFERENCE",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("AUTH_KEY",authKey);
        editor.putString("USER_ID",userId);
        editor.putString("USER_NAME",userName);
        editor.putString("EMAIL_ID",userEmail);
        editor.putString("YOUR_REFERRAL_CODE",yourReferralCode);
        editor.putString("APPLIED_REFERRAL_CODE",appliedRferralCode);
        editor.apply();



    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
          /*  case R.id.vI_aar_back_icon:
                onBackPressed();
                break;*/
            case R.id.vI_aar_cancel:
                hideLayoutToApply();
                break;
            case R.id.vL_aar_add_new_referral_code:
                showLayoutToApply();
                break;

            case R.id.vB_aar_apply:
                ApplyEnteredReferralCode();
                break;

            case R.id.vT_aar_share:
                //shareDeepLink(yourReferralCode);
                shortlinkBuild(yourReferralCode,"Ezzshopp",1);
                //shareLink();
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

    private void hideLayoutToApply() {
        String enteredCode=mEnteredReferralCode.getText().toString();
        if(enteredCode.length()>0)
        {
            mEnteredReferralCode.setText("");

        }
        else {
            mAppliedReferralCode.setVisibility(View.GONE);
            mApplyNewReferralCodeLayout.setVisibility(View.VISIBLE);
            mEnteringReferralCodeLayout.setVisibility(View.GONE);
        }

    }

    private void ApplyEnteredReferralCode() {

        String enteredCode=mEnteredReferralCode.getText().toString();
        if(enteredCode.isEmpty() || enteredCode.length()<4)
        {
            Snackbar.make(mShare,"Please enter a valid Referral code",Snackbar.LENGTH_LONG).show();
        }
        else {
            /*InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEnteredReferralCode.getWindowToken(), 0);*/
            hideKeyBoard();
            callApplyReferralAPI(enteredCode);
        }

    }

    private void showLayoutToApply() {

      /*  InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEnteredReferralCode, InputMethodManager.SHOW_IMPLICIT);*/

        mApplyNewReferralCodeLayout.setVisibility(View.GONE);
        mEnteringReferralCodeLayout.setVisibility(View.VISIBLE);

        mApplyReferral.setOnClickListener(this);
    }

    private void shareDeepLink(String deepLink) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,"EzzShopp");
        //intent.putExtra(Intent.EXTRA_TEXT, "I have Ezzshopp coupon of worth Rs 50. Signup with my referral code: "+ Html.fromHtml(yourReferralCode)+ " to avail the coupon.");
        intent.putExtra(Intent.EXTRA_TEXT, deepLink);

        startActivity(intent);

    }

    private void shareLink() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,"EzzShopp");
        intent.putExtra(Intent.EXTRA_TEXT, "I have Ezzshopp coupon of worth Rs 50. Signup with my referral code: "+ Html.fromHtml(yourReferralCode)+
                " to avail the coupon Download link \n"+Uri.parse("market://details?id=" + getPackageName()));
        //intent.putExtra(Intent.EXTRA_TEXT, Uri.parse("market://details?id=" + getPackageName()));

        startActivity(intent);

    }



    public void shortlinkBuild(@NonNull String referralCode, @NonNull String title, int minVersion)  {

        String myReferralCode= "<b>" + referralCode + "</b>";

        // Uri video=Uri.parse(referralCode);
       // Log.d("sharingparameters",video.toString()+"\n");
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("http://mob-india.com/ezzshop/"))
                .setDynamicLinkDomain("cu47u.app.goo.gl")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder(getApplicationContext().getPackageName())
                                .setMinimumVersion(minVersion)
                                .build())
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle(title)
                                .setDescription("I have Ezzshopp coupon of worth Rs 50. Signup with my referral code: "+ Html.fromHtml(myReferralCode))
                                //.setImageUrl(Uri.parse("http:/mob-india.com/ezzshop/uploads/280320180117532018-03-26-21-36-17-153.jpg"))
                                .build())
                .buildShortDynamicLink()
                .addOnCompleteListener(new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Log.d("generatedshortlink",shortLink.toString());
                            shareDeepLink(shortLink.toString());
                        } else {
                            // Error
                            // ...
                            Log.d("generatedshortlink","failed to generate short link");
                        }
                    }
                });
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

                ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(ApplyReferralCodeActivity.this,null);

                Intent intent=new Intent(ApplyReferralCodeActivity.this,LogInActivity.class);
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

    private void openNavigationDrawer()
    {
        //hideKeyBoard();
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);

        }

    }
    private void closeNavigationDrawer()
    {
        //hideKeyBoard();
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
    private void startActivity(Class<?> tClass)
    {
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this,null);
        startActivity(new Intent(ApplyReferralCodeActivity.this,tClass),transitionActivityOptions.toBundle());
        //overridePendingTransition(R.anim.slide_in,R.anim.slide_in);
        //this.finish();
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
