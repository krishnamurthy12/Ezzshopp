package com.zikrabyte.organic.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.transition.Transition;
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

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.zikrabyte.organic.R;
import com.zikrabyte.organic.api_requests.AddToCart;
import com.zikrabyte.organic.api_requests.ItemDescription;
import com.zikrabyte.organic.api_requests.RemoveFromCart;
import com.zikrabyte.organic.api_requests.ViewCart;
import com.zikrabyte.organic.api_requests.ViewUserProfile;
import com.zikrabyte.organic.api_responses.addtocart.AddToCartResponse;
import com.zikrabyte.organic.api_responses.itemdescription.ItemDescriptionResponse;
import com.zikrabyte.organic.api_responses.itemdescription.Response;
import com.zikrabyte.organic.api_responses.removefromcart.RemoveFromCartResponse;
import com.zikrabyte.organic.api_responses.viewcart.ViewCartResponse;
import com.zikrabyte.organic.api_responses.viewuserprofile.ViewUserProfileResponse;
import com.zikrabyte.organic.beanclasses.AddToCartDummy;
import com.zikrabyte.organic.customs.CircleImageView;
import com.zikrabyte.organic.database.TinyDB;
import com.zikrabyte.organic.utils.BaseActivity;
import com.zikrabyte.organic.utils.EzzShoppUtils;
import com.zikrabyte.organic.utils.MySharedPreference;
import com.zikrabyte.organic.utils.OnResponseListener;
import com.zikrabyte.organic.utils.WebServices;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ItemDescriptionActivity extends AppCompatActivity implements View.OnClickListener,OnResponseListener {
    private static final int DELAY = 100;

    TextView mItemName, mItemDescription, mItemRating, mItemCost, mItemDiscount, mNumberOfItems, mTotalCost, mQuantityType;
    //Button mAddToCart;
    ImageView mItemImage, mPlusSign, mMinusSign, mNavigationMenuIcon, mNavigationBack;

    //Text views for disable for guest user case
    TextView mMyAddress, mLogOut, mMyProfile, mMyOrders, mMyCoupons, mReferralCode;

    LinearLayout mHome, mMyProfileLayout, mMyAddressLayout, mMyCartLayout, mMyOrdersLayout, mMyCouponsLayout,
            mAboutUsLayout, mContactUsLayout, mShareLayout, mRateUsLayout, mLogOutLayout,mLogInLayout;

    CircleImageView mProfilePic;
    TextView mProfileName;

    FrameLayout mCartIcon;
    TextView mCartValue;
    ImageView mSearchIcon, mLogo;
    LinearLayout bgViewGroup;
    ProgressBar mProgressBar;

    String itemName, itemDescription, itemPrice, itemDiscount, itemImageURl, itemType;
    Double totalCost;
    int itemMaxQuantity;
    int quantity;
    float itemRating;

    DrawerLayout drawer;
    NavigationView navigationView;

    String authKey, userId, productId, categoryId;
    String userName, userEmail, userPhoneNumber, userPicture;

    int cartItemCount;

    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    int maxQuantity;

    MySharedPreference mySharedPreference = new MySharedPreference();
    public static ArrayList<AddToCartDummy> addToCartList = new ArrayList<>();

    List<String > data=new ArrayList<>();
    List<String > producttype=new ArrayList<>();

    ArrayList<AddToCartDummy> savedList=new ArrayList<>();

    boolean isLoggedIn = false;
    Toast mToast;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_description);

       /* LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.item_description_toolbar, null, false);
        mFrameLayout.addView(contentView);
*/
        savedList=mySharedPreference.getFavorites(this);
        setupWindowAnimations();
        initializeViews();

        //getBundleValues();
    }



    private void initializeViews() {

        mSearchIcon = findViewById(R.id.idt_search_icon);
        mLogo = findViewById(R.id.idt_logo);
        mCartIcon = findViewById(R.id.idt_cart_icon_layout);
        mCartValue = findViewById(R.id.vT_idt_cart_value);

        mMyAddress = findViewById(R.id.vT_nh_my_address);
        mMyProfile = findViewById(R.id.vT_nh_my_profile);
        mLogOut = findViewById(R.id.vT_nh_log_out);
        mMyCoupons = findViewById(R.id.vT_nh_my_coupons);
        mMyOrders = findViewById(R.id.vT_nh_my_orders);
        mReferralCode = findViewById(R.id.vT_nh_referral_code);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        bgViewGroup = findViewById(R.id.reveal_root_layout);

        mProgressBar = findViewById(R.id.vP_cid_progressbar);

        mHome = findViewById(R.id.vL_nh_home_layout);
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

        mNavigationMenuIcon = findViewById(R.id.idt_navigation_menu_icon);
        mNavigationBack = findViewById(R.id.vI_nh_back);

        mItemName = findViewById(R.id.vT_cid_item_name);
        mItemDescription = findViewById(R.id.vT_cid_item_description);
        mItemCost = findViewById(R.id.vT_cid_item_cost);
        mItemRating = findViewById(R.id.vT_cid_item_rating);
        mItemDiscount = findViewById(R.id.vT_cid_item_discount);

        mNumberOfItems = findViewById(R.id.vT_cid_number_of_items);
        mTotalCost = findViewById(R.id.vT_cid_total_cost);

        mPlusSign = findViewById(R.id.vI_cid_plus_sign);
        mMinusSign = findViewById(R.id.vI_cid_minus_sign);
        mItemImage = findViewById(R.id.vI_cid_item_image);
        mQuantityType = findViewById(R.id.vT_cid_quantity_type);

       /* mAddToCart=findViewById(R.id.vB_cid_addto_cart);

        mAddToCart.setOnClickListener(this);*/
        mPlusSign.setOnClickListener(this);
        mMinusSign.setOnClickListener(this);
        mItemImage.setOnClickListener(this);

        mSearchIcon.setOnClickListener(this);
        mCartIcon.setOnClickListener(this);

        mHome.setOnClickListener(this);
        mNavigationMenuIcon.setOnClickListener(this);
        mNavigationBack.setOnClickListener(this);


        mNavigationMenuIcon.setOnClickListener(this);
        mNavigationBack.setOnClickListener(this);

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


        if (mySharedPreference.getFavorites(this) != null) {
            addToCartList = mySharedPreference.getFavorites(this);
        }

        if (getBundleValues()) {
            //setValuesToViews();
            getSharedPreferenceData();
        } else {
            //Toast.makeText(this, "Did not teceived any data", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean getBundleValues() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            productId = bundle.getString("productId", null);
            categoryId = bundle.getString("categoryId", null);
            maxQuantity = bundle.getInt("maxQuantity", 0);
            quantity = bundle.getInt("quantity", 0);

            itemName = bundle.getString("title", null);
            itemDescription = bundle.getString("description", null);
            itemPrice = bundle.getString("price", null);
            itemDiscount = bundle.getString("discount", null);
            itemRating = bundle.getFloat("rating", 0);
            itemImageURl = bundle.getString("image", null);


            return true;
        }
        return false;

    }

    private void setValuesToViews() {

//        mItemImage.setImageResource(itemImageURl);

        /*Glide.with(this)
                .asBitmap()
                .load(itemImageURl)
                .into( mItemImage);
        mItemName.setText(itemName);
        mItemDescription.setText(itemDescription);
        mItemCost.setText(itemPrice);
        mItemDiscount.setText(itemDiscount);
        mItemRating.setText(String.valueOf(itemRating));*/

        // mNumberOfItems.setText(quantity+"");

    }

    private void getSharedPreferenceData() {
        SharedPreferences preferences = getSharedPreferences("LOGIN_PREFERENCE", MODE_PRIVATE);
        authKey = preferences.getString("AUTH_KEY", null);
        userId = preferences.getString("USER_ID", null);
        isLoggedIn = preferences.getBoolean("IS_LOGGEDIN", false);

        if (!isLoggedIn) {
            setValuesToViews();

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

            if (HomeActivity.addToCartList != null) {
                if (HomeActivity.addToCartList.isEmpty()) {
                    mCartValue.setText("0");
                    // HomeActivity.setCartValue(0);
                } else {
                    //mCartValue.setText(HomeActivity.addToCartList.size());
                    setCartValue(HomeActivity.addToCartList.size());
                    // HomeActivity.setCartValue(addToCartList.size());
                }

            }

        } else {

            mLogOutLayout.setVisibility(View.VISIBLE);
            mLogInLayout.setVisibility(View.GONE);

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
            callViewUserProfileAPI(authKey, userId);

        }

        callItemDescriptionAPI(authKey, userId);

    }

    private void callViewUserProfileAPI(String authKey, String userId) {
        ViewUserProfile viewUserProfile = new ViewUserProfile(userId);
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {
            mProgressBar.setVisibility(View.VISIBLE);
            WebServices<ViewUserProfileResponse> webServices = new WebServices<ViewUserProfileResponse>(ItemDescriptionActivity.this);
            webServices.viewUserProfile(WebServices.BASE_URL, WebServices.ApiType.viewUserProfile, authKey, userId);
        } else {
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            //Toast.makeText(this, R.string.err_msg_nointernet+"", Toast.LENGTH_SHORT).show();
            showToast(this.getResources().getString(R.string.err_msg_nointernet));
        }

    }

    private void callItemDescriptionAPI(String authKey, String userId) {
        ItemDescription itemDescription = new ItemDescription(categoryId, userId, productId);
        if (EzzShoppUtils.isConnectedToInternet(this)) {
            WebServices<ItemDescriptionResponse> webServices = new WebServices<ItemDescriptionResponse>(this);
            webServices.getItemDescription(WebServices.BASE_URL, WebServices.ApiType.itemDescription, authKey, userId, itemDescription);
        } else {
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
           /* Toast.makeText(this, R.string.err_msg_nointernet+"", Toast.LENGTH_SHORT).show();*/
            showToast(this.getResources().getString(R.string.err_msg_nointernet));
        }
    }

    private void callViewCartAPI(String authKey, String userId) {
        ViewCart viewCart = new ViewCart(userId, "");
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            WebServices<ViewCartResponse> webServices = new WebServices<ViewCartResponse>(ItemDescriptionActivity.this);
            webServices.viewCart(WebServices.BASE_URL, WebServices.ApiType.viewCart, authKey, userId, viewCart);
        } else {
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            /*Toast.makeText(this, R.string.err_msg_nointernet+"", Toast.LENGTH_SHORT).show();*/
            showToast(this.getResources().getString(R.string.err_msg_nointernet));
        }

    }

    private void callAddToCartAPI(int numberOfItems) {

        AddToCart addToCart = new AddToCart(productId, userId, numberOfItems + "");
        if (EzzShoppUtils.isConnectedToInternet(this)) {
            WebServices<AddToCartResponse> webServices = new WebServices<AddToCartResponse>(this);
            webServices.addToCart(WebServices.BASE_URL, WebServices.ApiType.addToCart, authKey, userId, addToCart);
        } else {
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
           /* Toast.makeText(this, R.string.err_msg_nointernet+"", Toast.LENGTH_SHORT).show();*/
            showToast(this.getResources().getString(R.string.err_msg_nointernet));
        }

    }

    private void callRemoveFromCartAPI(String authKey, String userId) {

        RemoveFromCart removeFromCart = new RemoveFromCart(userId, productId);
        if (EzzShoppUtils.isConnectedToInternet(this)) {
            WebServices<RemoveFromCartResponse> webServices = new WebServices<RemoveFromCartResponse>(this);
            webServices.removeFromCart(WebServices.BASE_URL, WebServices.ApiType.removeFromCart, authKey, userId, removeFromCart);
        } else {
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
           /* Toast.makeText(this, R.string.err_msg_nointernet+"", Toast.LENGTH_SHORT).show();*/
            showToast(this.getResources().getString(R.string.err_msg_nointernet));
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.vI_cid_plus_sign:
                if(mProgressBar!=null)
                {
                    if(!mProgressBar.isShown())
                    {
                        incrementValues();

                    }
                }

                break;
            case R.id.vI_cid_minus_sign:

                if(mProgressBar!=null)
                {
                    if(!mProgressBar.isShown())
                    {
                        decrementValues();

                    }
                }

                break;
            case R.id.vI_cid_item_image:
                //previewPicture();
                break;
            /*case R.id.vB_cid_addto_cart:
                //updateCart();
                callAddToCartAPI();
                break;*/

            case R.id.vL_nh_home_layout:
                startActivity(HomeActivity.class);
                break;

            case R.id.idt_navigation_menu_icon:
                openNavigationDrawer();
                break;

            case R.id.vI_nh_back:
                closeNavigationDrawer();
                break;

            case R.id.idt_search_icon:
                goToSearchActivity();
                break;

            case R.id.idt_cart_icon_layout:
                goToCartActivity();
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
            case  R.id.vL_nh_login_layout:
                startActivity(LogInActivity.class);
                break;

            default:
                closeNavigationDrawer();

        }

    }

    private void gotoMyProfileActivity() {
        startActivity(UserProfileActivity.class);
    }

    private void goToMyCouponsActivity() {

        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, null);
        Intent coupenIntent = new Intent(this, CoupensActivity.class);
        coupenIntent.putExtra("FROM", "NAVIGATIONDRAWER");
        startActivity(coupenIntent, transitionActivityOptions.toBundle());
        //startActivity(CoupensActivity.class);
        startActivity(CoupensActivity.class);
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

                ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(ItemDescriptionActivity.this, null);

                Intent intent = new Intent(ItemDescriptionActivity.this, LogInActivity.class);
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


    private void goToCartActivity() {
        startActivity(CartActivity.class);
    }

    private void goToSearchActivity() {

        startActivity(SearchActivity.class);
    }

    private void incrementValues() {
        int numberOfItems = Integer.parseInt(mNumberOfItems.getText().toString());
        float pricePerUnit = Float.parseFloat(mItemCost.getText().toString());

        if (isLoggedIn) {
            if (numberOfItems < maxQuantity) {
                numberOfItems++;
                callAddToCartAPI(numberOfItems);
                mNumberOfItems.setText(String.valueOf(numberOfItems));
            /*float totalCost = numberOfItems * pricePerUnit;
            mTotalCost.setText(String.valueOf(totalCost));*/

                totalCost = Double.valueOf(numberOfItems * pricePerUnit);
                mTotalCost.setText(new DecimalFormat("##.##").format(totalCost));
            } else {
                Snackbar.make(mItemDescription, "Cart value exceeds", Snackbar.LENGTH_LONG).show();
            }
        } else {
            if (numberOfItems < maxQuantity) {
                numberOfItems++;
                incrementCartAndSaveLocally(numberOfItems);
                mNumberOfItems.setText(String.valueOf(numberOfItems));
              /*float totalCost = numberOfItems * pricePerUnit;
               mTotalCost.setText(String.valueOf(totalCost));*/

                totalCost = Double.valueOf(numberOfItems * pricePerUnit);
                mTotalCost.setText(new DecimalFormat("##.##").format(totalCost));
            } else {
                Snackbar.make(mItemDescription, "Cart value exceeds", Snackbar.LENGTH_LONG).show();
            }

        }

    }

    private void decrementValues() {
        int numberOfItems = Integer.parseInt(mNumberOfItems.getText().toString());
        float pricePerUnit = Float.parseFloat(mItemCost.getText().toString());
        if (isLoggedIn) {
            if (numberOfItems > 1) {
                numberOfItems--;
                mNumberOfItems.setText(String.valueOf(numberOfItems));
                callAddToCartAPI(numberOfItems);
                double totalCost = numberOfItems * pricePerUnit;
                mTotalCost.setText(new DecimalFormat("##.##").format(totalCost));

            } else if (numberOfItems == 1) {
                callRemoveFromCartAPI(authKey, userId);
                numberOfItems--;
                mNumberOfItems.setText(String.valueOf(numberOfItems));
                double totalCost = numberOfItems * pricePerUnit;
                // mTotalCost.setText(String.valueOf(totalCost));
                mTotalCost.setText(new DecimalFormat("##.##").format(totalCost));

            }
        } else {

            if (numberOfItems >1) {
                numberOfItems--;
                mNumberOfItems.setText(String.valueOf(numberOfItems));
                //incrementCartAndSaveLocally(numberOfItems);
                removeFromLocalCart(numberOfItems);
                double totalCost = numberOfItems * pricePerUnit;
                mTotalCost.setText(new DecimalFormat("##.##").format(totalCost));

            } else if (numberOfItems ==1) {
                numberOfItems--;
                incrementCartAndSaveLocally(numberOfItems);
                mNumberOfItems.setText(String.valueOf(numberOfItems));
                double totalCost = numberOfItems * pricePerUnit;
                // mTotalCost.setText(String.valueOf(totalCost));
                mTotalCost.setText(new DecimalFormat("##.##").format(totalCost));

            }

        }
    }

    private void goToAddressActivity() {
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, null);
        Intent coupenIntent = new Intent(this, SelectAddressActivity.class);
        coupenIntent.putExtra("FROM", "NAVIGATIONDRAWER");
        startActivity(coupenIntent, transitionActivityOptions.toBundle());
//        startActivity(SelectAddressActivity.class);
    }

    private void goToMyOrdersActivity() {
        startActivity(OrderHistoryActivity.class);
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

    private void previewPicture() {
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, null);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.delivery);
        Intent pictireIntent = new Intent(this, PreviewPictureActivity.class);
        pictireIntent.putExtra("imageURL", itemImageURl);
        startActivity(pictireIntent, transitionActivityOptions.toBundle());

    }

  /*  void previewPicture()
    {
        Bundle bundle = new Bundle();
        bundle.putString("imageurl",itemImageURl);
        PreviewPictureDialogFragment previewPictureDialogFragment=new PreviewPictureDialogFragment();
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction ft=manager.beginTransaction();
        //ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_in);
        previewPictureDialogFragment.setCancelable(false);
        previewPictureDialogFragment.setArguments(bundle);
        previewPictureDialogFragment.show(ft,"previewPictureFragment");
    }*/

    private void startActivity(Class<?> tClass) {
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, null);
        startActivity(new Intent(ItemDescriptionActivity.this, tClass), transitionActivityOptions.toBundle());
        //overridePendingTransition(R.anim.slide_in,R.anim.slide_in);
        //this.finish();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Transition transition;
        transition = buildEnterTransition();
        getWindow().setEnterTransition(transition);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Transition buildEnterTransition() {
        Slide enterTransition = new Slide();
        //enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        return enterTransition;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        Visibility returnTransition = buildReturnTransition();
        getWindow().setReturnTransition(returnTransition);

        finishAfterTransition();
        //startActivity(HomeActivity.class);
        super.onBackPressed();
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
        callViewCartAPI(authKey, userId);
        callItemDescriptionAPI(authKey, userId);
    }

    @Override
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {
        switch (URL) {
            case itemDescription:
                if (mProgressBar.isShown()) {
                    mProgressBar.setVisibility(View.GONE);
                }
                if (isSucces) {
                    ItemDescriptionResponse itemDescriptionResponse = (ItemDescriptionResponse) response;
                    if (itemDescriptionResponse != null) {
                        if (itemDescriptionResponse.getResponsecode() != null) {
                            if (itemDescriptionResponse.getResponsecode().equalsIgnoreCase("200")) {
                                // Response res= (Response) itemDescriptionResponse.getResponse();
                                List<Response> mList = new ArrayList<>();
                                mList = itemDescriptionResponse.getResponse();
                                updateViews(mList);

                            } else {
                                //Response res= (Response) itemDescriptionResponse.getResponse();
                                //updateViews(res);
                                Snackbar.make(mItemDescription, "Description Not Available For this Item", Snackbar.LENGTH_LONG).show();

                                //Snackbar.make(mAddToCart,"Description Not Available For this Item",Snackbar.LENGTH_LONG).show();

                            }
                        }
                    }
                } else {
                    //Api call failed
                    Snackbar.make(mItemDescription, "API call failed", Snackbar.LENGTH_LONG).show();
                }
                break;

            case viewCart:
                if (isSucces) {
                    ViewCartResponse cartResponse = (ViewCartResponse) response;
                    if (cartResponse != null) {
                        if (cartResponse.getResponsecode() != null) {


                            if (cartResponse.getResponsecode().equalsIgnoreCase("200")) {
                                List<com.zikrabyte.organic.api_responses.viewcart.Response> mCartItemList = cartResponse.getResponse();
                                if (mCartItemList.isEmpty()) {
                                    //showEmptyLayout();
                                    mCartValue.setText("0");
                                } else {
                                    cartItemCount = mCartItemList.size();
                                    mCartValue.setText(cartItemCount + "");
                                }
                            } else {
                                //Something went Wrong
                            }
                        }
                    }

                } else {
                    //API call Failure
                    mCartValue.setText("0");
                    //Snackbar.make(mItemDescription,"API call failed",Snackbar.LENGTH_LONG).show();
                }
                break;

            case addToCart:
                if (isSucces) {
                    AddToCartResponse addToCartResponse = (AddToCartResponse) response;
                    if (addToCartResponse != null) {
                        if (addToCartResponse.getResponsecode() != null) {


                            if (addToCartResponse.getResponsecode().equalsIgnoreCase("200")) {
                                callViewCartAPI(authKey, userId);

                                //int curremtCartItems=Integer.parseInt(mCartValue.getText().toString());

                                if (addToCartResponse.getUpdateCartMessage() != null) {
                                    //Toast.makeText(this, addToCartResponse.getUpdateCartMessage() + "", Toast.LENGTH_SHORT).show();

                                    showToast(addToCartResponse.getUpdateCartMessage() + "");
                                }

                                //Toast.makeText(this, "Cart updated ...", Toast.LENGTH_SHORT).show();
                            } else {

                                if (addToCartResponse.getUpdateCartMessage() != null) {
                                    //Toast.makeText(this, addToCartResponse.getUpdateCartMessage() + "", Toast.LENGTH_SHORT).show();

                                    showToast(addToCartResponse.getUpdateCartMessage() + "");
                                }
                                // Toast.makeText(this, "Failed to add,please Try again", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }

                } else {

                    // Snackbar.make(mItemDescription,"API call failed",Snackbar.LENGTH_LONG).show();

                }
                break;

            case removeFromCart:
                if (isSucces) {
                    RemoveFromCartResponse removeFromCartResponse = (RemoveFromCartResponse) response;
                    if (removeFromCartResponse != null) {
                        if (removeFromCartResponse.getResponsecode() != null) {
                            if (removeFromCartResponse.getResponsecode().equalsIgnoreCase("200")) {
                               /* Intent intent=new Intent(context,CartActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);*/
                                //context.startActivity(new Intent(context,CartActivity.class));

                                callViewCartAPI(authKey, userId);
                                if (removeFromCartResponse.getRemoveCartMessage() != null) {
                                    showToast(removeFromCartResponse.getRemoveCartMessage() + "");
                                }
                                //Toast.makeText(this, removeFromCartResponse.getRemoveCartMessage() + "", Toast.LENGTH_SHORT).show();
                            } else {

                                if (removeFromCartResponse.getRemoveCartMessage() != null) {
                                    showToast(removeFromCartResponse.getRemoveCartMessage() + "");
                                }
                                //Toast.makeText(this, "Failed to remove item from cart", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                break;

            case viewUserProfile:
                if (isSucces) {
                    ViewUserProfileResponse viewUserProfileResponse = (ViewUserProfileResponse) response;
                    if (viewUserProfileResponse != null) {
                        if (viewUserProfileResponse.getResponsecode() != null) {
                            if (viewUserProfileResponse.getResponsecode().equalsIgnoreCase("200")) {
                                List<com.zikrabyte.organic.api_responses.viewuserprofile.Response> userinfoList = viewUserProfileResponse.getResponse();
                                updateUserProfile(userinfoList);
                            } else {
                                Snackbar.make(mItemDescription, "Failed to get user information", Snackbar.LENGTH_LONG).show();

                            }
                        }
                    } else {
                        // Snackbar.make(recyclerView,"user information is empty",Snackbar.LENGTH_LONG).show();

                    }


                } else {
                    //API call failed
                }
                break;
        }


    }

    private void updateUserProfile(List<com.zikrabyte.organic.api_responses.viewuserprofile.Response> mList) {

        SharedPreferences preferences = getSharedPreferences("USER_DETAILS", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("USERNAME", mList.get(0).getFname());
        editor.putString("EMAIL", mList.get(0).getEmail());
        editor.putString("PHONE_NUMBER", mList.get(0).getPhone());
        editor.putString("USER_ID", userId);
        editor.apply();

        userName = mList.get(0).getFname();
        userEmail = mList.get(0).getEmail();
        userPhoneNumber = mList.get(0).getPhone();
        userPicture = mList.get(0).getPicture();

        mProfileName.setText(userName);

        if (userPicture == null || userPicture.isEmpty()) {

            Picasso.get()
                    .load(R.drawable.user)
                    .placeholder(R.drawable.background_drawable)
                    .error(R.drawable.user)
                    .resize(150, 150)
                    .into(mProfilePic);

        } else {

            Picasso.get()
                    .load(userPicture)
                    .placeholder(R.drawable.background_drawable)
                    .error(R.drawable.user)
                    .resize(150, 150)
                    .into(mProfilePic);
        }


    }


    private void updateViews(List<Response> res) {
        if (res != null) {

            maxQuantity = Integer.parseInt(res.get(0).getMaxQuantity());
            itemType = res.get(0).getType();

            if (res.get(0).getPicture() == null || res.get(0).getPicture().isEmpty()) {

                itemImageURl=res.get(0).getPicture();
                Picasso.get()
                        .load(R.drawable.user)
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.user)
                        .into(mItemImage);

            } else {
                /*Glide.with(this)
                        .asBitmap()
                        .load(res.get(0).getPicture())
                        .into(mItemImage);*/
                Picasso.get()
                        .load(res.get(0).getPicture())
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.user)
                        .into(mItemImage);


            }

            mItemName.setText(res.get(0).getTitle());
            mItemDescription.setText(res.get(0).getDescription());
            mItemCost.setText(res.get(0).getNewPrice());
            mItemDiscount.setText(String.valueOf(res.get(0).getOffer()));
            mItemRating.setText(String.valueOf(res.get(0).getRating()));
            mNumberOfItems.setText(String.valueOf(res.get(0).getQuantity()));
            mTotalCost.setText(String.valueOf(res.get(0).getTotalAmt()));

            if (res.get(0).getType() != null) {
                mQuantityType.setText("/" + res.get(0).getType());

            }

            if (!isLoggedIn) {
                mItemName.setText(res.get(0).getTitle());
                mItemDescription.setText(res.get(0).getDescription());
                mItemCost.setText(res.get(0).getNewPrice());
                mItemDiscount.setText(String.valueOf(res.get(0).getOffer()));
                mItemRating.setText(String.valueOf(res.get(0).getRating()));
                mNumberOfItems.setText(quantity + "");

                Double costPerItem = Double.valueOf(res.get(0).getNewPrice());

                Double totalCost = costPerItem * quantity;
                //mTotalCost.setText(String.valueOf(totalCost));
                mTotalCost.setText(new DecimalFormat("##.##").format(totalCost));

                if (res.get(0).getType() != null) {
                    mQuantityType.setText("/" + res.get(0).getType());

                }
            }
        } else {
            initializeViews();
        }

    }

    private AnimationDrawable getProgressBarAnimation() {

        GradientDrawable rainbow1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW});

        GradientDrawable rainbow2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{Color.YELLOW, Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN});

        GradientDrawable rainbow3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{Color.GREEN, Color.YELLOW, Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN});

        GradientDrawable rainbow4 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED, Color.MAGENTA, Color.BLUE});

        GradientDrawable rainbow5 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED, Color.MAGENTA});

        GradientDrawable rainbow6 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED});


        GradientDrawable[] gds = new GradientDrawable[]{rainbow1, rainbow2, rainbow3, rainbow4, rainbow5, rainbow6};

        AnimationDrawable animation = new AnimationDrawable();

        for (GradientDrawable gd : gds) {
            animation.addFrame(gd, 100);

        }

        animation.setOneShot(false);

        return animation;


    }

    public void showToast(String message) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
            // v.setBackground(getResources().getDrawable(R.drawable.disabled_button));
        }
    }

    public void setCartValue(int size) {
        mCartValue.setText(size + "");

    }

    private void removeFromLocalCart(int quantity)
    {
        String itemPrice=mItemCost.getText().toString();
        String totalCost=mTotalCost.getText().toString();
        String itemName=mItemName.getText().toString();
        if (quantity >=1) {

            showToast("Cart updated");

            for (int i = 0; i < HomeActivity.addToCartList.size(); i++) {

                if (productId.equalsIgnoreCase(HomeActivity.addToCartList.get(i).getPriductId()) && itemType.equalsIgnoreCase(HomeActivity.addToCartList.get(i).getPriductQuantityType())) {
                    //subtotal=quantity*costPerItem;
                    //mSubTotal.setText(String.valueOf(subTotalAmount+subtotal));
                    HomeActivity.addToCartList.get(i).setPriductQuantity(String.valueOf(quantity));
                    HomeActivity.addToCartList.get(i).setProductSubTotal(String.valueOf(totalCost));
                    mySharedPreference.saveCart(this, HomeActivity.addToCartList);


                    //mSubTotal.setText(String.valueOf(new DecimalFormat("##.##").format(subTotalAmount)));
                }

            }
        }
    }

    private void incrementCartAndSaveLocally(int quantity) {

        String itemPrice=mItemCost.getText().toString();
        String totalCost=mTotalCost.getText().toString();
        String itemName=mItemName.getText().toString();
        if(quantity==1)
        {


            if(!data.contains(productId))
            {
                showToast("Item added to cart");
                data.add(productId);
                producttype.add(itemType);
                AddToCartDummy addToCartDummy=new AddToCartDummy(productId,itemName,itemPrice,quantity+"",itemType,itemImageURl,totalCost+"",maxQuantity+"");
                HomeActivity.addToCartList.add(addToCartDummy);
                mySharedPreference.saveCart(this,HomeActivity.addToCartList);
            }

            //}
            Log.d("localdata1","size=>"+HomeActivity.addToCartList.size()+"");

        }

        if (quantity > 1) {

            showToast("Cart updated");

            for (int i = 0; i < HomeActivity.addToCartList.size(); i++) {

                if (productId.equalsIgnoreCase(HomeActivity.addToCartList.get(i).getPriductId()) && itemType.equalsIgnoreCase(HomeActivity.addToCartList.get(i).getPriductQuantityType())) {
                    //subtotal=quantity*costPerItem;
                    //mSubTotal.setText(String.valueOf(subTotalAmount+subtotal));
                    HomeActivity.addToCartList.get(i).setPriductQuantity(String.valueOf(quantity));
                    HomeActivity.addToCartList.get(i).setProductSubTotal(String.valueOf(totalCost));
                    mySharedPreference.saveCart(this, HomeActivity.addToCartList);


                    //mSubTotal.setText(String.valueOf(new DecimalFormat("##.##").format(subTotalAmount)));
                }

            }

        } else if (quantity < 1) {

            showToast("Item removed from cart");

            for (int i = 0; i < HomeActivity.addToCartList.size(); i++) {

                if (productId.equalsIgnoreCase(HomeActivity.addToCartList.get(i).getPriductId()) && itemType.equalsIgnoreCase(HomeActivity.addToCartList.get(i).getPriductQuantityType())) {
                    HomeActivity.addToCartList.remove(i);
                    data.remove(productId);

                    mySharedPreference.saveCart(this, HomeActivity.addToCartList);

                }
                //addToCartList.add(new AddToCartDummy(productId,ProductName,productQuantity,productQuantityType,productImage));
            }
        }


        if ( HomeActivity.addToCartList != null) {
            HomeActivity.setCartValue( HomeActivity.addToCartList.size());
            setCartValue(HomeActivity.addToCartList.size());
        }
    }
}
