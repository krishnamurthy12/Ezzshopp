package com.zikrabyte.organic.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zikrabyte.organic.R;
import com.zikrabyte.organic.adapters.HomePageAdapter;
import com.zikrabyte.organic.adapters.MyPagerAdapter;
import com.zikrabyte.organic.api_requests.ViewCart;
import com.zikrabyte.organic.api_requests.ViewUserProfile;
import com.zikrabyte.organic.api_responses.categories.CategoriesResponse;
import com.zikrabyte.organic.api_responses.categories.Response;
import com.zikrabyte.organic.api_responses.viewcart.ViewCartResponse;
import com.zikrabyte.organic.api_responses.viewuserprofile.ViewUserProfileResponse;
import com.zikrabyte.organic.beanclasses.AddToCartDummy;
import com.zikrabyte.organic.beanclasses.ItemDescription;
import com.zikrabyte.organic.customs.CircleImageView;
import com.zikrabyte.organic.database.TinyDB;
import com.zikrabyte.organic.utils.BaseActivity;
import com.zikrabyte.organic.utils.EzzShoppUtils;
import com.zikrabyte.organic.utils.MySharedPreference;
import com.zikrabyte.organic.utils.OnResponseListener;
import com.zikrabyte.organic.utils.WebServices;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements View.OnClickListener, OnResponseListener {

    boolean isLoggedIn = false;
    ArrayList<AddToCartDummy> savedList = new ArrayList<>();
    MySharedPreference mySharedPreference = new MySharedPreference();
    public static ArrayList<AddToCartDummy> addToCartList = new ArrayList<>();

    /*Navigation drawer elements*/
    DrawerLayout drawer;
    NavigationView navigationView;
    boolean doubleBackToExitPressedOnce = false;

    /*Views*/
    ImageView mNavigationMenuIcon, mNavigationBack, mSearchIcon;
    FrameLayout mCartIcon;
    static TextView mcartValue;

    LinearLayout mHomeLayout, mMyProfileLayout, mMyAddressLayout, mMyCartLayout, mMyOrdersLayout, mMyCouponsLayout, mAboutUsLayout,
            mContactUsLayout, mShareLayout, mRateUsLayout, mLogOutLayout,mLogInLayout;
    CircleImageView mProfilePic;
    TextView mProfileName;
    ProgressBar mProgressBar;
    TextView mInternetStatusText;
    //Button mRetry;

    //Text views for disable for guest user case
    TextView mMyAddress, mLogOut, mMyProfile, mMyOrders, mMyCoupons, mReferralCode;

    RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    HomePageAdapter adapter;

    TabLayout mTabLayout;
    ViewPager viewPager;
    MyPagerAdapter myPagerAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    List<ItemDescription> mList = new ArrayList<>();

    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    String authKey, userId, userName, userEmail, userPhoneNumber, userPicture;
    List<Response> mCategoriesList = new ArrayList<Response>();

    Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       /* LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.content_main, null, false);
        mFrameLayout.addView(contentView);*/
        //setupWindowAnimations();
        initializeViews();
        savedList = mySharedPreference.getFavorites(this);
        if (mySharedPreference.getFavorites(this) != null) {
            addToCartList = mySharedPreference.getFavorites(this);
        }
        //initializeTabLayout();

    }


    private void initializeViews() {
        mMyAddress = findViewById(R.id.vT_nh_my_address);
        mMyProfile = findViewById(R.id.vT_nh_my_profile);
        mLogOut = findViewById(R.id.vT_nh_log_out);
        mMyCoupons = findViewById(R.id.vT_nh_my_coupons);
        mMyOrders = findViewById(R.id.vT_nh_my_orders);
        mReferralCode = findViewById(R.id.vT_nh_referral_code);


        mTabLayout = findViewById(R.id.home_page_tablayout);
        viewPager = findViewById(R.id.tbt_viewpager);
        // mRetry=findViewById(R.id.vB_cm_retry);

        //mSwipeRefreshLayout=findViewById(R.id.vS_swipe_refresh_home_page);

        //mSwipeRefreshLayout.setOnRefreshListener(this);

        mProgressBar = findViewById(R.id.vP_cm_progressbar);
        mInternetStatusText = findViewById(R.id.vT_cm_textview);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        mHomeLayout = findViewById(R.id.vL_nh_home_layout);
        mMyProfileLayout = findViewById(R.id.vL_nh_myprofile_layout);
        mMyAddressLayout = findViewById(R.id.vL_nh_myaddress_layout);
        mMyCartLayout = findViewById(R.id.vL_nh_mycart_layout);
        mMyOrdersLayout = findViewById(R.id.vL_nh_myorders_layout);
        mMyCouponsLayout = findViewById(R.id.vL_nh_mycoupons_layout);
        mAboutUsLayout = findViewById(R.id.vL_nh_aboutus_layout);
        mContactUsLayout = findViewById(R.id.vL_nh_contactus_layout);
        mShareLayout = findViewById(R.id.vL_nh_share_layout);
        mLogOutLayout = findViewById(R.id.vL_nh_logout_layout);
        mRateUsLayout = findViewById(R.id.vL_nh_rateus_layout);
        mLogInLayout=findViewById(R.id.vL_nh_login_layout);

        mProfilePic = findViewById(R.id.vIV_nh_profile_pic);
        mProfileName = findViewById(R.id.vT_nh_profile_name);


        mNavigationMenuIcon = findViewById(R.id.homepage_navigation_menu_icon);
        mNavigationBack = findViewById(R.id.vI_nh_back);

       /* mEditProfilePic=findViewById(R.id.vF_nh_edit_profile_picture);
        mEditName=findViewById(R.id.vF_nh_edit_user_name);
        mEditEmail=findViewById(R.id.vF_nh_edit_user_email);*/


        mCartIcon = findViewById(R.id.homepage_cart_icon_layout);
        mSearchIcon = findViewById(R.id.homepage_search_icon);
        mcartValue = findViewById(R.id.vT_tl_cart_value);

        //mRetry.setOnClickListener(this);


       /* recyclerView=findViewById(R.id.recyclerview_homepage);
        layoutManager=new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mList=getList();
        adapter=new HomePageAdapter(this,mList);
        recyclerView.setAdapter(adapter);*/
       /* DividerItemDecoration itemDecoration=new DividerItemDecoration(this,0);
        recyclerView.addItemDecoration(itemDecoration);*/

        mNavigationMenuIcon.setOnClickListener(this);
        mNavigationBack.setOnClickListener(this);

        mHomeLayout.setOnClickListener(this);
        //mMyProfileLayout.setOnClickListener(this);
        //mMyOrdersLayout.setOnClickListener(this);
        //mMyAddressLayout.setOnClickListener(this);
        mMyCartLayout.setOnClickListener(this);
        //mMyCouponsLayout.setOnClickListener(this);
        mAboutUsLayout.setOnClickListener(this);
        mContactUsLayout.setOnClickListener(this);
        //mShareLayout.setOnClickListener(this);
        mRateUsLayout.setOnClickListener(this);
        //mLogOutLayout.setOnClickListener(this);

        mSearchIcon.setOnClickListener(this);
        mCartIcon.setOnClickListener(this);

       /* mEditProfilePic.setOnClickListener(this);
        mEditName.setOnClickListener(this);
        mEditEmail.setOnClickListener(this);*/

        getSharedPreferenceData();


    }

    private void getSharedPreferenceData() {
        SharedPreferences preferences = getSharedPreferences("LOGIN_PREFERENCE", MODE_PRIVATE);
        authKey = preferences.getString("AUTH_KEY", null);
        userId = preferences.getString("USER_ID", null);
        userName = preferences.getString("USER_NAME", null);
        userEmail = preferences.getString("EMAIL_ID", null);

        isLoggedIn = preferences.getBoolean("IS_LOGGEDIN", false);

        if (!isLoggedIn) {
            mProfileName.setVisibility(View.GONE);
            Picasso.get()
                    .load(R.drawable.user)
                    .placeholder(R.drawable.background_drawable)
                    .error(R.drawable.user)
                    .resize(150, 150)
                    .into(mProfilePic);

            if (savedList != null) {
                HomeActivity.setCartValue(savedList.size());
            }

            mLogOutLayout.setVisibility(View.GONE);
            mLogInLayout.setVisibility(View.VISIBLE);

            mLogInLayout.setOnClickListener(this);

            disableViews(mMyAddress);
            disableViews(mMyProfile);
            disableViews(mMyOrders);
            disableViews(mMyCoupons);
            disableViews(mReferralCode);
            disableViews(mLogOut);
        }

        Log.d("AUTHKEY", authKey + "");
        Log.d("USERID", userId + "");

        mProfileName.setText(userName);

        callGetAllCategoriesAPI(authKey, userId);
        callViewUserProfileAPI(authKey, userId);

        if (isLoggedIn) {


            mLogOutLayout.setVisibility(View.VISIBLE);
            mLogInLayout.setVisibility(View.GONE);

            enableViews(mMyAddress);
            enableViews(mMyProfile);
            enableViews(mMyOrders);
            enableViews(mMyCoupons);
            enableViews(mReferralCode);
            enableViews(mLogOut);

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

            callViewCartAPI(authKey, userId);
        }
    }

    private void getRestartSharedPreferenceData() {
        SharedPreferences preferences = getSharedPreferences("LOGIN_PREFERENCE", MODE_PRIVATE);
        authKey = preferences.getString("AUTH_KEY", null);
        userId = preferences.getString("USER_ID", null);
        userName = preferences.getString("USER_NAME", null);
        userEmail = preferences.getString("EMAIL_ID", null);

        isLoggedIn = preferences.getBoolean("IS_LOGGEDIN", false);

        if (!isLoggedIn) {
            mProfileName.setVisibility(View.GONE);
            Picasso.get()
                    .load(R.drawable.user)
                    .placeholder(R.drawable.background_drawable)
                    .error(R.drawable.user)
                    .resize(150, 150)
                    .into(mProfilePic);

            if (savedList != null) {
                HomeActivity.setCartValue(savedList.size());
            }

            mLogOutLayout.setVisibility(View.GONE);
            mLogInLayout.setVisibility(View.VISIBLE);
            mLogInLayout.setOnClickListener(this);

            disableViews(mMyAddress);
            disableViews(mMyProfile);
            disableViews(mMyOrders);
            disableViews(mMyCoupons);
            disableViews(mReferralCode);
            disableViews(mLogOut);
        }

        Log.d("AUTHKEY", authKey + "");
        Log.d("USERID", userId + "");

        mProfileName.setText(userName);

        //callGetAllCategoriesAPI(authKey,userId);
        callViewUserProfileAPI(authKey, userId);
        if (isLoggedIn) {
            mLogOutLayout.setVisibility(View.VISIBLE);
            mLogInLayout.setVisibility(View.GONE);

            enableViews(mMyAddress);
            enableViews(mMyProfile);
            enableViews(mMyOrders);
            enableViews(mMyCoupons);
            enableViews(mReferralCode);
            enableViews(mLogOut);

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

            callViewCartAPI(authKey, userId);
        }
    }

    private void callGetAllCategoriesAPI(String authKey, String userId) {

        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            mInternetStatusText.setVisibility(View.GONE);
            //mRetry.setVisibility(View.GONE);

            mProgressBar.setVisibility(View.VISIBLE);


            WebServices<CategoriesResponse> webServices = new WebServices<CategoriesResponse>(HomeActivity.this);
            webServices.getCategories(WebServices.BASE_URL, WebServices.ApiType.getCategories, authKey, userId);
            /*WebServices<CategoriesResponse> webServices = new WebServices<CategoriesResponse>(HomeActivity.this);
            webServices.getCategories(WebServices.BASE_URL, WebServices.ApiType.getCategories,authKey,userId);*/
        } else {

            mInternetStatusText.setVisibility(View.VISIBLE);
            //mRetry.setVisibility(View.VISIBLE);
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            //Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet)+"", Toast.LENGTH_SHORT).show();
        }

    }

    private void callViewCartAPI(String authKey, String userId) {
        ViewCart viewCart = new ViewCart(userId, "");
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            mInternetStatusText.setVisibility(View.GONE);
            //mRetry.setVisibility(View.GONE);
            WebServices<ViewCartResponse> webServices = new WebServices<ViewCartResponse>(HomeActivity.this);
            webServices.viewCart(WebServices.BASE_URL, WebServices.ApiType.viewCart, authKey, userId, viewCart);
        } else {

            mInternetStatusText.setVisibility(View.VISIBLE);
            //mRetry.setVisibility(View.VISIBLE);
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            //Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet)+"", Toast.LENGTH_SHORT).show();
        }

    }

    private void callViewUserProfileAPI(String authKey, String userId) {
        ViewUserProfile viewUserProfile = new ViewUserProfile(userId);
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            mInternetStatusText.setVisibility(View.GONE);
            //mRetry.setVisibility(View.GONE);
            WebServices<ViewUserProfileResponse> webServices = new WebServices<ViewUserProfileResponse>(HomeActivity.this);
            webServices.viewUserProfile(WebServices.BASE_URL, WebServices.ApiType.viewUserProfile, authKey, userId);
        } else {

            mInternetStatusText.setVisibility(View.VISIBLE);
            //mRetry.setVisibility(View.VISIBLE);
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            //Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet)+"", Toast.LENGTH_SHORT).show();
        }

    }


    private List<ItemDescription> getList() {
        for (int i = 0; i < 20; i++) {

            ItemDescription object = new ItemDescription(R.drawable.delivery, "Carrot", "each", "20", "4",
                    "24", getResources().getString(R.string.large_text));
            mList.add(object);
        }
        return mList;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            showExitDialog();
            //this.finish();
        }
    }

 /*   @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        //Snackbar.make(btnLogin, "press back again to exit", Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }*/


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

           /* case R.id.vB_cm_retry:
                getSharedPreferenceData();
                break;*/

            case R.id.homepage_navigation_menu_icon:
                openNavigationDrawer();
                break;

            case R.id.vI_nh_back:
                closeNavigationDrawer();
                break;

            case R.id.homepage_search_icon:
                goToSearchActivity();
                break;

            case R.id.homepage_cart_icon_layout:
                goToCartActivity();
                break;

            case R.id.vL_nh_home_layout:
                closeNavigationDrawer();
                viewPager.setCurrentItem(0);
                //startActivity(HomeActivity.class);
                //startActivity(NavigationTrialActivity.class);
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

                Intent aboutus = new Intent(this, WebViewActivity.class);
                aboutus.putExtra("aboutUsUrl", "http://www.ezzshopp.com/about-us.php");
                startActivity(aboutus);
                break;

            case R.id.vL_nh_contactus_layout:
                closeNavigationDrawer();

                Intent contactus = new Intent(this, WebViewActivity.class);
                contactus.putExtra("contactUsurl", "http://www.ezzshopp.com/contact.php");
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

            case R.id.vL_nh_login_layout:
                startActivity(LogInActivity.class);
                break;

            default:
                closeNavigationDrawer();
        }

    }

    private void showExitDialog() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.exit_dialog, null);
        Button mYes = dialogView.findViewById(R.id.vB_ed_yes);
        Button mNo = dialogView.findViewById(R.id.vB_ed_no);

        builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();

        mYes.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
    }

    private void showLogOutDialog() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.logout_dialog, null);
        Button cancel = dialogView.findViewById(R.id.vB_lod_cancel);
        Button logOut = dialogView.findViewById(R.id.vB_lod_logout);

        builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(HomeActivity.this, "LogOut", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences = getSharedPreferences("LOGIN_PREFERENCE", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();

                SharedPreferences usePpreferences = getSharedPreferences("USER_DETAILS", MODE_PRIVATE);
                SharedPreferences.Editor editor1 = usePpreferences.edit();
                editor1.clear();
                editor1.apply();

                SharedPreferences paymentDetails = getSharedPreferences("PAYMENT_DETAILS", MODE_PRIVATE);
                SharedPreferences.Editor editor2 = paymentDetails.edit();
                editor2.clear();
                editor2.apply();

                ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(HomeActivity.this, null);

                Intent intent = new Intent(HomeActivity.this, LogInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finishAffinity();
                startActivity(intent, transitionActivityOptions.toBundle());
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

        /*if(isLoggedIn)
        {
            finishAfterTransition();
            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(HomeActivity.this,null);
            Intent mainIntent = new Intent(HomeActivity.this, CartActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent,transitionActivityOptions.toBundle());
            startActivity(CartActivity.class);

        }
        else
        {
            finishAfterTransition();
            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(HomeActivity.this,null);
            Intent mainIntent = new Intent(HomeActivity.this, LogInActivity.class);
            //mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent,transitionActivityOptions.toBundle());
        }*/
    }

    private void goToAddressActivity() {
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, null);
        Intent coupenIntent = new Intent(this, SelectAddressActivity.class);
        coupenIntent.putExtra("FROM", "NAVIGATIONDRAWER");
        startActivity(coupenIntent, transitionActivityOptions.toBundle());
        //startActivity(SelectAddressActivity.class);
    }

    private void goToMyOrdersActivity() {
        startActivity(OrderHistoryActivity.class);
    }

    private void goToMyCouponsActivity() {

        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, null);
        Intent coupenIntent = new Intent(this, CoupensActivity.class);
        coupenIntent.putExtra("FROM", "NAVIGATIONDRAWER");
        startActivity(coupenIntent, transitionActivityOptions.toBundle());
        //startActivity(CoupensActivity.class);
    }

    private void openNavigationDrawer() {
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);

        }

    }

    private void closeNavigationDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        }

    }

    private void goToSearchActivity() {

        startActivity(SearchActivity.class);
    }

    private void startActivity(Class<?> tClass) {
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, null);
        startActivity(new Intent(HomeActivity.this, tClass), transitionActivityOptions.toBundle());
        //overridePendingTransition(R.anim.slide_in,R.anim.slide_in);
        //this.finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Visibility enterTransition = buildEnterTransition();
        getWindow().setEnterTransition(enterTransition);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Visibility buildEnterTransition() {
        Explode enterTransition = new Explode();
        enterTransition.setDuration(1000);
        enterTransition.setMode(Visibility.MODE_IN);
        // This view will not be affected by enter transition animation
        enterTransition.excludeTarget(R.id.toolbar_layout, true);
        return enterTransition;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Visibility buildReturnTransition() {
        Visibility enterTransition = new Slide();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        return enterTransition;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //getSharedPreferenceData();
        getRestartSharedPreferenceData();
       /* if(isLoggedIn) {
            callViewCartAPI(authKey, userId);
            callViewUserProfileAPI(authKey, userId);
        }*/
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (mProgressBar != null) {
            if (mProgressBar.isShown()) {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mProgressBar != null) {
            if (mProgressBar.isShown()) {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {

        switch (URL) {
            case getCategories:
                if (mProgressBar.isShown()) {
                    mProgressBar.setVisibility(View.GONE);
                }
                CategoriesResponse categoriesResponse = (CategoriesResponse) response;
                if (isSucces) {

                    if (categoriesResponse != null) {
                        if (categoriesResponse.getResponsecode() != null) {

                            if (categoriesResponse.getResponsecode().equalsIgnoreCase("200")) {
                                mCategoriesList = categoriesResponse.getResponse();
                                if (mCategoriesList.size() > 0) {

                                    initializeTabLayout(mCategoriesList);
                                    Log.d("categories", mCategoriesList + "");
                                    Log.d("categories", mCategoriesList.size() + "");

                                    //Toast.makeText(this, "" + mCategoriesList.size(), Toast.LENGTH_SHORT).show();
                                } else {

                                }
                            } else {
                                if (categoriesResponse.getResponsecode().equalsIgnoreCase("501")) {
                                    if (categoriesResponse.getResponseMessage() != null) {
                                        Snackbar.make(mSearchIcon, categoriesResponse.getResponseMessage(), Snackbar.LENGTH_LONG).show();

                                        // Toast.makeText(this, categoriesResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                                if (categoriesResponse.getCategoriesMessage() != null) {
                                    Snackbar.make(mSearchIcon, categoriesResponse.getCategoriesMessage(), Snackbar.LENGTH_LONG).show();

                                    // Toast.makeText(this, categoriesResponse.getCategoriesMessage(), Toast.LENGTH_SHORT).show();

                                    showToast(categoriesResponse.getCategoriesMessage());
                                }

                            }
                        }
                    }
                } else {
                    if (categoriesResponse != null) {
                        if (categoriesResponse.getCategoriesMessage() != null) {

                            showToast(categoriesResponse.getCategoriesMessage());
                            //Toast.makeText(this, categoriesResponse.getCategoriesMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }

                }
                break;

            case viewCart:
                ViewCartResponse cartResponse = (ViewCartResponse) response;
                if (isSucces) {
                    if (cartResponse != null) {
                        if (cartResponse.getResponsecode() != null) {

                            if (cartResponse.getResponsecode().equalsIgnoreCase("200")) {
                                List<com.zikrabyte.organic.api_responses.viewcart.Response> mCartItemList = cartResponse.getResponse();
                                if (mCartItemList.isEmpty()) {
                                    //showEmptyLayout();
                                    setCartValue(0);
                                } else {
                                    setCartValue(mCartItemList.size());
//                                mcartValue.setText(mCartItemList.size()+"");
                                    //setAdapter(mCartItemList);
                                }
                            } else {
                                //setCartValue(0);
                                if (cartResponse.getResponsecode().equalsIgnoreCase("501")) {

                                    if (cartResponse.getResponseMessage() != null) {
                                        //Toast.makeText(this, cartResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();

                                        showToast(cartResponse.getResponseMessage());
                                    }

                                    if (cartResponse.getViewCartMessage() != null) {
                                        // Toast.makeText(this, cartResponse.getViewCartMessage(), Toast.LENGTH_SHORT).show();

                                        showToast(cartResponse.getViewCartMessage());
                                    }
                                }

                                if (cartResponse.getResponseMessage() != null) {
                                    //Toast.makeText(this, cartResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                                    showToast(cartResponse.getResponseMessage());
                                }
                                if (cartResponse.getViewCartMessage() != null) {
                                    //Toast.makeText(this, cartResponse.getViewCartMessage(), Toast.LENGTH_SHORT).show();
                                    showToast(cartResponse.getViewCartMessage());
                                }

                                //Something went Wrong
                            }
                        }
                    } else {

                        //Null response
                    }

                } else {

                    //setCartValue(0);

                    if (cartResponse != null) {
                        if (cartResponse.getViewCartMessage() != null) {
                            //Toast.makeText(this, cartResponse.getViewCartMessage(), Toast.LENGTH_SHORT).show();
                            showToast(cartResponse.getViewCartMessage());
                        }
                    }
                    //API call Failure
                    //Snackbar.make(mSearchIcon,"API call failed",Snackbar.LENGTH_LONG).show();
                }
                break;

            case viewUserProfile:
                ViewUserProfileResponse viewUserProfileResponse = (ViewUserProfileResponse) response;
                if (isSucces) {
                    if (viewUserProfileResponse != null) {
                        if (viewUserProfileResponse.getResponsecode() != null) {
                            if (viewUserProfileResponse.getResponsecode().equalsIgnoreCase("200")) {
                                List<com.zikrabyte.organic.api_responses.viewuserprofile.Response> userinfoList = viewUserProfileResponse.getResponse();
                                if (userinfoList != null) {
                                    updateUserProfile(userinfoList);
                                } else {
                                    if (viewUserProfileResponse.getProfileMessage() != null) {
                                        //Toast.makeText(this, viewUserProfileResponse.getProfileMessage(), Toast.LENGTH_SHORT).show();
                                        showToast(viewUserProfileResponse.getProfileMessage());
                                    }
                                }
                            } else {
                                if (viewUserProfileResponse.getProfileMessage() != null) {
                                    //   Toast.makeText(this, viewUserProfileResponse.getProfileMessage(), Toast.LENGTH_SHORT).show();
                                    showToast(viewUserProfileResponse.getProfileMessage());
                                }

                            }
                        }
                    } else {

                        // Snackbar.make(recyclerView,"user information is empty",Snackbar.LENGTH_LONG).show();

                    }


                } else {
                    if (viewUserProfileResponse != null) {
                        if (viewUserProfileResponse.getProfileMessage() != null) {
                            //Toast.makeText(this, viewUserProfileResponse.getProfileMessage(), Toast.LENGTH_SHORT).show();
                            showToast(viewUserProfileResponse.getProfileMessage());
                        }
                    }
                    //API call failed
                }
                break;
        }

    }

    private void updateUserProfile(List<com.zikrabyte.organic.api_responses.viewuserprofile.Response> mList) {


        userName = mList.get(0).getFname();
        userEmail = mList.get(0).getEmail();
        userPhoneNumber = mList.get(0).getPhone();
        userPicture = mList.get(0).getPicture();

        SharedPreferences preferences = getSharedPreferences("USER_DETAILS", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("USERNAME", userName);
        editor.putString("EMAIL", userEmail);
        editor.putString("PHONE_NUMBER", userPhoneNumber);
        editor.putString("USER_ID", userId);
        editor.apply();

        mProfileName.setText(userName);
        /*Glide.with(this)
                .asBitmap()
                .load(userPicture)
                .into(mUserProfilePic);*/

        if (userPicture == null || userPicture.isEmpty()) {

            Picasso.get()
                    .load(R.drawable.user)
                    .placeholder(R.drawable.background_drawable)
                    .error(R.drawable.user)
                    //.resize(150,150)
                    .into(mProfilePic);

        } else {
            Picasso.get()
                    .load(userPicture)
                    .placeholder(R.drawable.background_drawable)
                    .error(R.drawable.user)
                    //.resize(150,150)
                    .into(mProfilePic);

        }


    }

    private void initializeTabLayout(List<Response> mList) {


        //mTabLayout.addTab(mTabLayout.newTab().setText(mCategoriesList.get(0).getName()));
        //mTabLayout.addTab(mTabLayout.newTab().setText(mCategoriesList.get(1).getName()));
        for (int i = 0; i < mList.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mList.get(i).getName()));
        }
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), mList.size());

        viewPager.setAdapter(myPagerAdapter);
        //viewPager.setOffscreenPageLimit(3);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mList.clear();


    }

    public static void setCartValue(int size) {

        mcartValue.setText(size + "");

    }

    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
            // v.setBackground(getResources().getDrawable(R.drawable.disabled_button));
        }
    }

    private void enableViews(View... views) {
        for (View v : views) {
            v.setEnabled(true);
            // v.setBackground(getResources().getDrawable(R.drawable.disabled_button));
        }
    }

    public void showToast(String message) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

   /* @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setColorSchemeColors(this.getResources().getColor(R.color.blue),this.getResources().getColor(R.color.colorAccent),
                this.getResources().getColor(R.color.green));
        refreshContent();

    }

    public void refreshContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSharedPreferenceData();
                mSwipeRefreshLayout.setRefreshing(false);

            }
        },3000);
    }*/
}
