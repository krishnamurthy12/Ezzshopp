package com.zikrabyte.organic.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.Visibility;
import android.util.Base64;
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

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.zikrabyte.organic.R;
import com.zikrabyte.organic.api_requests.ViewUserProfile;
import com.zikrabyte.organic.api_responses.edituserdetails.EditUserDetailsResponse;
import com.zikrabyte.organic.api_responses.edituserprofile.EditUserProfileResponse;
import com.zikrabyte.organic.api_responses.viewuserprofile.Response;
import com.zikrabyte.organic.api_responses.viewuserprofile.ViewUserProfileResponse;
import com.zikrabyte.organic.customs.CircleImageView;
import com.zikrabyte.organic.utils.EzzShoppUtils;
import com.zikrabyte.organic.utils.OnResponseListener;
import com.zikrabyte.organic.utils.WebServices;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener,OnResponseListener {

    /*Navigation drawer elements*/
    DrawerLayout drawer;
    NavigationView navigationView;
    ImageView mNavigationMenuIcon,mNavigationBack;

    LinearLayout mHome,mMyProfileLayout,mMyAddressLayout,mMyCartLayout,mMyOrdersLayout,mMyCouponsLayout,mAboutUsLayout,mContactUsLayout,mShareLayout,mRateUsLayout,mLogOutLayout;
    CircleImageView mProfilePic;
    TextView mProfileName;

    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    String authKey,userId,userName,userEmail,userPhoneNumber,userPicture;

    Button mUpdateProfile;
    EditText mUsername,mUserEmail;
    TextView mUserPhoneNumber;
    CircleImageView mUserProfilePic;
    ImageView mEditProfilePic;
    ProgressBar mProgressBar;

    String userChoosenTask;
    public static String selectedFilePath;
    private static final int SELECT_PICTURE=100,CAPTURE_PICTURE=101;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = { Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    private Uri fileUri;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    public static final int MEDIA_TYPE_IMAGE = 1;

    ProgressDialog progressDialog;
    static String originalPictureUrl;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setupWindowAnimations();
        initializeViews();
    }
    private void initializeViews()
    {
        progressDialog=new ProgressDialog(this);

        mProgressBar=findViewById(R.id.vP_aup_progressbar);
        //mActionBack=findViewById(R.id.vI_aup_back_icon);
        mEditProfilePic=findViewById(R.id.vI_aup_edit_profile_picture);

        mUsername=findViewById(R.id.vE_aup_name);
        mUserEmail=findViewById(R.id.vE_aup_email);
        mUserPhoneNumber=findViewById(R.id.vE_aup_phone);
        mUserProfilePic =findViewById(R.id.vCIV_aup_profile_pic);

        mUpdateProfile=findViewById(R.id.vB_aup_update);

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

        mUpdateProfile.setOnClickListener(this);
        //mActionBack.setOnClickListener(this);
        mEditProfilePic.setOnClickListener(this);

        getSharedPreferenceData();

    }

    private void getSharedPreferenceData()
    {
        SharedPreferences preferences=getSharedPreferences("LOGIN_PREFERENCE",MODE_PRIVATE);
        authKey=preferences.getString("AUTH_KEY",null);
        userId=preferences.getString("USER_ID",null);
        userName=preferences.getString("USER_NAME",null);
        userEmail=preferences.getString("EMAIL_ID",null);

        callViewUserProfileAPI(authKey,userId);
    }

    private void callViewUserProfileAPI(String authKey,String userId)
    {
        ViewUserProfile viewUserProfile=new ViewUserProfile(userId);
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            mProgressBar.setVisibility(View.VISIBLE);
            WebServices<ViewUserProfileResponse> webServices = new WebServices<ViewUserProfileResponse>(UserProfileActivity.this);
            webServices.viewUserProfile(WebServices.BASE_URL, WebServices.ApiType.viewUserProfile, authKey, userId);
        } else {
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet)+"", Toast.LENGTH_SHORT).show();
        }

    }

    private void callEditUserProfileAPI(String name,String email,String phone,File file)
    {

        if(file!=null) {
            //File file = new File(filepath);
            if(mProgressBar.isShown())
            {
                mProgressBar.setVisibility(View.GONE);
            }

            RequestBody uid = RequestBody.create(MediaType.parse("text/plain"), userId);
            RequestBody uname = RequestBody.create(MediaType.parse("text/plain"), name);
            RequestBody uemail = RequestBody.create(MediaType.parse("text/plain"), email);
            RequestBody uphone = RequestBody.create(MediaType.parse("text/plain"), phone);

            RequestBody fpath = RequestBody.create(MediaType.parse("image/*"), file);
       /* ProgressRequestBody progresssBody=new ProgressRequestBody(file,this);*/
            MultipartBody.Part myFile = MultipartBody.Part.createFormData("picture", file.getName(), fpath);

            if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {
                mProgressBar.setVisibility(View.VISIBLE);

           /* progressDialog.setMessage("Updating Profile,Please wait");
            progressDialog.show();
*/
                WebServices<EditUserProfileResponse> webServices = new WebServices<EditUserProfileResponse>(UserProfileActivity.this);
                webServices.editUserProfile(WebServices.BASE_URL, WebServices.ApiType.editUserProfile, authKey, userId, uid, uname, uemail, uphone, myFile);
            } else {
                //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
                Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet) + "", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void callUpdateUserProfileAPI(String name,String email,String phone,String picture)
    {

            if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {
                mProgressBar.setVisibility(View.VISIBLE);

                WebServices<EditUserDetailsResponse> webServices = new WebServices<EditUserDetailsResponse>(UserProfileActivity.this);
                webServices.editUserProfileDetails(WebServices.BASE_URL, WebServices.ApiType.updateUserDetails, authKey, userId, userId, name,
                        email, phone,picture);
            } else {
                //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
                Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet) + "", Toast.LENGTH_SHORT).show();
            }
        }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            /*case R.id.vI_aup_back_icon:
                onBackPressed();
                break;*/

            case R.id.vB_aup_update:
                updateProfile();
                break;

            case R.id.vI_aup_edit_profile_picture:
                selectImage();
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

                ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(UserProfileActivity.this,null);

                Intent intent=new Intent(UserProfileActivity.this,LogInActivity.class);
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

    private void updateProfile() {

        userName = mUsername.getText().toString().trim();
        userEmail = mUserEmail.getText().toString().trim();
        userPhoneNumber = mUserPhoneNumber.getText().toString().trim();


         if (!validateUserName())
        {
            return;
        }
         else if(!validateEmail())
         {
             return;

         }
         else {
             if(originalPictureUrl!=null) {
                 callUpdateUserProfileAPI(userName, userEmail, userPhoneNumber, originalPictureUrl);
             }
             else {
                 callUpdateUserProfileAPI(userName, userEmail, userPhoneNumber, "");
             }
         }
        //userPicture=selectedFilePath;

      /*  SharedPreferences profilePic=getSharedPreferences("USER_PICTURE",MODE_PRIVATE);
        String presentPicture=profilePic.getString("USERPIC",null);*/


    }

    private void callUploadProfilePic(File selectedFilePath) {

        userName = mUsername.getText().toString().trim();
        userEmail = mUserEmail.getText().toString().trim();
        userPhoneNumber = mUserPhoneNumber.getText().toString().trim();
        //userPicture=selectedFilePath;

        if (!validateUserName())
        {
            return;
        }
        else if(!validateEmail())
        {
            return;

        }
        else {
            callEditUserProfileAPI(userName,userEmail,userPhoneNumber,selectedFilePath);
        }

        Log.d("profilepicture_path","path while selecting"+selectedFilePath);


    }

    private boolean validateUserName() {

        String userName=mUsername.getText().toString().trim();

        if (userName.isEmpty() || userName.length()<3 || !isValidUserName(userName)) {
            Snackbar.make(mUpdateProfile,R.string.err_msg_name, Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    private boolean validateEmail() {
        String email = mUserEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            Snackbar.make(mUpdateProfile,R.string.err_msg_email, Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidUserName(String name)
    {
        String regexUserName = "^[A-Za-z\\s]+$";
        Pattern p = Pattern.compile(regexUserName, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(name);
        return m.matches();
    }
    @Override
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {

        switch (URL)
        {
            case viewUserProfile:
                if(mProgressBar.isShown())
                {
                    mProgressBar.setVisibility(View.GONE);
                }
                ViewUserProfileResponse viewUserProfileResponse= (ViewUserProfileResponse) response;
                if(isSucces)
                {
                    if(viewUserProfileResponse!=null)
                    {
                        if(viewUserProfileResponse.getResponsecode()!=null) {
                            if (viewUserProfileResponse.getResponsecode().equalsIgnoreCase("200")) {
                                List<Response> userinfoList = viewUserProfileResponse.getResponse();
                                updateUserProfile(userinfoList);
                            } else {
                                if(viewUserProfileResponse.getProfileMessage()!=null)
                                {
                                    Snackbar.make(mUpdateProfile, viewUserProfileResponse.getProfileMessage(), Snackbar.LENGTH_LONG).show();
                                }


                            }
                        }
                    }
                    else
                    {
                        //empty response
                    }


                }
                else
                {
                    //API call failed
                    if(viewUserProfileResponse!=null) {
                        if (viewUserProfileResponse.getProfileMessage() != null) {
                            Snackbar.make(mUpdateProfile, viewUserProfileResponse.getProfileMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }

                }
                break;

            case editUserProfile:
                EditUserProfileResponse editUserProfileResponse= (EditUserProfileResponse) response;

                if(mProgressBar.isShown())
                {
                    mProgressBar.setVisibility(View.GONE);
                }
                if (isSucces)
                {
                    if(editUserProfileResponse!=null)
                    {
                        if(editUserProfileResponse.getResponsecode()!=null) {
                            if (editUserProfileResponse.getResponsecode().equalsIgnoreCase("200")) {

                                callViewUserProfileAPI(authKey, userId);

                                if(editUserProfileResponse.getProfileMessage()!=null)
                                {
                                    Snackbar.make(mUpdateProfile, editUserProfileResponse.getProfileMessage(), Snackbar.LENGTH_LONG).show();
                                }
                            } else {
                                if(editUserProfileResponse.getProfileMessage()!=null)
                                {
                                    Snackbar.make(mUpdateProfile, editUserProfileResponse.getProfileMessage(), Snackbar.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                    else {
                        Snackbar.make(mUpdateProfile,"Can not update profile at this moment",Snackbar.LENGTH_LONG).show();
                    }

                }
                else
                {
                    //API call failed
                    if(editUserProfileResponse!=null) {
                        if (editUserProfileResponse.getProfileMessage() != null) {
                            Snackbar.make(mUpdateProfile, editUserProfileResponse.getProfileMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
                break;

            case updateUserDetails:
                EditUserDetailsResponse editUserDetailsResponse= (EditUserDetailsResponse) response;
                if(mProgressBar.isShown())
                {
                    mProgressBar.setVisibility(View.GONE);
                }
                if (isSucces)
                {

                    if(editUserDetailsResponse!=null)
                    {
                        if(editUserDetailsResponse.getResponsecode()!=null) {
                            if (editUserDetailsResponse.getResponsecode().equalsIgnoreCase("200")) {

                                callViewUserProfileAPI(authKey, userId);

                                if(editUserDetailsResponse.getProfileMessage()!=null)
                                {
                                    Snackbar.make(mUpdateProfile, editUserDetailsResponse.getProfileMessage(), Snackbar.LENGTH_LONG).show();
                                }

                            } else {
                                if(editUserDetailsResponse.getProfileMessage()!=null)
                                {
                                    Snackbar.make(mUpdateProfile, editUserDetailsResponse.getProfileMessage(), Snackbar.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                    else {
                        Snackbar.make(mUpdateProfile,"Can not update profile at this moment",Snackbar.LENGTH_LONG).show();
                    }

                }
                else
                {
                    //API call failed
                    if(editUserDetailsResponse!=null) {
                        if (editUserDetailsResponse.getProfileMessage() != null) {
                            Snackbar.make(mUpdateProfile, editUserDetailsResponse.getProfileMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
                break;
        }

    }

    private void updateUserProfile(List<com.zikrabyte.organic.api_responses.viewuserprofile.Response> mList) {

        userName=mList.get(0).getFname();
        userEmail=mList.get(0).getEmail();
        userPhoneNumber=mList.get(0).getPhone();
        userPicture=mList.get(0).getPicture();

        originalPictureUrl=userPicture;
        //originalPictureUrl=mList.get(0).getPicture();
        mProfileName.setText(userName);

        /*To be used in payment gateway*/
        SharedPreferences preferences=getSharedPreferences("USER_DETAILS",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("USERNAME",mList.get(0).getFname());
        editor.putString("EMAIL",mList.get(0).getEmail());
        editor.putString("PHONE_NUMBER",mList.get(0).getPhone());
        editor.putString("USER_ID",userId);
        editor.apply();

        Log.d("userpicture",userPicture);

        mUsername.setText(userName);
        mUserEmail.setText(userEmail);
        mUserPhoneNumber.setText(userPhoneNumber);
        /*Glide.with(this)
                .asBitmap()
                .load(userPicture.trim())
                .into(mUserProfilePic);*/

        if(userPicture==null || userPicture.isEmpty())
        {
            Picasso.get()
                    .load(R.drawable.user)
                    .placeholder(R.drawable.background_drawable)
                    .error(R.drawable.user)
                    .into(mUserProfilePic);

            Picasso.get()
                    .load(R.drawable.logo)
                    .placeholder(R.drawable.background_drawable)
                    .error(R.drawable.user)
                    //.resize(150,150)
                    .into(mProfilePic);

        }
        else {
            Picasso.get()
                    .load(userPicture)
                    .placeholder(R.drawable.background_drawable)
                    .error(R.drawable.user)
                    .into(mUserProfilePic);

            Picasso.get()
                    .load(userPicture)
                    .placeholder(R.drawable.background_drawable)
                    .error(R.drawable.user)
                    //.resize(150,150)
                    .into(mProfilePic);
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Visibility buildReturnTransition() {
        Visibility enterTransition = new Slide();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        return enterTransition;
    }
    private void startActivity(Class<?> tClass)
    {
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this,null);
        startActivity(new Intent(UserProfileActivity.this,tClass),transitionActivityOptions.toBundle());
        //overridePendingTransition(R.anim.slide_in,R.anim.slide_in);
        //this.finish();
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library","Remove Picture",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //boolean result=Utility.checkPermission(HomePageActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    //if(result)
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    //if(result)
                    galleryIntent();
                }
                else if (items[item].equals("Remove Picture")) {
                    userChoosenTask="Remove Picture";
                    //if(result)
                    removePicture();
                }
                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

    private void removePicture() {

        userName = mUsername.getText().toString().trim();
        userEmail = mUserEmail.getText().toString().trim();
        userPhoneNumber = mUserPhoneNumber.getText().toString().trim();


        if (!validateUserName())
        {
            return;
        }
        else if(!validateEmail())
        {
            return;

        }
        else {
            callUpdateUserProfileAPI(userName, userEmail, userPhoneNumber, "");
        }
    }

    private void cameraIntent()
    {
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }else {


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //startActivityForResult(intent, CAPTURE_PICTURE);
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(Intent.createChooser(intent, "Complete by using"), CAPTURE_PICTURE);

        }
    }

    private void galleryIntent()
    {
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_PICTURE);

        }
    }
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ALL) {
            if ((permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED )&&
                    (permissions[1].equals(Manifest.permission.CAMERA)
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED ))
            {
                selectImage();

            }
        }
         else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.

                Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (resultCode == RESULT_OK && requestCode == SELECT_PICTURE) {
                Uri selectedUri = data.getData();
                //Glide.with(this).load(selectedUri).into(userPic);
                try {
                    selectedFilePath = getFilePath(this, selectedUri);
                    Log.d("selectedfilepath", "" + selectedFilePath);
                    if (selectedFilePath != null) {
                        Glide.with(this).asBitmap().load(selectedFilePath).into(mUserProfilePic);
                        File file = new File(selectedFilePath);

                        File compressedImageFile = new Compressor(this).compressToFile(file);

                        callUploadProfilePic(compressedImageFile);
                         mProgressBar.setVisibility(View.VISIBLE);
                       /* Bitmap compressedBitmap=shrinkBitmap(selectedFilePath,150,150);
                        String compressedFilePath=BitMapToString(compressedBitmap);*/
                        //callUploadProfilePic(selectedFilePath);
                        Log.d("selectedfile", "" + file.getAbsolutePath());
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == CAPTURE_PICTURE && resultCode == RESULT_OK) {
                //Uri imageUri = data.getData();

                try {
                    if(fileUri!=null) {
                        Glide.with(this).load(fileUri.toString()).into(mUserProfilePic);
                        //callUploadProfilePic(fileUri.getPath());


                    }

                    selectedFilePath = getFilePath(this, fileUri);
                    File file=new File(selectedFilePath);
                    File compressedImageFile = new Compressor(this).compressToFile(file);

                    callUploadProfilePic(compressedImageFile);
                    mProgressBar.setVisibility(View.VISIBLE);

                   /* //Toast.makeText(this, "file path"+imageUri, Toast.LENGTH_LONG).show();
                    if (selectedFilePath != null) {

                        //callUploadProfilePic(selectedFilePath);
                    } else {

                        Snackbar.make(tabLayout, "file is empty", Snackbar.LENGTH_SHORT).show();
                    }*/
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
            else if(requestCode==CAPTURE_PICTURE && resultCode==RESULT_CANCELED)
            {
                Log.d("LogCheck", "inside result code cancel block");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    public Bitmap shrinkBitmap(String file, int width, int height)
    {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

        if(heightRatio > 1 || widthRatio > 1)
        {
            if(heightRatio > widthRatio)
            {
                bmpFactoryOptions.inSampleSize = heightRatio;
            }
            else
            {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
