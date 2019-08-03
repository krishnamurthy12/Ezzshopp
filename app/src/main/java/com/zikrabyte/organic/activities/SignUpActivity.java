package com.zikrabyte.organic.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Visibility;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zikrabyte.organic.R;
import com.zikrabyte.organic.api_requests.LogIn;
import com.zikrabyte.organic.api_requests.Register;
import com.zikrabyte.organic.api_requests.SendOTP;
import com.zikrabyte.organic.api_requests.VerifyOTP;
import com.zikrabyte.organic.api_responses.login.LogInResponse;
import com.zikrabyte.organic.api_responses.register.UserRegistrationResponse;
import com.zikrabyte.organic.api_responses.sendotp.SendOTPResponse;
import com.zikrabyte.organic.api_responses.verifyotp.VerifyOTPResponse;
import com.zikrabyte.organic.utils.EzzShoppUtils;
import com.zikrabyte.organic.utils.MySMSBroadCastReceiver;
import com.zikrabyte.organic.utils.OnResponseListener;
import com.zikrabyte.organic.utils.SmsListener;
import com.zikrabyte.organic.utils.WebServices;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener,OnResponseListener{

    TextView mSignupText,mTermsAndConditionsText;
    Button mSignUp;
    EditText mName,mPhoneNumber,mEmail,mPassword,mConfirmPassword,mReferralCode;
    FrameLayout mContainerLayout;
    CheckBox mAcceptTerms;
    ProgressBar mProgressBar,mVerifyProgressBar;

    TextView mOTPLayoutPhoneNumber,mOTPLayoutResendOTP;
    Button mOTPLayoutVerify;
    EditText mOTP;
    ImageView mShowNewPassword,mHideNewPassword,mShowConfirmPassword,mHideConfirmPassword;

    String receivedPhoneNum,receivedEmail;

    private  String enteredPhoneNumber=null;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    MySMSBroadCastReceiver receiver;

    @Override
    protected void onStart() {
        super.onStart();
        if (checkAndRequestPermissions()) {

            com.zikrabyte.organic.broadcastreceivers.SmsListener smsListener = new com.zikrabyte.organic.broadcastreceivers.SmsListener();
            try {
                unregisterReceiver(smsListener);
            } catch (Exception e) {
            }
            registerReceiver(smsListener, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //setupWindowAnimations();
        initializeViews();

        /*String text = "<font color='hexvalue of green'>This is green</font> <font color='hexvalue of red'>and this is red</font>.";
          myTextView.setText(Html.fromHtml((text));*/
    }


    private boolean receiveIntentData() {
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            receivedEmail=bundle.getString("userEmail",null);
            receivedPhoneNum=bundle.getString("userPhone",null);

            return true;
        }
        else
            return false;

    }
    private void initializeViews()
    {
        receiver=new MySMSBroadCastReceiver();
        mContainerLayout=findViewById(R.id.signup_container_layout);
        mSignupText=findViewById(R.id.vT_asu_signuptext);
        mSignUp=findViewById(R.id.vB_asu_signup);

         String text = "<font color=#909090>Now to fill your </font>" +
                 "<font color=#DE7E86>Login</font>"+
                 "<font color=#909090> Information</font>.";
          mSignupText.setText(Html.fromHtml(text));

          mName=findViewById(R.id.vE_asu_username);
          mEmail=findViewById(R.id.vE_asu_email);
          mPhoneNumber=findViewById(R.id.vE_asu_phone_number);
          mPassword=findViewById(R.id.vE_asu_pass);
          mConfirmPassword=findViewById(R.id.vE_asu_confirmpass);
          mReferralCode=findViewById(R.id.vE_asu_referral_code);
          mAcceptTerms=findViewById(R.id.vC_asu_terms_and_conditions);
          mProgressBar=findViewById(R.id.vP_asu_progress_bar);
          mTermsAndConditionsText=findViewById(R.id.vT_asu_terms_and_conditions_text);
          mTermsAndConditionsText.setPaintFlags(mTermsAndConditionsText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mShowNewPassword=findViewById(R.id.vI_asu_show_new_password);
        mHideNewPassword=findViewById(R.id.vI_asu_hide_new_password);
        mShowConfirmPassword=findViewById(R.id.vI_asu_show_confirm_password);
        mHideConfirmPassword=findViewById(R.id.vI_asu_hide_confirm_password);

        mShowNewPassword.setOnClickListener(this);
        mHideNewPassword.setOnClickListener(this);
        mShowConfirmPassword.setOnClickListener(this);
        mHideConfirmPassword.setOnClickListener(this);

          mSignUp.setOnClickListener(this);
          mTermsAndConditionsText.setOnClickListener(this);

        if(receiveIntentData())
        {
            mPhoneNumber.setText(receivedPhoneNum);
            mEmail.setText(receivedEmail);
        }

    }

    private void callUserSignUpAPI(Register register) {

        //LogIn logIn=new LogIn(mPhoneNumber.getText().toString(),mPassword.getText().toString());
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            toggleVisibility(mProgressBar);

            WebServices<UserRegistrationResponse> webServices = new WebServices<UserRegistrationResponse>(SignUpActivity.this);
            webServices.registerUser(WebServices.BASE_URL, WebServices.ApiType.userSignUp,register);

        } else {
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet)+"", Toast.LENGTH_SHORT).show();
        }
    }

    private void callSendOTPAPI(String phone) {

        SendOTP sendOTP=new SendOTP(phone);

        //LogIn logIn=new LogIn(mPhoneNumber.getText().toString(),mPassword.getText().toString());
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            WebServices<SendOTPResponse> webServices = new WebServices<SendOTPResponse>(SignUpActivity.this);
            webServices.sendOTP(WebServices.BASE_URL, WebServices.ApiType.sendOTP,sendOTP);

        } else {
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet)+"", Toast.LENGTH_SHORT).show();
        }
    }

    private void callVerifyOTPAPI(String phone,String otp) {

        VerifyOTP verifyOTP=new VerifyOTP(phone,otp);

        //LogIn logIn=new LogIn(mPhoneNumber.getText().toString(),mPassword.getText().toString());
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            WebServices<VerifyOTPResponse> webServices = new WebServices<VerifyOTPResponse>(SignUpActivity.this);
            webServices.verifyOTP(WebServices.BASE_URL, WebServices.ApiType.verifyOTP,verifyOTP);

        } else {

            toggleVisibility(mVerifyProgressBar);
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet)+"", Toast.LENGTH_SHORT).show();
        }
    }

    private void callUserLogInAPI() {
        LogIn logIn=new LogIn(mPhoneNumber.getText().toString().trim(),mPassword.getText().toString().trim());
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {
            toggleVisibility(mVerifyProgressBar);

            WebServices<LogInResponse> webServices = new WebServices<LogInResponse>(SignUpActivity.this);
            webServices.userLogIn(WebServices.BASE_URL, WebServices.ApiType.userlogin,logIn);

        } else {
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet)+"", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.vB_asu_signup:
               validateAllFields();
                break;

            case R.id.vB_ol_verify:
                if(validateOTP())
                {
                    verifyOTP();
                }

               break;

            case R.id.vT_ol_resend_otp:
                otpResend();
                break;
            case R.id.vT_asu_terms_and_conditions_text:
                Intent terms=new Intent(this,WebViewActivity.class);
                terms.putExtra("terms","http://www.ezzshopp.com/term-and-conditions.php");
                startActivity(terms);
                break;

            case R.id.vI_asu_show_new_password:
                showPassword(mPassword,mShowNewPassword,mHideNewPassword);
                break;

            case R.id.vI_asu_hide_new_password:
                hidePassword(mPassword,mShowNewPassword,mHideNewPassword);
                break;

            case R.id.vI_asu_show_confirm_password:
                showPassword(mConfirmPassword,mShowConfirmPassword,mHideConfirmPassword);
                break;

            case R.id.vI_asu_hide_confirm_password:
                hidePassword(mConfirmPassword,mShowConfirmPassword,mHideConfirmPassword);
                break;
        }

    }

    private void showPassword(EditText view,View... views)
    {
        for(View v:views)
        {
            toggleVisibility(v);
            //toggleVisibility(v);
        }

        int start,end;


        start=view.getSelectionStart();
        end=view.getSelectionEnd();
        view.setTransformationMethod(null);
        view.setSelection(start,end);


    }
    private void hidePassword(EditText view,View... views)
    {
        for(View v:views)
        {
            toggleVisibility(v);
            //toggleVisibility(v);
        }
        //toggleVisibility(mShowPassword);
        //toggleVisibility(mHidePassword);
        int start,end;

        start=view.getSelectionStart();
        end=view.getSelectionEnd();
        view.setTransformationMethod(new PasswordTransformationMethod());;
        view.setSelection(start,end);

    }

    private void showAndHidePassword()
    {
         /*mShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            int start,end;
            if(!b){
                start=mPassword.getSelectionStart();
                end=mPassword.getSelectionEnd();
                mPassword.setTransformationMethod(new PasswordTransformationMethod());;
                mPassword.setSelection(start,end);
            }else{
                start=mPassword.getSelectionStart();
                end=mPassword.getSelectionEnd();
                mPassword.setTransformationMethod(null);
                mPassword.setSelection(start,end);
            }
        }
    });*/

    }


    private void validateAllFields() {

        if(!validateUserName()) {
            return;
        }
        else if(!validateEmail()){
            return;
        }
        else if (!validatePhoneNumber()) {
            return;
        }
        else if (!validatePassword()) {
            return;
        }
        else if (!validateTermsAndconditions()) {
            return;
        }
       validationSuccess();
    }

    private boolean validateUserName() {

        String userName=mName.getText().toString().trim();

        if (userName.isEmpty() || userName.length()<3 || !isValidUserName(userName)) {
            Snackbar.make(mSignUp,R.string.err_msg_name, Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    private boolean validateEmail() {
        String email = mEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            Snackbar.make(mSignUp,R.string.err_msg_email, Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private boolean validatePhoneNumber()
    {
        String phoneNumber=mPhoneNumber.getText().toString().trim();

        if (phoneNumber.isEmpty()||phoneNumber.length()<10 ||!isValidMobile(phoneNumber)) {
            Snackbar.make(mSignUp,R.string.err_msg_phonenumber, Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private boolean validatePassword() {
        if (mPassword.getText().toString().trim().isEmpty()) {
            Snackbar.make(mSignUp,R.string.err_msg_password, Snackbar.LENGTH_SHORT).show();
            return false;
        }
        else  if(mPassword.getText().toString().trim().length()<8){
            Snackbar.make(mSignUp,R.string.err_msg_password_length, Snackbar.LENGTH_SHORT).show();
            return false;
        }
        else if(!mPassword.getText().toString().trim().equals(mConfirmPassword.getText().toString().trim()))
        {
            Snackbar.make(mSignUp,"Password mismatch",Snackbar.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


    private boolean validateTermsAndconditions()
    {
        if (!(mAcceptTerms.isChecked())) {
            Snackbar.make(mSignUp,R.string.err_msg_termsandconditions, Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidMobile(String phone) {
        return phone.matches("[1-9][0-9]{9}");
    }
    private boolean isValidUserName(String name)
    {
        String regexUserName = "^[A-Za-z\\s]+$";
        Pattern p = Pattern.compile(regexUserName, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(name);
        return m.matches();
    }


    private void validationSuccess()
    {

        String email=mEmail.getText().toString();
        String password=mPassword.getText().toString();
        String fname=mName.getText().toString();
        String phoneNumber=mPhoneNumber.getText().toString();
        String referralCode=mReferralCode.getText().toString();

        //enteredPhoneNumber=mPhoneNumber.getText().toString();

        Register register=new Register(email,password,fname,phoneNumber,referralCode);

        callUserSignUpAPI(register);


    }

    private boolean validateOTP() {

        if (mOTP.getText().toString().trim().isEmpty()||mOTP.getText().toString().trim().length()<4)
        {
            //disableViews(mOTPLayoutVerify);
            return false;
        }
        else {
            //enableViews(mOTPLayoutVerify);

            callVerifyOTPAPI(mPhoneNumber.getText().toString(),mOTP.getText().toString());
                return true;

            //hideKeyBoard();
        }

    }

    private void showOTPLayout()
    {
        View otpLayout=getLayoutInflater().inflate(R.layout.otp_layout,mContainerLayout,true);

        mOTPLayoutResendOTP=otpLayout.findViewById(R.id.vT_ol_resend_otp);
        mOTPLayoutVerify=otpLayout.findViewById(R.id.vB_ol_verify);
        mOTP=otpLayout.findViewById(R.id.vE_ol_otp);
        mOTPLayoutPhoneNumber=otpLayout.findViewById(R.id.vT_ol_phone_number);
        mVerifyProgressBar=otpLayout.findViewById(R.id.vP_ol_progress_bar);

        mOTPLayoutPhoneNumber.setText(enteredPhoneNumber);

        mOTPLayoutResendOTP.setPaintFlags(mOTPLayoutResendOTP.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
       // disableViews(mOTPLayoutVerify);
        mOTPLayoutResendOTP.setOnClickListener(this);
        mOTPLayoutVerify.setOnClickListener(this);

        BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null && intent.hasExtra("varificationCode")) {
                    String youtOTPcode = intent.getStringExtra("varificationCode");

                    Log.d("otpreceived",youtOTPcode);

                    if(youtOTPcode!=null)
                    {
                        mOTP.setText(youtOTPcode);

                    }
                }
            }
        };

        final String DISPLAY_MESSAGE_ACTION = this.getPackageName() + ".CodeSmsReceived";

        Log.d("otpreceived",DISPLAY_MESSAGE_ACTION);


        try {
            this.unregisterReceiver(mHandleMessageReceiver);
        } catch (Exception e) {
        }
        this.registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));

       /* mOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                validateOTP();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        if (checkAndRequestPermissions()) {
            MySMSBroadCastReceiver.bindListener(new SmsListener() {
                @Override
                public void messageReceived(String messageText) {
                    Log.d("Text",messageText);
                    Toast.makeText(SignUpActivity.this,"Message: "+messageText,Toast.LENGTH_LONG).show();

                    String code = parseCode(messageText);//Parse verification code
                    if(code!=null)
                    {
                        mOTP.setText(code);
                    }
                    else {
                        Toast.makeText(SignUpActivity.this, "couldn't fetch OTP", Toast.LENGTH_SHORT).show();
                    }

                    Log.d("Text2",code);
                }
            });

        }
    }

    private void verifyOTP() {

        //callVerifyOTPAPI(mOTPLayoutPhoneNumber.getText().toString(),mOTP.getText().toString());
        Log.d("phonenumber","phone number while verify=>"+enteredPhoneNumber);
        mOTPLayoutPhoneNumber.setText(enteredPhoneNumber);
        callVerifyOTPAPI(enteredPhoneNumber,mOTP.getText().toString());

    }
    private void otpResend() {
        /*showOTPLayout();

        String currentvalue=mOTPLayoutPhoneNumber.getText().toString();
        Log.d("currentphonenumber","phone number while resend=>"+currentvalue);

        mOTPLayoutPhoneNumber.setText(enteredPhoneNumber);*/
        mVerifyProgressBar.setVisibility(View.VISIBLE);
        callSendOTPAPI(enteredPhoneNumber);

        //Log.d("phonenumber","phone number while resend=>"+enteredPhoneNumber);
        //Toast.makeText(this, "Otp has been sent to above number", Toast.LENGTH_SHORT).show();
    }


    private void startActivity(Class<?> tClass)
    {
        hideKeyBoard();
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this,null);
        startActivity(new Intent(SignUpActivity.this,tClass),transitionActivityOptions.toBundle());
        //overridePendingTransition(R.anim.slide_in,R.anim.slide_in);
        //this.finish();
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

    private void enableViews(View... views) {
        for (View v : views) {
            v.setEnabled(true);
            v.setBackground(getResources().getDrawable(R.drawable.roundeded_button));

        }
    }

    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
            v.setBackground(getResources().getDrawable(R.drawable.disabled_button));
        }
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

    private void setupWindowAnimations() {
        Visibility enterTransition = buildEnterTransition();
        getWindow().setEnterTransition(enterTransition);
    }


    private Visibility buildEnterTransition() {
        Explode enterTransition = new Explode();
        enterTransition.setDuration(1000);
        enterTransition.setMode(Visibility.MODE_IN);
        // This view will not be affected by enter transition animation
        enterTransition.excludeTarget(R.id.toolbar_layout, true);
        return enterTransition;
    }

    private Visibility buildReturnTransition() {
        Visibility enterTransition = new Slide();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        return enterTransition;
    }


    @Override
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {

        switch (URL)
        {
            case userSignUp:
                if(mProgressBar.isShown()) {
                    toggleVisibility(mProgressBar);
                }
                if(isSucces)
                {
                    UserRegistrationResponse userRegistrationResponse= (UserRegistrationResponse) response;

                    if(userRegistrationResponse.getResponsecode()!=null) {
                        if (userRegistrationResponse.getResponsecode().equalsIgnoreCase("200")) {
                            //Snackbar.make(mSignUp,userRegistrationResponse.getRegisterMessage()+"",Snackbar.LENGTH_LONG).show();

                            //updateSharedPreferenceData(userRegistrationResponse);

                            enteredPhoneNumber=mPhoneNumber.getText().toString();

                            callSendOTPAPI(enteredPhoneNumber);

                            showOTPLayout();

                        } else if (userRegistrationResponse.getResponsecode().equalsIgnoreCase("406")) {
                            if(userRegistrationResponse.getRegisterMessage()!=null)
                            {
                                Snackbar.make(mSignUp, userRegistrationResponse.getRegisterMessage() + "", Snackbar.LENGTH_LONG).show();
                            }

                        } else if (userRegistrationResponse.getResponsecode().equalsIgnoreCase("400")) {
                            if(userRegistrationResponse.getRegisterMessage()!=null)
                            {
                                Snackbar.make(mSignUp, userRegistrationResponse.getRegisterMessage() + "", Snackbar.LENGTH_LONG).show();
                            }
                        } else if (userRegistrationResponse.getResponsecode().equalsIgnoreCase("501")) {
                            if(userRegistrationResponse.getRegisterMessage()!=null)
                            {
                                Snackbar.make(mSignUp, userRegistrationResponse.getRegisterMessage() + "", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }



                }
                else {

                    //API call FAiled

                }
                break;

            case sendOTP:
                SendOTPResponse otpResponse= (SendOTPResponse) response;
                if(isSucces)
                {
                    if(mVerifyProgressBar!=null) {
                        if (mVerifyProgressBar.isShown()) {
                            mVerifyProgressBar.setVisibility(View.GONE);
                        }
                    }

                    if(otpResponse!=null) {
                        if (otpResponse.getResponsecode() != null) {
                            if (otpResponse.getResponsecode().equalsIgnoreCase("200")) {
                                //showOTPLayout();
                                if(otpResponse.getGetOtpMessage()!=null)
                                {
                                    Toast.makeText(this,otpResponse.getGetOtpMessage() , Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                if(otpResponse.getGetOtpMessage()!=null)
                                {
                                    Toast.makeText(this,otpResponse.getGetOtpMessage() , Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    }
                    else
                    {
                        //Toast.makeText(this, "null response", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    //API call Failed
                    if(otpResponse!=null) {

                        if (otpResponse.getGetOtpMessage() != null) {
                            Toast.makeText(this, otpResponse.getGetOtpMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;

            case verifyOTP:

                if(mVerifyProgressBar.isShown())
                {
                    toggleVisibility(mVerifyProgressBar);
                }
                VerifyOTPResponse verifyOTPResponse= (VerifyOTPResponse) response;
                if(isSucces)
                {

                    if(verifyOTPResponse!=null)
                    {
                        if(verifyOTPResponse.getResponsecode()!=null) {
                            if (verifyOTPResponse.getResponsecode().equalsIgnoreCase("200")) {
                                //startActivity(HomeActivity.class);
                                callUserLogInAPI();
                                if(verifyOTPResponse.getVerifyOtpMessage()!=null)
                                {
                                    Snackbar.make(mOTPLayoutVerify, verifyOTPResponse.getVerifyOtpMessage() + "", Snackbar.LENGTH_LONG).show();

                                }

                            } else {
                                if(verifyOTPResponse.getVerifyOtpMessage()!=null)
                                {
                                    Snackbar.make(mOTPLayoutVerify, verifyOTPResponse.getVerifyOtpMessage() + "", Snackbar.LENGTH_LONG).show();

                                }
                                }
                        }
                    }
                    else {
                       // Snackbar.make(mOTPLayoutVerify,"Null response",Snackbar.LENGTH_LONG).show();

                    }


                }
                else {
                    //API call failed
                    if(verifyOTPResponse!=null)
                    {
                        if(verifyOTPResponse.getVerifyOtpMessage()!=null)
                        {
                            Snackbar.make(mOTPLayoutVerify, verifyOTPResponse.getVerifyOtpMessage() + "", Snackbar.LENGTH_LONG).show();

                        }
                    }
                }
                break;

            case userlogin:

               /* if(mLoginProgressBar.isShown())
                {
                    toggleVisibility(mLoginProgressBar);
                }*/

                if(mVerifyProgressBar.isShown())
                {
                    toggleVisibility(mVerifyProgressBar);
                }
                LogInResponse logInResponse= (LogInResponse) response;

                if(isSucces)
                {

                    if(logInResponse!=null)
                    {
                        if(logInResponse.getResponsecode()!=null)
                        {
                            if(logInResponse.getResponsecode().equalsIgnoreCase("200"))
                            {
                                if(logInResponse.getLoginMessage()!=null)
                                {
                                    Snackbar.make(mOTPLayoutVerify,logInResponse.getLoginMessage(),Snackbar.LENGTH_LONG).show();

                                }

                                saveLogInSession(logInResponse);
                                mOTP.setText("");
                                /*startActivity(HomeActivity.class);*/
                                startActivity(CartActivity.class);

                            }
                            else if(logInResponse.getResponsecode().equalsIgnoreCase("501"))
                            {
                                if(logInResponse.getLoginMessage()!=null)
                                {
                                    Snackbar.make(mOTPLayoutVerify,logInResponse.getLoginMessage(),Snackbar.LENGTH_LONG).show();

                                }

                            }
                        }

                    }

                }
                else {
                    if(logInResponse!=null)
                    {
                        if(logInResponse.getLoginMessage()!=null)
                        {
                            Snackbar.make(mOTPLayoutVerify,logInResponse.getLoginMessage(),Snackbar.LENGTH_LONG).show();

                        }
                    }
                    //API call Failed
                }
                break;
        }

    }

    private void saveLogInSession(LogInResponse logInResponse) {

        SharedPreferences logInPreference=getSharedPreferences("LOGIN_PREFERENCE",MODE_PRIVATE);
        SharedPreferences.Editor editor=logInPreference.edit();

        editor.putBoolean("IS_LOGGEDIN", true);
        editor.putString("AUTH_KEY", logInResponse.getAuthKey());
        editor.putString("USER_ID", logInResponse.getId());
        editor.putString("EMAIL_ID", logInResponse.getEmail());
        editor.putString("USER_NAME", logInResponse.getFname());
        editor.putString("PHONE_NUMBER", logInResponse.getPhone());
        editor.putString("YOUR_REFERRAL_CODE",logInResponse.getReferralCode());
        editor.putString("APPLIED_REFERRAL_CODE",logInResponse.getAppliedReferralCode());
        editor.apply();
    }

    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }

    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        //LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();

        com.zikrabyte.organic.broadcastreceivers.SmsListener smsListener = new com.zikrabyte.organic.broadcastreceivers.SmsListener();
        try {
            unregisterReceiver(smsListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
        registerReceiver(smsListener, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));

        //LocalBroadcastManager.getInstance(this).registerReceiver(receiver);
    }


    @Override
    public void onPause() {
        super.onPause();
        com.zikrabyte.organic.broadcastreceivers.SmsListener smsListener = new com.zikrabyte.organic.broadcastreceivers.SmsListener();
        try {
            unregisterReceiver(smsListener);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onStop() {
        super.onStop();

        com.zikrabyte.organic.broadcastreceivers.SmsListener smsListener = new com.zikrabyte.organic.broadcastreceivers.SmsListener();
        try {
            unregisterReceiver(smsListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
       // LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }


}
