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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.zikrabyte.organic.adapters.SelectAddressAdapter;
import com.zikrabyte.organic.api_requests.AddAddress;
import com.zikrabyte.organic.api_requests.ViewAddress;
import com.zikrabyte.organic.api_requests.ViewUserProfile;
import com.zikrabyte.organic.api_responses.addaddress.AddAddressResponse;
import com.zikrabyte.organic.api_responses.apartmentlist.ApartmentListResponse;
import com.zikrabyte.organic.api_responses.viewAddress.Response;
import com.zikrabyte.organic.api_responses.viewAddress.ViewAddressResponse;
import com.zikrabyte.organic.api_responses.viewuserprofile.ViewUserProfileResponse;
import com.zikrabyte.organic.beanclasses.AddressBean;
import com.zikrabyte.organic.customs.CircleImageView;
import com.zikrabyte.organic.utils.EzzShoppUtils;
import com.zikrabyte.organic.utils.OnResponseListener;
import com.zikrabyte.organic.utils.WebServices;

import java.util.ArrayList;
import java.util.List;

public class SelectAddressActivity extends AppCompatActivity implements View.OnClickListener,OnResponseListener{


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

    RecyclerView mRecyclerView;
    LinearLayoutManager layoutManager;
    Button mAddNewAddress;
   /* ImageView mActionBack;*/
    LinearLayout mEmptyLayout;

    SelectAddressAdapter adapter;

    List<AddressBean> mList=new ArrayList<>();

    String[] name={"Krishnamurthy","Mahesh","Manish","shobhnath","praveen"};
    String[] city={"Bangalore","Tumkur","Mysore","Mangalore","koorg"};
    String[] state={"Karnataka","AP","Telangana","chhattisGhad","Karnatak"};


    boolean isFromNavigationDrawer=false,isFromPreviousActivity=false;
    String fromINtent=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        //setupWindowAnimations();
        initializeViews();
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

    private void setupWindowAnimations() {
        Transition transition,returnTransition;
        //transition = buildEnterTransition();
        transition = TransitionInflater.from(this).inflateTransition(R.transition.slide_from_bottom);
        returnTransition=buildReturnTransition();

       /* if (type == TYPE_PROGRAMMATICALLY) {
            transition = buildEnterTransition();
        }  else {
            transition = TransitionInflater.from(this).inflateTransition(R.transition.explode);
        }*/
        getWindow().setEnterTransition(transition);
        getWindow().setReturnTransition(returnTransition);
    }

    private void initializeViews()
    {
        mList=getList();

        //mActionBack=findViewById(R.id.vI_asa_back_icon);
        mAddNewAddress=findViewById(R.id.vB_asa_add_new_address);
        mRecyclerView=findViewById(R.id.vR_asa_recyclerview);

        mEmptyLayout=findViewById(R.id.vL_asa_empty_layout);

        mRecyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        if(receiveIntentData())
        {
            if(fromINtent!=null){

                if(fromINtent.equalsIgnoreCase("PAYMENTDETAILS")){
                    //Invisible
                    isFromPreviousActivity=true;
                    enableViews(mAddNewAddress);

                    SharedPreferences editPtreference=getSharedPreferences("EDITADDRESS_PREF",MODE_PRIVATE);
                    SharedPreferences.Editor editor=editPtreference.edit();
                    editor.putBoolean("IS_EDITABLE",true);
                    editor.apply();
                }
                if(fromINtent.equalsIgnoreCase("NAVIGATIONDRAWER")){
                    //Invisible
                    isFromNavigationDrawer=true;
                    enableViews(mAddNewAddress);

                    SharedPreferences editPtreference=getSharedPreferences("EDITADDRESS_PREF",MODE_PRIVATE);
                    SharedPreferences.Editor editor=editPtreference.edit();
                    editor.putBoolean("IS_EDITABLE",false);
                    editor.apply();
                }/*else{
                    isFromNavigationDrawer=false;
                    enableViews(mAddNewAddress);
                }*/
            }

        }

       /* if(mList.isEmpty())
        {
            mEmptyLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
        else
        {
            mEmptyLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            adapter=new SelectAddressAdapter(this,mList);
            mRecyclerView.setAdapter(adapter);

        }*/


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
        mAddNewAddress.setOnClickListener(this);

        getSharedPreferenceData();


    }

    private void getSharedPreferenceData()
    {
        SharedPreferences preferences=getSharedPreferences("LOGIN_PREFERENCE",MODE_PRIVATE);
        authKey=preferences.getString("AUTH_KEY",null);
        userId=preferences.getString("USER_ID",null);
        userName=preferences.getString("USER_NAME",null);
        userEmail=preferences.getString("EMAIL_ID",null);

        Log.d("AUTHKEY",authKey+"");

        mProfileName.setText(userName);

        callViewAddressAPI(authKey,userId);
        callViewUserProfileAPI(authKey,userId);
    }


    private void callViewUserProfileAPI(String authKey,String userId)
    {
        ViewUserProfile viewUserProfile=new ViewUserProfile(userId);
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            /*mInternetStatusText.setVisibility(View.GONE);*/
            WebServices<ViewUserProfileResponse> webServices = new WebServices<ViewUserProfileResponse>(SelectAddressActivity.this);
            webServices.viewUserProfile(WebServices.BASE_URL, WebServices.ApiType.viewUserProfile,authKey,userId);
        } else {

          /*  mInternetStatusText.setVisibility(View.VISIBLE);*/
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet)+"", Toast.LENGTH_SHORT).show();
        }

    }
    public void callViewAddressAPI(String authKey,String userId)
    {
        ViewAddress viewAddress=new ViewAddress(userId);
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            WebServices<ViewAddressResponse> webServices = new WebServices<ViewAddressResponse>(SelectAddressActivity.this);
            webServices.viewAddressList(WebServices.BASE_URL, WebServices.ApiType.viewAddressList,authKey,userId,viewAddress);
        } else {
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, R.string.err_msg_nointernet+"", Toast.LENGTH_SHORT).show();
        }

    }

   /* public void callEditAddressAPI(String authKey,String userId)
    {
        name=mName.getText().toString();
        phoneNumber=mPhoneNum.getText().toString();
        houseNumber=mHouseNum.getText().toString();
        apartmentName=mApartmentsSpinner.getSelectedItem().toString();
        city=mCity.getText().toString();
        pinCode=mPinCode.getText().toString();

        AddAddress addAddress=new AddAddress(userId,name,phoneNumber,houseNumber,apartmentName,city,pinCode);
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            WebServices<AddAddressResponse> webServices = new WebServices<AddAddressResponse>(SelectAddressActivity.this);
            webServices.addAddress(WebServices.BASE_URL, WebServices.ApiType.addAddress,authKey,userId,addAddress);
        } else {
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, R.string.err_msg_nointernet+"", Toast.LENGTH_SHORT).show();
        }

    }*/

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
       /* buildReturnTransition();
        finishAfterTransition();*/
        //finishAfterTransition();
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            /*case R.id.vI_asa_back_icon:
                onBackPressed();
                break;*/
            case R.id.vB_asa_add_new_address:
                gotoEditAddressActivity();
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
    private void showExitDialog()
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

                ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(SelectAddressActivity.this,null);

                Intent intent=new Intent(SelectAddressActivity.this,LogInActivity.class);
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


    private void gotoEditAddressActivity() {
        startActivity(EditAddressActivity.class);
    }

    private Visibility buildReturnTransition() {
        Visibility enterTransition = new Slide();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        return enterTransition;
    }

    public List<AddressBean> getList() {
        for (int i=0;i<name.length;i++){

            AddressBean object =new AddressBean(name[i],"9901333695","#46",
                    "RSMJ towers",city[i]);
            mList.add(object);

        }
        return mList;
    }

    private void startActivity(Class<?> tClass)
    {
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this,null);
        startActivity(new Intent(SelectAddressActivity.this,tClass),transitionActivityOptions.toBundle());
        //overridePendingTransition(R.anim.slide_in,R.anim.slide_in);
        //this.finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getSharedPreferenceData();
        /*SharedPreferences editPtreference=getSharedPreferences("EDITADDRESS_PREF",MODE_PRIVATE);
        SharedPreferences.Editor editor=editPtreference.edit();
        editor.putBoolean("IS_EDITABLE",true);
        editor.apply();*/
    }

    @Override
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {
        switch (URL)
        {
            case viewAddressList:
                if(isSucces)
                {
                    ViewAddressResponse viewAddressResponse= (ViewAddressResponse) response;
                    if(viewAddressResponse!=null)
                    {
                        if(viewAddressResponse.getResponsecode()!=null) {
                            if (viewAddressResponse.getResponsecode().equalsIgnoreCase("200")) {
                                List<Response> mList = viewAddressResponse.getResponse();
                                updateUI(mList);

                            } else {
                                Snackbar.make(mAddNewAddress, "No Saved Address found", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }
                    else
                    {
                        //Empty response Something went wrong
                    }

                }
                else {
                    //API call Failed
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

    private void updateUI(List<Response> mList) {

         if(mList.isEmpty())
        {
            mEmptyLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
        else
        {
            mEmptyLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            adapter=new SelectAddressAdapter(this,mList,isFromNavigationDrawer);
            mRecyclerView.setAdapter(adapter);

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
}
