package com.zikrabyte.organic.utils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.zikrabyte.organic.R;
import com.zikrabyte.organic.activities.ApplyReferralCodeActivity;
import com.zikrabyte.organic.activities.CartActivity;
import com.zikrabyte.organic.activities.CoupensActivity;
import com.zikrabyte.organic.activities.HomeActivity;
import com.zikrabyte.organic.activities.LogInActivity;
import com.zikrabyte.organic.activities.OrderHistoryActivity;
import com.zikrabyte.organic.activities.SelectAddressActivity;
import com.zikrabyte.organic.activities.UserProfileActivity;
import com.zikrabyte.organic.activities.WebViewActivity;
import com.zikrabyte.organic.customs.CircleImageView;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    public DrawerLayout drawer;
    public NavigationView navigationView;
    public ImageView mNavigationMenuIcon,mNavigationBack;

    public LinearLayout mMyProfileLayout,mMyAddressLayout,mMyCartLayout,mMyOrdersLayout,mMyCouponsLayout,mAboutUsLayout,mContactUsLayout,mShareLayout,mRateUsLayout,mLogOutLayout;
    public CircleImageView mProfilePic;
    public TextView mProfileName;

    public FrameLayout mFrameLayout;

    public String authKey,userId,userName,userEmail,userPhoneNumber,userPicture;
    boolean isLoggedIn=false;


    public AlertDialog.Builder builder;
    public AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
       initializeViews();
    }

    private void initializeViews() {

        mFrameLayout=findViewById(R.id.content_frame);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

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

        mMyCartLayout.setOnClickListener(this);
        mAboutUsLayout.setOnClickListener(this);
        mContactUsLayout.setOnClickListener(this);
        mRateUsLayout.setOnClickListener(this);



        getSharedPreferenceData();
    }

        private void getSharedPreferenceData()
        {
            SharedPreferences preferences=getSharedPreferences("LOGIN_PREFERENCE",MODE_PRIVATE);
            authKey=preferences.getString("AUTH_KEY",null);
            userId=preferences.getString("USER_ID",null);
            userName=preferences.getString("USER_NAME",null);
            userEmail=preferences.getString("EMAIL_ID",null);

            isLoggedIn= preferences.getBoolean("IS_LOGGEDIN",false);

            if(!isLoggedIn)
            {
                mMyProfileLayout.setClickable(false);
                mMyOrdersLayout.setClickable(false);
                mMyAddressLayout.setClickable(false);
                mMyCouponsLayout.setClickable(false);
                mShareLayout.setClickable(false);
                mLogOutLayout.setClickable(false);

                mMyCartLayout.setClickable(true);
                mAboutUsLayout.setClickable(true);
                mContactUsLayout.setClickable(true);
                mRateUsLayout.setClickable(true);

            }
            else
            {
                    mMyProfileLayout.setOnClickListener(this);
                    mMyOrdersLayout.setOnClickListener(this);
                    mMyAddressLayout.setOnClickListener(this);
                   // mMyCartLayout.setOnClickListener(this);
                    mMyCouponsLayout.setOnClickListener(this);
                    //mAboutUsLayout.setOnClickListener(this);
                    //mContactUsLayout.setOnClickListener(this);
                    mShareLayout.setOnClickListener(this);
                    //mRateUsLayout.setOnClickListener(this);
                    mLogOutLayout.setOnClickListener(this);


            }

            Log.d("AUTHKEY",authKey+"");
            Log.d("USERID",userId+"");

            mProfileName.setText(userName);
    }

   /* @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        actionBarDrawerToggle.syncState();
    }*/


    private Visibility buildReturnTransition() {
        Visibility enterTransition = new Slide();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        return enterTransition;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.homepage_navigation_menu_icon:
                openNavigationDrawer();
                break;

            case R.id.vI_nh_back:
                closeNavigationDrawer();
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

    public void openNavigationDrawer()
    {
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);

        }

    }
    public void closeNavigationDrawer()
    {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        }

    }
/*    public void showExitDialog()
    {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.exit_dialog, null);
        Button mYes=dialogView.findViewById(R.id.vB_ed_yes);
        Button mNo=dialogView.findViewById(R.id.vB_ed_no);

        builder=new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();

        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finishAffinity();

                Visibility returnTransition = buildReturnTransition();
                getWindow().setReturnTransition(returnTransition);

                finishAfterTransition();
                alertDialog.dismiss();

            }
        });
        mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });
    }*/
    public void showLogOutDialog() {

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

                ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(BaseActivity.this,null);

                Intent intent=new Intent(BaseActivity.this,LogInActivity.class);
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
    public void gotoMyProfileActivity() {
        startActivity(UserProfileActivity.class);
    }
    public void goToCartActivity() {
        startActivity(CartActivity.class);
    }
    public void goToAddressActivity()
    {
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this,null);
        Intent coupenIntent=new Intent(this,SelectAddressActivity.class);
        coupenIntent.putExtra("FROM","NAVIGATIONDRAWER");
        startActivity(coupenIntent,transitionActivityOptions.toBundle());
        //startActivity(SelectAddressActivity.class);
    }

    public void goToMyOrdersActivity()
    {
        startActivity(OrderHistoryActivity.class);
    }
    public void goToMyCouponsActivity() {

        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this,null);
        Intent coupenIntent=new Intent(this,CoupensActivity.class);
        coupenIntent.putExtra("FROM","NAVIGATIONDRAWER");
        startActivity(coupenIntent,transitionActivityOptions.toBundle());
        //startActivity(CoupensActivity.class);
    }
    private void startActivity(Class<?> tClass)
    {
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this,null);
        startActivity(new Intent(BaseActivity.this,tClass),transitionActivityOptions.toBundle());
        //overridePendingTransition(R.anim.slide_in,R.anim.slide_in);
        //this.finish();
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
}
