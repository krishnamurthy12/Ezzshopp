package com.zikrabyte.organic.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.zikrabyte.organic.R;
import com.zikrabyte.organic.adapters.CartAdapter;
import com.zikrabyte.organic.adapters.DummyCartAdapter;
import com.zikrabyte.organic.api_requests.BulkAPI;
import com.zikrabyte.organic.api_requests.ViewCart;
import com.zikrabyte.organic.api_requests.ViewUserProfile;
import com.zikrabyte.organic.api_responses.bulkapi.BulkAPIResponse;
import com.zikrabyte.organic.api_responses.viewcart.Response;
import com.zikrabyte.organic.api_responses.viewcart.ViewCartResponse;
import com.zikrabyte.organic.api_responses.viewuserprofile.ViewUserProfileResponse;
import com.zikrabyte.organic.beanclasses.AddToCartDummy;
import com.zikrabyte.organic.beanclasses.CartBeanClass;
import com.zikrabyte.organic.customs.CircleImageView;
import com.zikrabyte.organic.utils.BaseActivity;
import com.zikrabyte.organic.utils.EzzShoppUtils;
import com.zikrabyte.organic.utils.MySharedPreference;
import com.zikrabyte.organic.utils.OnResponseListener;
import com.zikrabyte.organic.utils.WebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

public class CartActivity extends AppCompatActivity implements View.OnClickListener,OnResponseListener{

    boolean isLoggedIn=false;

    Toast mToast;

    /*Navigation drawer elements*/
    DrawerLayout drawer;
    NavigationView navigationView;
    ImageView mNavigationMenuIcon,mNavigationBack;

    LinearLayout mHome,mMyProfileLayout,mMyAddressLayout,mMyCartLayout,mMyOrdersLayout,mMyCouponsLayout,mAboutUsLayout,
            mContactUsLayout,mShareLayout,mRateUsLayout,mLogOutLayout,mLogInLayout;
    CircleImageView mProfilePic;
    TextView mProfileName;

    //Text views for disable for guest user case
    TextView mMyAddress,mLogOut,mMyProfile,mMyOrders,mMyCoupons,mReferralCode;

    String authKey,userId,userName,userEmail,userPhoneNumber,userPicture;


    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    //ImageView mBackIcon;
    TextView mSubTotal;
   // EditText mPromoCodeValue;
    Button mCheckOut;

    RelativeLayout rootview;

    RecyclerView mRecyclerview;
    LinearLayoutManager layoutManager;
    CartAdapter adapter;
    DummyCartAdapter dummyCartAdapter;

    LinearLayout mEmptyLayout,mDetailLayout;
    Button mStartShopping;
    ProgressBar mProgressBar;

    List<CartBeanClass> mList=new ArrayList<>();


    List<Response> mCartItemList=new ArrayList<>();

    String subTotal,gst,deliveryCharge,grandTotal;

    ArrayList<AddToCartDummy> savedList=new ArrayList<>();
    MySharedPreference mySharedPreference=new MySharedPreference();

    double dummySubTotal=0;

    CartActivity cartActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        if(mySharedPreference.getFavorites(this)!=null)
        {
            savedList=mySharedPreference.getFavorites(this);
        }

        cartActivity=this;
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupWindowAnimations();


        }*/
      /*  SharedPreferences preferences = getSharedPreferences("LOGIN_PREFERENCE", MODE_PRIVATE);
        isLoggedIn= preferences.getBoolean("IS_LOGGEDIN",false);*/

        initViews();


    }
    private void initViews()
    {

        initCircleRevealAnimation();

        mMyAddress=findViewById(R.id.vT_nh_my_address);
        mMyProfile=findViewById(R.id.vT_nh_my_profile);
        mLogOut=findViewById(R.id.vT_nh_log_out);
        mMyCoupons=findViewById(R.id.vT_nh_my_coupons);
        mMyOrders=findViewById(R.id.vT_nh_my_orders);
        mReferralCode=findViewById(R.id.vT_nh_referral_code);

        mProgressBar=findViewById(R.id.vP_ac_progressBar);

        mEmptyLayout=findViewById(R.id.vL_ac_empty_layout);
        mStartShopping=findViewById(R.id.vB_ac_start_shopping);
        //mList=getList();

        mDetailLayout=findViewById(R.id.details_layout);

        //mBackIcon=findViewById(R.id.vI_ac_back_icon);
        mCheckOut=findViewById(R.id.vB_ac_checkout);
        //mPromoCodeValue=findViewById(R.id.vE_ac_promocode);

        mSubTotal=findViewById(R.id.vT_ac_subtotal_amount);
        //mTotalAmount=findViewById(R.id.vT_ac_total_amount);
       //mPromoCodeText=findViewById(R.id.vT_ac_promocode_text);

       mRecyclerview=findViewById(R.id.cart_recyclerview);
       layoutManager=new LinearLayoutManager(this);
       mRecyclerview.setHasFixedSize(true);
       mRecyclerview.setLayoutManager(layoutManager);

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


       // getSharedPreferenceData();

      /* adapter=new CartAdapter(this,mList);

       if(mList.isEmpty())
       {
           mEmptyLayout.setVisibility(View.VISIBLE);
           mRecyclerview.setVisibility(View.GONE);
           mCheckOut.setVisibility(View.GONE);
           mDetailLayout.setVisibility(View.GONE);
       }
       else
       {
           mEmptyLayout.setVisibility(View.GONE);
           mDetailLayout.setVisibility(View.VISIBLE);
           mRecyclerview.setVisibility(View.VISIBLE);
           mCheckOut.setVisibility(View.VISIBLE);
           mRecyclerview.setAdapter(adapter);
       }*/


       mCheckOut.setOnClickListener(this);
       //mPromoCodeText.setOnClickListener(this);
       //mBackIcon.setOnClickListener(this);
       mStartShopping.setOnClickListener(this);
        getSharedPreferenceData();

       /* if(isLoggedIn)
        {
            //initViews();
            getSharedPreferenceData();
        }
        else {
            //startActivity(LogInActivity.class);
            showDuummmyCart();
        }*/

    }
    public  void showDuummmyCart() {
        if (savedList != null) {
            if (savedList.isEmpty()) {
                showEmptyLayout();
            } else {

                //ArrayList<AddToCartDummy> savedList = mySharedPreference.getFavorites(this);
               /* for (int i = 0; i < savedList.size(); i++) {
                    Log.d("localdata", "savedList product id =>" + savedList.get(i).getPriductId() + "");
                    Log.d("localdata", "savedList product name =>" + savedList.get(i).getProductName() + "");
                    Log.d("localdata", "savedList quantyty =>" + savedList.get(i).getPriductQuantity() + "");

                }*/

               /* for (int i = 0; i < savedList.size(); i++) {
                    dummySubTotal += Double.parseDouble(savedList.get(i).getProductSubTotal());
                }
                //subTotal = String.valueOf(savedList.get(0).getProductSubTotal());

                mSubTotal.setText(dummySubTotal + "");*/
                if (savedList != null)
                    HomeActivity.setCartValue(savedList.size());
                // gst = String.valueOf(cartResponse.getGst());
                grandTotal = String.valueOf(savedList.get(0).getProductSubTotal());
                //deliveryCharge = cartResponse.getDeliveryCharges();

                /*setAdapter(mCartItemList);*/

                dummyCartAdapter = new DummyCartAdapter(this, savedList, mSubTotal,cartActivity);

                mEmptyLayout.setVisibility(View.GONE);
                mDetailLayout.setVisibility(View.VISIBLE);
                mRecyclerview.setVisibility(View.VISIBLE);
                mCheckOut.setVisibility(View.VISIBLE);
                mRecyclerview.setAdapter(dummyCartAdapter);
                //mSubTotal.setText(String.valueOf(cartResponse.getSubTotal()));

                //mTotalAmount.setText(String.valueOf(cartResponse.getGrandTotal()));
            }
        }
    }

    private void initCircleRevealAnimation() {

        rootview=findViewById(R.id.vR_ac_root_layout);
        rootview.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        //rootview.setBackground(getResources().getDrawable(R.drawable.cart_bg));
        rootview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);
                  //LEFT TOP TO BOTTOM RIGHT ANIMATION
               /* int cx1 = 0;
                int cy1 = 0;*/
                int cx1 = rootview.getRight();
                int cy1 = rootview.getLeft();
                // get the hypothenuse so the radius is from one corner to the other
                int radius1 = (int) Math.hypot(right, bottom);
                Animator reveal1 = ViewAnimationUtils.createCircularReveal(v, cx1, cy1, 0, radius1);
                reveal1.setInterpolator(new DecelerateInterpolator(2f));
                reveal1.setDuration(1200);
                reveal1.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                         rootview.setBackground(getResources().getDrawable(R.drawable.signup_bg));

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                reveal1.start();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void endCircularRevealAnimation()
    {
        final View myView = findViewById(R.id.vR_ac_root_layout);

        // get the center for the clipping circle
      /*  int cx = myView.getWidth() / 2;
        int cy = myView.getHeight() / 2;*/

        int cx = myView.getRight();
        int cy = myView.getLeft();

        // get the initial radius for the clipping circle
        float initialRadius = (float) Math.hypot(myView.getWidth(), myView.getWidth());

        // create the animation (the final radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);
        anim.setDuration(500);
        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                myView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                myView.setVisibility(View.INVISIBLE);
            }
        });

        // start the animation
        anim.start();
    }

   /* private void getSharedPreferenceData()
    {
        SharedPreferences preferences=getSharedPreferences("LOGIN_PREFERENCE",MODE_PRIVATE);
        authKey=preferences.getString("AUTH_KEY",null);
        userId=preferences.getString("USER_ID",null);

        callViewCartAPI(authKey,userId);
    }*/

    private void getSharedPreferenceData()
    {
        SharedPreferences preferences=getSharedPreferences("LOGIN_PREFERENCE",MODE_PRIVATE);
        authKey=preferences.getString("AUTH_KEY",null);
        userId=preferences.getString("USER_ID",null);
        userName=preferences.getString("USER_NAME",null);
        userEmail=preferences.getString("EMAIL_ID",null);
        isLoggedIn= preferences.getBoolean("IS_LOGGEDIN",false);


        mProfileName.setText(userName);

        if(isLoggedIn)
        {
            //initViews();
            if (savedList != null) {
                if (!savedList.isEmpty()) {
                    callBulkCartAPI(authKey, userId, savedList);

                } else {
                    callViewCartAPI(authKey, userId);

                }

            } else {
                ///callViewCartAPI(authKey,userId);
            }

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

            callViewUserProfileAPI(authKey,userId);
        }
        else {
            //startActivity(LogInActivity.class);

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

            showDuummmyCart();

        }

       // callGetAllCategoriesAPI(authKey,userId);

    }

    private void callBulkCartAPI(String authKey,String userId,ArrayList<AddToCartDummy> savedList) {
        if(!savedList.isEmpty())
        {
            JsonObject mainjsonObject=new JsonObject();
            JsonArray jsonArray=new JsonArray();
            JSONArray mainArray=new JSONArray();


            for(int i=0;i<savedList.size();i++)
            {
                JsonObject innerObject=new JsonObject();

                innerObject.addProperty("product_id",savedList.get(i).getPriductId());
                innerObject.addProperty("quantity",savedList.get(i).getPriductQuantity());
                jsonArray.add(innerObject);

            }
           // jsonArray.put(jsonObject);
            Log.d("array",jsonArray+"");
            mainjsonObject.addProperty("user_id",userId);
            mainjsonObject.add("products",jsonArray);
            // mainArray.put(mainjsonObject);

            //BulkAPI bulkAPI=new BulkAPI(userId,jsonArray);

           // RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject((Map) mainjsonObject)).toString());

            Log.d("parameters","auth key=> "+authKey);
            Log.d("parameters","userid=> "+userId);
            Log.d("parameters","main json object=>"+mainjsonObject);
            Log.d("parameters","json array=> "+jsonArray);
         //   Log.d("parameters","BulkAPI bean class=> "+bulkAPI.toString());
         //   Log.d("parameters","converted json orray=>"+ Arrays.toString(toStringArray(jsonArray)));

            Log.d("parameters","BulkAPI bean class=> "+mainjsonObject.toString());

            if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

                mProgressBar.setVisibility(View.VISIBLE);

                WebServices<BulkAPIResponse> webServices = new WebServices<BulkAPIResponse>(CartActivity.this);
                webServices.bulkCartAPI(WebServices.BASE_URL, WebServices.ApiType.bulkCart,authKey,userId,mainjsonObject);
            } else {
                //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
               // Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet)+"", Toast.LENGTH_SHORT).show();
                showToast(getResources().getString(R.string.err_msg_nointernet));
            }
        }
    }

    public static String[] toStringArray(JSONArray array) {
        if(array==null)
            return null;

        String[] arr=new String[array.length()];
        for(int i=0; i<arr.length; i++) {
            arr[i]=array.optString(i);
        }
        return arr;
    }

    private void callViewUserProfileAPI(String authKey,String userId)
    {
        ViewUserProfile viewUserProfile=new ViewUserProfile(userId);
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            /*mInternetStatusText.setVisibility(View.GONE);*/
            WebServices<ViewUserProfileResponse> webServices = new WebServices<ViewUserProfileResponse>(CartActivity.this);
            webServices.viewUserProfile(WebServices.BASE_URL, WebServices.ApiType.viewUserProfile,authKey,userId);
        } else {

          /*  mInternetStatusText.setVisibility(View.VISIBLE);*/
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
           // Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet)+"", Toast.LENGTH_SHORT).show();
            showToast(getResources().getString(R.string.err_msg_nointernet));
        }

    }
    public void callViewCartAPI(String authKey,String userId)
    {

        Log.d("flowcheck","Inside view cart API call");
        ViewCart viewCart=new ViewCart(userId,"");
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {
            mProgressBar.setVisibility(View.VISIBLE);

            WebServices<ViewCartResponse> webServices = new WebServices<ViewCartResponse>(CartActivity.this);
            webServices.viewCart(WebServices.BASE_URL, WebServices.ApiType.viewCart,authKey,userId,viewCart);
        } else {
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
          //  Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet)+"", Toast.LENGTH_SHORT).show();
            showToast(getResources().getString(R.string.err_msg_nointernet));
        }

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
    private Transition buildEnterTransition() {
        Explode enterTransition = new Explode();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        return enterTransition;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            /*case R.id.vI_ac_back_icon:
                onBackPressed();
                break;*/

            case R.id.vB_ac_checkout:
                if(mSubTotal.getText().toString().equalsIgnoreCase("0"))
                {
                    Snackbar.make(mCheckOut,"Cart is empty ",Snackbar.LENGTH_LONG).show();
                    //gotoHomeActivity();
                }
                else {
                    if(isLoggedIn)
                    {
                        gotoCheckOutActivity();
                    }
                    else
                    {
                        Intent loginIntent=new Intent(this,LogInActivity.class);
                        loginIntent.putExtra("FROMINTENT","CARTACTIVITY");
                        startActivity(loginIntent);
                    }

                }

                break;

            /*case R.id.vT_ac_promocode_text:
                handlePromoCode();
                break;*/

            case R.id.vB_ac_start_shopping:
                gotoHomeActivity();
                break;

            case R.id.vL_nh_home_layout:
                closeNavigationDrawer();
                startActivity(HomeActivity.class);
                break;

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

            case R.id.vL_nh_login_layout:
                startActivity(LogInActivity.class);
                break;

            default:closeNavigationDrawer();
        }

    }

    private void gotoHomeActivity()
    {
        startActivity(HomeActivity.class);
    }
    private void gotoCheckOutActivity() {

        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this,null);
        Intent checkoutIntent=new Intent(this,PaymentDetailsActivity.class);
        checkoutIntent.putExtra("SUB_TOTAL",subTotal);
        checkoutIntent.putExtra("GST",gst);
        checkoutIntent.putExtra("DELIVERY_CHARGE",deliveryCharge);
        checkoutIntent.putExtra("GRAND_TOTAL",grandTotal);
        startActivity(checkoutIntent,transitionActivityOptions.toBundle());

        /*startActivity(SelectAddressActivity.class);*/
        //startActivity(PaymentDetailsActivity.class);
    }

    /*private void handlePromoCode() {
        mPromoCodeText.setVisibility(View.GONE);
        mPromoCodeValue.setVisibility(View.VISIBLE);
    }*/

  /*  public List<CartBeanClass> getList() {
        for(int i =0;i<=5;i++)
        {
            mList.add(new CartBeanClass(R.drawable.delivery,"Carrot",5,20));
        }
        return mList;
    }
*/
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
            endCircularRevealAnimation();
            //startActivity(HomeActivity.class);
            super.onBackPressed();
        }
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
        startActivity(new Intent(CartActivity.this,tClass),transitionActivityOptions.toBundle());
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

                ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(CartActivity.this,null);

                Intent intent=new Intent(CartActivity.this,LogInActivity.class);
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
        /*callViewCartAPI(authKey,userId);
        callViewUserProfileAPI(authKey,userId);*/

        initViews();
       // getSharedPreferenceData();
    }

    @Override
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {

        switch (URL)
        {
            case viewCart:

                Log.d("flowcheck","Inside view cart response");
                if(mProgressBar!=null)
                {
                    if(mProgressBar.isShown())
                    {
                        mProgressBar.setVisibility(View.GONE);
                    }
                }
                if(isSucces) {
                    ViewCartResponse cartResponse = (ViewCartResponse) response;
                    if (cartResponse != null) {
                        if(cartResponse.getResponsecode()!=null) {
                            if (cartResponse.getResponsecode().equalsIgnoreCase("200")) {
                                mCartItemList = cartResponse.getResponse();
                                if (mCartItemList.isEmpty()) {
                                    showEmptyLayout();
                                } else {

                                    subTotal = String.valueOf(cartResponse.getSubTotal());
                                    gst = String.valueOf(cartResponse.getGst());
                                    grandTotal = String.valueOf(cartResponse.getGrandTotal());
                                    deliveryCharge = cartResponse.getDeliveryCharges();

                                    setAdapter(mCartItemList);
                                    mSubTotal.setText(String.valueOf(cartResponse.getSubTotal()));

                                    //mTotalAmount.setText(String.valueOf(cartResponse.getGrandTotal()));
                                }
                                //Something went Wrong
                            }
                            else {
                                if(cartResponse.getResponseMessage()!=null)
                                {
                                    Snackbar.make(mCheckOut,cartResponse.getResponseMessage(),Snackbar.LENGTH_LONG).show();
                                }
                                if(cartResponse.getViewCartMessage()!=null)
                                {
                                    Snackbar.make(mCheckOut,cartResponse.getViewCartMessage(),Snackbar.LENGTH_LONG).show();
                                }
                            }
                        }
                    }

                } else {
                    //API call Failure
                    //Snackbar.make(mStartShopping, "API call failed", Snackbar.LENGTH_LONG).show();
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
                                       // Toast.makeText(this, viewUserProfileResponse.getProfileMessage(), Toast.LENGTH_SHORT).show();
                                        showToast(viewUserProfileResponse.getProfileMessage());
                                    }
                                }
                            } else {
                                if(viewUserProfileResponse.getProfileMessage()!=null)
                                {
                                   // Toast.makeText(this, viewUserProfileResponse.getProfileMessage(), Toast.LENGTH_SHORT).show();
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

            case bulkCart:
                if(mProgressBar!=null)
                {
                    if(mProgressBar.isShown())
                    {
                        mProgressBar.setVisibility(View.GONE);
                    }
                }
                BulkAPIResponse bulkAPIResponse= (BulkAPIResponse) response;
                if(isSucces)
                {
                    if(bulkAPIResponse!=null)
                    {
                        if(bulkAPIResponse.getResponsecode()!=null)
                        {
                            if(bulkAPIResponse.getResponsecode().equalsIgnoreCase("200"))
                            {
                                showCartAndClearPreferences();
                                if(bulkAPIResponse.getAddCartMessage()!=null)
                                {
                                    Snackbar.make(mCheckOut,bulkAPIResponse.getAddCartMessage(),Snackbar.LENGTH_LONG).show();
                                }

                            }else
                            {
                                if(bulkAPIResponse.getAddCartMessage()!=null)
                                {
                                    Snackbar.make(mCheckOut,bulkAPIResponse.getAddCartMessage(),Snackbar.LENGTH_LONG).show();
                                }

                            }
                        }

                    }
                }
                else
                {
                    //API call failed
                    if(bulkAPIResponse!=null)
                    {
                        if(bulkAPIResponse.getAddCartMessage()!=null)
                        {
                            Snackbar.make(mCheckOut,bulkAPIResponse.getAddCartMessage(),Snackbar.LENGTH_LONG).show();
                        }
                    }

                }

        }

    }

    private void showCartAndClearPreferences() {
        callViewCartAPI(authKey,userId);

        if(savedList!=null);
        {
            savedList.clear();
        }
        mySharedPreference.clearPreferences(this);

    }

    private void setAdapter(List<Response> mCartItemList) {

        adapter=new CartAdapter(this,mCartItemList,mSubTotal);

        mEmptyLayout.setVisibility(View.GONE);
        mDetailLayout.setVisibility(View.VISIBLE);
        mRecyclerview.setVisibility(View.VISIBLE);
        mCheckOut.setVisibility(View.VISIBLE);
        mRecyclerview.setAdapter(adapter);
    }

    private void showEmptyLayout() {

        mEmptyLayout.setVisibility(View.VISIBLE);
        mRecyclerview.setVisibility(View.GONE);
        mCheckOut.setVisibility(View.GONE);
        mDetailLayout.setVisibility(View.GONE);
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
    public void showToast (String message){
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

}
