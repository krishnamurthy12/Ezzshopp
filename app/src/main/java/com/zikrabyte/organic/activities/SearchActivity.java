package com.zikrabyte.organic.activities;

import android.content.Context;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.squareup.picasso.Picasso;
import com.zikrabyte.organic.R;
import com.zikrabyte.organic.api_requests.ViewUserProfile;
import com.zikrabyte.organic.api_responses.searchresults.Response;
import com.zikrabyte.organic.api_responses.searchresults.SearchProductsResponse;
import com.zikrabyte.organic.adapters.SearchAdapter;
import com.zikrabyte.organic.api_requests.Search;
import com.zikrabyte.organic.api_responses.viewuserprofile.ViewUserProfileResponse;
import com.zikrabyte.organic.beanclasses.ItemDescription;
import com.zikrabyte.organic.customs.CircleImageView;
import com.zikrabyte.organic.utils.EzzShoppUtils;
import com.zikrabyte.organic.utils.OnResponseListener;
import com.zikrabyte.organic.utils.WebServices;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener,OnResponseListener {
    List<ItemDescription> mList=new ArrayList<>();

    DrawerLayout drawer;
    NavigationView navigationView;
    ImageView mNavigationMenuIcon,mNavigationBack;

    LinearLayout mHome,mMyProfileLayout,mMyAddressLayout,mMyCartLayout,mMyOrdersLayout,mMyCouponsLayout,
            mAboutUsLayout,mContactUsLayout,mShareLayout,mRateUsLayout,mLogOutLayout,mLogInLayout;
    CircleImageView mProfilePic;
    TextView mProfileName;

    //Text views for disable for guest user case
    TextView mMyAddress,mLogOut,mMyProfile,mMyOrders,mMyCoupons,mReferralCode;

    String authKey,userId,userName,userEmail,userPhoneNumber,userPicture;


    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    ImageView mActionBack,mSearch,mCancel;/*mCartIcon*/
    LinearLayout searchInitial,mainLayout;
    EditText mSearchText;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    SearchAdapter searchAdapter;
    LinearLayout mNoMatchLayout;
    ProgressBar mProgressBar;

    boolean isLoggedIn=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initializeViews();
    }

    private void initializeViews()
    {
        mMyAddress=findViewById(R.id.vT_nh_my_address);
        mMyProfile=findViewById(R.id.vT_nh_my_profile);
        mLogOut=findViewById(R.id.vT_nh_log_out);
        mMyCoupons=findViewById(R.id.vT_nh_my_coupons);
        mMyOrders=findViewById(R.id.vT_nh_my_orders);
        mReferralCode=findViewById(R.id.vT_nh_referral_code);

        mList=getList();



        mProgressBar=findViewById(R.id.vP_as_progressbar);
        recyclerView=findViewById(R.id.vR_as_recycleview);
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
        mLogInLayout=findViewById(R.id.vL_nh_login_layout);

        mProfilePic=findViewById(R.id.vIV_nh_profile_pic);
        mProfileName=findViewById(R.id.vT_nh_profile_name);


        mNavigationMenuIcon=findViewById(R.id.homepage_navigation_menu_icon);
        mNavigationBack=findViewById(R.id.vI_nh_back);

        mNavigationMenuIcon.setOnClickListener(this);
        mNavigationBack.setOnClickListener(this);


        mHome.setOnClickListener(this);
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



        //mActionBack=findViewById(R.id.vI_as_back);
        mCancel=findViewById(R.id.vI_as_cancel);
        mSearch=findViewById(R.id.vI_as_search);
        //mCartIcon=findViewById(R.id.vI_as_carticon);
        mSearchText=findViewById(R.id.vE_as_searchtext);
        searchInitial=findViewById(R.id.initial_search);
        mainLayout=findViewById(R.id.mainlayout_search);
        mNoMatchLayout=findViewById(R.id.vL_ac_empty_layout);

        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(count==0)
                {
                    mCancel.setVisibility(View.GONE);
                    searchInitial.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    mNoMatchLayout.setVisibility(View.GONE);

                }
                else
                {
                    searchInitial.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    mCancel.setVisibility(View.VISIBLE);
                    mNoMatchLayout.setVisibility(View.GONE);

                }


                //searchAdapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

                callSearchAPI(s.toString());

            }
        });

       // mActionBack.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        //mCartIcon.setOnClickListener(this);
        mCancel.setOnClickListener(this);


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

        if(isLoggedIn)
        {
            callViewUserProfileAPI(authKey,userId);

            mLogOutLayout.setVisibility(View.VISIBLE);
            mLogInLayout.setVisibility(View.GONE);

            mMyProfileLayout.setOnClickListener(this);
            mMyOrdersLayout.setOnClickListener(this);
            mMyAddressLayout.setOnClickListener(this);
            mMyCouponsLayout.setOnClickListener(this);
            mShareLayout.setOnClickListener(this);
            mLogOutLayout.setOnClickListener(this);
        }
        else {

            mProfileName.setVisibility(View.GONE);
            Picasso.get()
                    .load(R.drawable.user)
                    .placeholder(R.drawable.background_drawable)
                    .error(R.drawable.user)
                    .resize(150,150)
                    .into(mProfilePic);

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


    }

    private void callViewUserProfileAPI(String authKey,String userId)
    {
        ViewUserProfile viewUserProfile=new ViewUserProfile(userId);
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            /*mInternetStatusText.setVisibility(View.GONE);*/
            WebServices<ViewUserProfileResponse> webServices = new WebServices<ViewUserProfileResponse>(SearchActivity.this);
            webServices.viewUserProfile(WebServices.BASE_URL, WebServices.ApiType.viewUserProfile,authKey,userId);
        } else {

          /*  mInternetStatusText.setVisibility(View.VISIBLE);*/
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet)+"", Toast.LENGTH_SHORT).show();
        }

    }


    private void callSearchAPI(String text)
    {
        Search search=new Search(userId,text);
        //Search viewUserProfile=new Search(userId,enteredText);
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            mProgressBar.setVisibility(View.VISIBLE);

            WebServices<SearchProductsResponse> webServices = new WebServices<SearchProductsResponse>(SearchActivity.this);
            webServices.searchProduct(WebServices.BASE_URL, WebServices.ApiType.search, authKey, userId,search);
        } else {
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet)+"", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            /*case R.id.vI_as_back:
                goBack();
                break;*/

            case R.id.vI_as_search:
                gotoSearchActivity();
                break;


          /*  case R.id.vI_as_carticon:
                gotoCartActivity();
                break;*/


            case R.id.vI_as_cancel:
                mSearchText.setText("");
                mCancel.setVisibility(View.GONE);
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

            case  R.id.vL_nh_login_layout:
                startActivity(LogInActivity.class);
                break;

            default:closeNavigationDrawer();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void goBack() {
        try {
            //InputMethodManager is used to hide the virtual keyboard from the user after finishing the user input
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isAcceptingText()) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        } catch (NullPointerException e) {
            Log.e("Exception", e.getMessage() + ">>");
        }
        onBackPressed();
    }

    private void gotoCartActivity()
    {
        Intent cartIntent=new Intent(SearchActivity.this, CartActivity.class);
        startActivity(cartIntent);
//        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

    }
    private void gotoSearchActivity()
    {
        callSearchAPI(mSearchText.getText().toString());
        /*Intent searchIntent=new Intent(SearchActivity.this, SearchActivity.class);
        startActivity(searchIntent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);*/
        //searchAdapter.getFilter().filter(mSearchText.getText());
    }

    private List<ItemDescription> getList() {
        for(int i=0;i<20;i++)
        {

            ItemDescription object =new ItemDescription(R.drawable.delivery, "Carrot","each","20","4",
                    "24",String.valueOf(R.string.large_text));
            mList.add(object);
        }
        return mList;
    }

    @Override
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {
        switch (URL)
        {
            case search:
                if(mProgressBar.isShown())
                {
                    mProgressBar.setVisibility(View.GONE);
                }
                if (isSucces)
                {
                    SearchProductsResponse searchResponse= (SearchProductsResponse) response;
                    if(searchResponse!=null)
                    {
                        if(searchResponse.getResponsecode()!=null)
                        {
                            if(searchResponse.getResponsecode().equalsIgnoreCase("200"))
                            {
                                List<Response> mList=searchResponse.getResponse();

                                callSetAdapter(mList);

                            }
                            else {
                                searchInitial.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.GONE);
                                mNoMatchLayout.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                    else {
                        //Null response

                        searchInitial.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        mNoMatchLayout.setVisibility(View.VISIBLE);
                    }


                }
                else {
                    //API call failed
                    searchInitial.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    mNoMatchLayout.setVisibility(View.VISIBLE);
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

    private void callSetAdapter(List<Response> mList) {

        searchAdapter=new SearchAdapter(this,mList,mainLayout);
        recyclerView.setAdapter(searchAdapter);
    }

    private void updateUserProfile(List<com.zikrabyte.organic.api_responses.viewuserprofile.Response> mList) {

        userName=mList.get(0).getFname();
        userEmail=mList.get(0).getEmail();
        userPhoneNumber=mList.get(0).getPhone();
        userPicture=mList.get(0).getPicture();

        /*SharedPreferences preferences=getSharedPreferences("USER_DETAILS",MODE_PRIVATE);
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
        startActivity(new Intent(SearchActivity.this,tClass),transitionActivityOptions.toBundle());
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

                ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(SearchActivity.this,null);

                Intent intent=new Intent(SearchActivity.this,LogInActivity.class);
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

    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
            // v.setBackground(getResources().getDrawable(R.drawable.disabled_button));
        }
    }
}
