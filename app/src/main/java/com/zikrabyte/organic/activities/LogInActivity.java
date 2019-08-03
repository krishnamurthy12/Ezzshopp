package com.zikrabyte.organic.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zikrabyte.organic.R;
import com.zikrabyte.organic.api_requests.CheckUser;
import com.zikrabyte.organic.api_requests.ForgotPassword;
import com.zikrabyte.organic.api_requests.LogIn;
import com.zikrabyte.organic.api_requests.SendOTP;
import com.zikrabyte.organic.api_requests.VerifyOTP;
import com.zikrabyte.organic.api_responses.CheckUserResponse;
import com.zikrabyte.organic.api_responses.forgotpassword.ForgotPasswordResponse;
import com.zikrabyte.organic.api_responses.login.LogInResponse;
import com.zikrabyte.organic.api_responses.sendotp.SendOTPResponse;
import com.zikrabyte.organic.api_responses.verifyotp.VerifyOTPResponse;
import com.zikrabyte.organic.broadcastreceivers.SmsListener;
import com.zikrabyte.organic.utils.EzzShoppUtils;
import com.zikrabyte.organic.utils.MySMSBroadCastReceiver;
import com.zikrabyte.organic.utils.OnResponseListener;

import com.zikrabyte.organic.utils.WebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogInActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, OnResponseListener {

    EditText mPhoneNumber, mForgotPagePhoneNumber, mReceivedPassword, mNewPassword, mConfirmPassword;
    Button mProceed, mLogin, mGetPassword, mUpdatePassword;
    CheckBox mKeepMeLogIn;
    LinearLayout mMainLayout;
    ProgressBar mProgressBar, mLoginProgressBar, mForgotPasswordProgressBar, mConfirmPasswordProgressBar;
    TextView mLoginText, mForgotPassword, mSkip;
    EditText mPassword;
    ImageView mShowPassword, mHidePassword, mShowReceivedPassword, mHideReceivedPassword, mShowNewPassword, mHideNewPassword,
            mShowConfirmPassword, mHideConfirmPassword;

    FrameLayout mContainerLayout;

    Boolean keepMeLOGIN = false;

    String userChoice;
    boolean isValidInput = false;

    String receivedOTPMessage = null;

    TextView mOTPLayoutPhoneNumber, mOTPLayoutResendOTP;
    Button mOTPLayoutVerify;
    EditText mOTP;
    ProgressBar mVerifyProgressBar, mOTPLayoutProgressBar;

    String enteredPhoneNumber;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    //MySMSBroadCastReceiver receiver;

    String FROM = null;


    @Override
    protected void onStart() {
        super.onStart();

        if (checkAndRequestPermissions()) {

            SmsListener smsListener = new SmsListener();
            try {
                unregisterReceiver(smsListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
            registerReceiver(smsListener, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            FROM = bundle.getString("FROMINTENT", null);
        } else {
            FROM = "HOMEPAGE";
        }

        setupWindowAnimations();

        initializeViews();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        // Re-enter transition is executed when returning to this activity
        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.LEFT);
        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        getWindow().setReenterTransition(slideTransition);
        getWindow().setExitTransition(slideTransition);
    }

    private void initializeViews() {
        //receiver=new MySMSBroadCastReceiver();
        mMainLayout = findViewById(R.id.vL_main_layout);

        mProgressBar = findViewById(R.id.vP_al_progress_bar);
        mProgressBar.setVisibility(View.GONE);
        mContainerLayout = findViewById(R.id.container_layout);

        //mTermsAccept=findViewById(R.id.vC_al_terms_and_conditions);
        mPhoneNumber = findViewById(R.id.vE_al_phone_number);

        mLoginText = findViewById(R.id.vT_al_login_text);

        String text = "<font color=#909090>We will send SMS with </font>" +
                "<font color=#DE7E86>confirmation code </font>" +
                "<font color=#909090>to this number </font>.";
        mLoginText.setText(Html.fromHtml(text));


        mProceed = findViewById(R.id.vB_al_proceed);

        disableViews(mProceed);

        mPhoneNumber.addTextChangedListener(this);

        mProceed.setOnClickListener(this);
       /* mForgotPassword.setOnClickListener(this);*/


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.vB_al_proceed:
                callCheckUserAPI();
                break;

            case R.id.vB_pl_login:
                validatePasswordFieldAndCallLogInAPI();
                break;

            case R.id.vT_pl_forgot_password:
                showForgotPasswordPage();
                break;

            case R.id.vB_fpl_get_password:
                String enteredPhoneNumber = mForgotPagePhoneNumber.getText().toString();
                if (TextUtils.isEmpty(enteredPhoneNumber)) {
                    Snackbar.make(mGetPassword, R.string.err_phone_empty, Snackbar.LENGTH_LONG).show();
                } else if (!isValidPhoneNumber(enteredPhoneNumber)) {
                    Snackbar.make(mGetPassword, R.string.err_phone_empty, Snackbar.LENGTH_LONG).show();
                } else {
                    callForgotPasswordAPI(enteredPhoneNumber, "");
                }

                break;

            case R.id.vB_cpl_update:
                validatePasswordsAndCallAPI();
                break;

            case R.id.vT_cpl_skip:
               /* mMainLayout.setVisibility(View.VISIBLE);*/
                //startActivity(new Intent(this,LogInActivity.class));
                setContentView(R.layout.activity_log_in);
                initializeViews();
                break;

            case R.id.vB_ol_verify:
               /* if(validateOTP())
                {
                    verifyOTP();
                }*/
                validateOTP();
                break;

            case R.id.vT_ol_resend_otp:
                otpResend();
                break;

            case R.id.vI_pl_show_password:
                showPassword(mPassword, mShowPassword, mHidePassword);
                break;

            case R.id.vI_pl_hide_password:
                hidePassword(mPassword, mShowPassword, mHidePassword);
                break;

            case R.id.vI_cpl_show_received_password:
                showPassword(mReceivedPassword, mShowReceivedPassword, mHideReceivedPassword);
                break;

            case R.id.vI_cpl_hide_received_password:
                hidePassword(mReceivedPassword, mShowReceivedPassword, mHideReceivedPassword);
                break;

            case R.id.vI_cpl_show_new_password:
                showPassword(mNewPassword, mShowNewPassword, mHideNewPassword);
                break;

            case R.id.vI_cpl_hide_new_password:
                hidePassword(mNewPassword, mShowNewPassword, mHideNewPassword);
                break;

            case R.id.vI_cpl_show_confirm_password:
                showPassword(mConfirmPassword, mShowConfirmPassword, mHideConfirmPassword);
                break;

            case R.id.vI_cpl_hide_confirm_password:
                hidePassword(mConfirmPassword, mShowConfirmPassword, mHideConfirmPassword);
                break;


        }

    }

    private void showPassword(EditText view, View... views) {
        for (View v : views) {
            toggleVisibility(v);
            //toggleVisibility(v);
        }

        int start, end;


        start = view.getSelectionStart();
        end = view.getSelectionEnd();
        view.setTransformationMethod(null);
        view.setSelection(start, end);


    }

    private void hidePassword(EditText view, View... views) {
        for (View v : views) {
            toggleVisibility(v);
            //toggleVisibility(v);
        }
        //toggleVisibility(mShowPassword);
        //toggleVisibility(mHidePassword);
        int start, end;

        start = view.getSelectionStart();
        end = view.getSelectionEnd();
        view.setTransformationMethod(new PasswordTransformationMethod());
        ;
        view.setSelection(start, end);

    }

    private void otpResend() {
        //showOTPLayout();
        mVerifyProgressBar.setVisibility(View.VISIBLE);
        callSendOTPAPI(enteredPhoneNumber);

        /*String currentvalue=mOTPLayoutPhoneNumber.getText().toString();
        Log.d("currentphonenumber","phone number while resend=>"+currentvalue);

        //mOTPLayoutPhoneNumber.setText(enteredPhoneNumber);
        callSendOTPAPI(enteredPhoneNumber);

        Log.d("phonenumber","phone number while resend=>"+enteredPhoneNumber);
        //Toast.makeText(this, "Otp has been sent to above number", Toast.LENGTH_SHORT).show();*/
    }

    private void showOTPLayout() {
        hideKeyBoard();
        mMainLayout.setVisibility(View.GONE);

        View otpLayout = getLayoutInflater().inflate(R.layout.otp_layout, mContainerLayout, true);

        mOTPLayoutResendOTP = otpLayout.findViewById(R.id.vT_ol_resend_otp);
        mOTPLayoutVerify = otpLayout.findViewById(R.id.vB_ol_verify);
        mOTP = otpLayout.findViewById(R.id.vE_ol_otp);
        mOTPLayoutPhoneNumber = otpLayout.findViewById(R.id.vT_ol_phone_number);
        mVerifyProgressBar = otpLayout.findViewById(R.id.vP_ol_progress_bar);

        mOTPLayoutPhoneNumber.setText(enteredPhoneNumber);

        mOTPLayoutResendOTP.setPaintFlags(mOTPLayoutResendOTP.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //disableViews(mOTPLayoutVerify);

        mOTPLayoutVerify.setOnClickListener(this);
        mOTPLayoutResendOTP.setOnClickListener(this);


        BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null && intent.hasExtra("varificationCode")) {
                    String youtOTPcode = intent.getStringExtra("varificationCode");

                    Log.d("otpreceived", youtOTPcode);

                    if (youtOTPcode != null) {
                        mOTP.setText(youtOTPcode);

                    }
                }
            }
        };

        final String DISPLAY_MESSAGE_ACTION = this.getPackageName() + ".CodeSmsReceived";

        Log.d("otpreceived", DISPLAY_MESSAGE_ACTION);


        try {
            this.unregisterReceiver(mHandleMessageReceiver);
        } catch (Exception e) {
        }
        this.registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));
        //callSendOTPAPI(enteredPhoneNumber);

        /*mOTP.addTextChangedListener(new TextWatcher() {
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

       /* if (checkAndRequestPermissions()) {
            MySMSBroadCastReceiver.bindListener(new SmsListener() {
                @Override
                public void messageReceived(String messageText) {
                    Log.d("Text",messageText);
                    Toast.makeText(LogInActivity.this,"Message: "+messageText,Toast.LENGTH_LONG).show();

                    String code = parseCode(messageText);//Parse verification code
                    if(code!=null)
                    {
                        mOTP.setText(code);
                    }
                    else {
                        Toast.makeText(LogInActivity.this, "couldn't fetch OTP", Toast.LENGTH_SHORT).show();
                    }

                    Log.d("Text2",code);
                }
            });

        }*/

    }

    private void validateOTP() {

        if (mOTP.getText().toString().trim().isEmpty() || mOTP.getText().toString().trim().length() < 4) {
            Snackbar.make(mOTPLayoutVerify, "Invalid valid OTP", Snackbar.LENGTH_LONG).show();
            //disableViews(mOTPLayoutVerify);
            //return false;
        } else {
            //enableViews(mOTPLayoutVerify);
            callVerifyOTPAPI(mPhoneNumber.getText().toString(), mOTP.getText().toString());
            //return  true;

            //hideKeyBoard();
        }

    }

    private void verifyOTP() {

        //callVerifyOTPAPI(mOTPLayoutPhoneNumber.getText().toString(),mOTP.getText().toString());
        Log.d("phonenumber", "phone number while verify=>" + enteredPhoneNumber);
        mOTPLayoutPhoneNumber.setText(enteredPhoneNumber);
        callVerifyOTPAPI(enteredPhoneNumber, mOTP.getText().toString());

    }


    private void validatePasswordsAndCallAPI() {
        String receivedPassword = mReceivedPassword.getText().toString();
        String newPassword = mNewPassword.getText().toString();
        String confirmPassword = mConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(receivedPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Snackbar.make(mUpdatePassword, "Please enter a valid password", Snackbar.LENGTH_LONG).show();
        } else if (newPassword.length() < 8 || confirmPassword.length() < 8) {
            Snackbar.make(mUpdatePassword, "password length should be atleast 8 characters", Snackbar.LENGTH_LONG).show();
        } else if (!newPassword.equalsIgnoreCase(confirmPassword)) {
            Snackbar.make(mUpdatePassword, "password mismatch", Snackbar.LENGTH_LONG).show();
        } else if (!receivedPassword.equalsIgnoreCase(receivedOTPMessage)) {
            Snackbar.make(mUpdatePassword, "check the received password again", Snackbar.LENGTH_LONG).show();
        } else {
            callUpdatePasswordAPI(mPhoneNumber.getText().toString(), mConfirmPassword.getText().toString());
        }
    }

    private void showForgotPasswordPage() {
        hideKeyBoard();
        mMainLayout.setVisibility(View.GONE);
        // mSecondLayout.setVisibility(View.VISIBLE);
        View v = getLayoutInflater().inflate(R.layout.forgot_password_layout, mContainerLayout, true);
        mForgotPagePhoneNumber = v.findViewById(R.id.vE_fpl_phone_number);
        mGetPassword = v.findViewById(R.id.vB_fpl_get_password);
        mForgotPasswordProgressBar = v.findViewById(R.id.vP_fpl_progress_bar);

        mForgotPagePhoneNumber.setText(mPhoneNumber.getText().toString());

        mGetPassword.setOnClickListener(this);

    }

    private void showUpdatePasswordPage() {
        hideKeyBoard();
        mMainLayout.setVisibility(View.GONE);
        // mSecondLayout.setVisibility(View.VISIBLE);
        View v = getLayoutInflater().inflate(R.layout.confirm_password_layout, mContainerLayout, true);
        mSkip = v.findViewById(R.id.vT_cpl_skip);
        mReceivedPassword = v.findViewById(R.id.vE_cpl_received_password);
        mNewPassword = v.findViewById(R.id.vE_cpl_new_password);
        mConfirmPassword = v.findViewById(R.id.vE_cpl_confirm_password);
        mUpdatePassword = v.findViewById(R.id.vB_cpl_update);
        mConfirmPasswordProgressBar = v.findViewById(R.id.vP_cpl_progress_bar);

        mShowReceivedPassword = v.findViewById(R.id.vI_cpl_show_received_password);
        mHideReceivedPassword = v.findViewById(R.id.vI_cpl_hide_received_password);
        mShowNewPassword = v.findViewById(R.id.vI_cpl_show_new_password);
        mHideNewPassword = v.findViewById(R.id.vI_cpl_hide_new_password);
        mShowConfirmPassword = v.findViewById(R.id.vI_cpl_show_confirm_password);
        mHideConfirmPassword = v.findViewById(R.id.vI_cpl_hide_confirm_password);

        mSkip.setPaintFlags(mSkip.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mUpdatePassword.setOnClickListener(this);
        mSkip.setOnClickListener(this);

        mShowReceivedPassword.setOnClickListener(this);
        mHideReceivedPassword.setOnClickListener(this);
        mShowNewPassword.setOnClickListener(this);
        mHideNewPassword.setOnClickListener(this);
        mShowConfirmPassword.setOnClickListener(this);
        mHideConfirmPassword.setOnClickListener(this);


        Log.d("parametercheck", "received otp=>" + receivedOTPMessage);

    }

    private void validatePasswordFieldAndCallLogInAPI() {
        if (TextUtils.isEmpty(mPassword.getText().toString())) {
            // mPassword.setError(getResources().getString(R.string.err_msg_password));
            Snackbar.make(mLogin, getResources().getString(R.string.err_msg_password), Snackbar.LENGTH_LONG).show();
        }
        else if( mPassword.getText().toString().length() < 8)
        {
            Snackbar.make(mLogin,"Wrong password", Snackbar.LENGTH_LONG).show();
        }
        else {
            callUserLogInAPI();
        }

    }

    private void goToSignUpActivity() {
       /* ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this,null);
        hideKeyBoard();
        Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        startActivity(intent,transitionActivityOptions.toBundle());*/
        //overridePendingTransition(R.anim.slide_in,R.anim.slide_in);
        String userInput = mPhoneNumber.getText().toString();
        hideKeyBoard();
        Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
        if (userChoice.equals("email")) {
            intent.putExtra("userEmail", userInput);
        } else {
            intent.putExtra("userPhone", userInput);

        }
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        startActivity(intent);
    }


    private void showPasswordLayout() {
        hideKeyBoard();
        mMainLayout.setVisibility(View.GONE);
        // mSecondLayout.setVisibility(View.VISIBLE);
        View v = getLayoutInflater().inflate(R.layout.password_layout, mContainerLayout, true);
        mLogin = v.findViewById(R.id.vB_pl_login);
        mPassword = v.findViewById(R.id.vE_pl_password);
        mKeepMeLogIn = v.findViewById(R.id.vC_pl_keep_me_login);
        mLoginProgressBar = v.findViewById(R.id.vP_pl_progress_bar);
        mForgotPassword = v.findViewById(R.id.vT_pl_forgot_password);
        mShowPassword = v.findViewById(R.id.vI_pl_show_password);
        mHidePassword = v.findViewById(R.id.vI_pl_hide_password);

        TextView mPaCsswordText = v.findViewById(R.id.vT_asi_signintext);

        String text = "<font color=#909090>Just one step left to your </font>" +
                "<font color=#DE7E86>EzzShopp </font>";
        mPaCsswordText.setText(Html.fromHtml(text));

        mForgotPassword.setOnClickListener(this);
        mShowPassword.setOnClickListener(this);
        mHidePassword.setOnClickListener(this);

        mKeepMeLogIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    keepMeLOGIN = true;
                } else {
                    keepMeLOGIN = false;
                }

            }
        });

        mLogin.setOnClickListener(this);
        mPassword.addTextChangedListener(this);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (start < 1) {
            //mPhoneNumber.setInputType(InputType.TYPE_CLASS_TEXT);
            mPhoneNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(40)});
        }

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (mPhoneNumber.hasFocus()) {
           /* if(start<1)
            {
                mPhoneNumber.setInputType(InputType.TYPE_CLASS_TEXT);
                mPhoneNumber.setFilters( new InputFilter[] { new InputFilter.LengthFilter(25) } );
            }*/
            //validatePhoneNumber();

        }

    }


    @Override
    public void afterTextChanged(Editable s) {

        String text = mPhoneNumber.getText().toString();
        if (isEmail(text)) {
            //Toast.makeText(this, "you entered email", Toast.LENGTH_SHORT).show();
            userChoice = "email";
            if (!isValidEmail(text)) {

                disableViews(mProceed);
            } else {
                enableViews(mProceed);

            }
        }
        if (isPhone(text)) {
            //mPhoneNumber.setInputType(InputType.TYPE_CLASS_PHONE);
            mPhoneNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

            userChoice = "phoneNumber";

            if (!isValidPhoneNumber(text)) {
                disableViews(mProceed);
            } else {
                enableViews(mProceed);
            }
            // Toast.makeText(this, "you entered phone number", Toast.LENGTH_SHORT).show();
        }


    }


    private void validatePhoneNumber() {

        String number = mPhoneNumber.getText().toString().trim();
        if (number.isEmpty() || number.length() < 10) {
            disableViews(mProceed);
        } else {
            enableViews(mProceed);
            //hideKeyBoard();

        }
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private static boolean isValidPhoneNumber(String number) {

        /*String expression = "[6-9][0-9]{9}";
        Pattern p = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(number);
        return m.matches();*/
        return number.matches("[6-9][0-9]{9}"); //1st digit should be 6 to 9 then 9 digits any combination of 0-9
    }

    private void callCheckUserAPI() {
        CheckUser checkUser = new CheckUser(mPhoneNumber.getText().toString());
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            toggleVisibility(mProgressBar);

            WebServices<CheckUserResponse> webServices = new WebServices<CheckUserResponse>(LogInActivity.this);
            webServices.checkUser(WebServices.BASE_URL, WebServices.ApiType.checkUser, checkUser);
        } else {
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet) + "", Toast.LENGTH_SHORT).show();
        }
    }

    private void callUserLogInAPI() {
        LogIn logIn = new LogIn(mPhoneNumber.getText().toString(), mPassword.getText().toString());
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            toggleVisibility(mLoginProgressBar);

            toggleVisibility(mProgressBar);

            WebServices<LogInResponse> webServices = new WebServices<LogInResponse>(LogInActivity.this);
            webServices.userLogIn(WebServices.BASE_URL, WebServices.ApiType.userlogin, logIn);

        } else {
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet) + "", Toast.LENGTH_SHORT).show();
        }
    }

    private void callSendOTPAPI(String phone) {

        Log.d("whilesendingotp", phone);

        SendOTP sendOTP = new SendOTP(phone);

        //LogIn logIn=new LogIn(mPhoneNumber.getText().toString(),mPassword.getText().toString());
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            WebServices<SendOTPResponse> webServices = new WebServices<SendOTPResponse>(LogInActivity.this);
            webServices.sendOTP(WebServices.BASE_URL, WebServices.ApiType.sendOTP, sendOTP);

        } else {
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet) + "", Toast.LENGTH_SHORT).show();
        }
    }

    private void callVerifyOTPAPI(String phone, String otp) {

        VerifyOTP verifyOTP = new VerifyOTP(phone, otp);

        //LogIn logIn=new LogIn(mPhoneNumber.getText().toString(),mPassword.getText().toString());
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            WebServices<VerifyOTPResponse> webServices = new WebServices<VerifyOTPResponse>(LogInActivity.this);
            webServices.verifyOTP(WebServices.BASE_URL, WebServices.ApiType.verifyOTP, verifyOTP);

        } else {

            toggleVisibility(mVerifyProgressBar);
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet) + "", Toast.LENGTH_SHORT).show();
        }
    }

    private void callForgotPasswordAPI(String phone, String password) {
        ForgotPassword forgotPassword = new ForgotPassword(phone, password);
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {

            toggleVisibility(mForgotPasswordProgressBar);

            //toggleVisibility(mProgressBar);

            WebServices<ForgotPasswordResponse> webServices = new WebServices<ForgotPasswordResponse>(LogInActivity.this);
            webServices.forgotPassword(WebServices.BASE_URL, WebServices.ApiType.forgotPassword, forgotPassword);
        } else {
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet) + "", Toast.LENGTH_SHORT).show();
        }
    }

    private void callUpdatePasswordAPI(String phone, String password) {
        ForgotPassword forgotPassword = new ForgotPassword(phone, password);
        if (EzzShoppUtils.isConnectedToInternet(getApplicationContext())) {
            toggleVisibility(mConfirmPasswordProgressBar);

            //toggleVisibility(mProgressBar);

            WebServices<ForgotPasswordResponse> webServices = new WebServices<ForgotPasswordResponse>(LogInActivity.this);
            webServices.forgotPassword(WebServices.BASE_URL, WebServices.ApiType.updatePassword, forgotPassword);
        } else {
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            Toast.makeText(this, getResources().getString(R.string.err_msg_nointernet) + "", Toast.LENGTH_SHORT).show();
        }
    }


    public static boolean isEmail(String text) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern p = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        return m.matches();
    }

    public static boolean isPhone(String text) {
        if (!TextUtils.isEmpty(text)) {
            return TextUtils.isDigitsOnly(text);
        } else {
            return false;
        }
    }

    private void hideKeyBoard() {
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

    private void toggleVisibility(View... views) {
        for (View v : views) {
            if (v.getVisibility() == View.VISIBLE) {
                v.setVisibility(View.GONE);
            } else {
                v.setVisibility(View.VISIBLE);
            }
        }
    }

    private void goToHomeActivity() {
        hideKeyBoard();
        if (FROM.equalsIgnoreCase("CARTACTIVITY")) {
            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, null);
            Intent intent = new Intent(LogInActivity.this, CartActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
            startActivity(intent, transitionActivityOptions.toBundle());

        } else {

            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, null);
            Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent, transitionActivityOptions.toBundle());
            //overridePendingTransition(R.anim.slide_in,R.anim.slide_in);
        }
    }


    @Override
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {

        switch (URL) {
            case checkUser:
                if (mProgressBar.isShown()) {
                    toggleVisibility(mProgressBar);
                }

                if (isSucces) {
                    enteredPhoneNumber = mPhoneNumber.getText().toString();
                    CheckUserResponse checkUserResponse = (CheckUserResponse) response;

                    if (checkUserResponse != null) {
                        if (checkUserResponse.getResponsecode() != null) {
                            if (checkUserResponse.getResponsecode().equalsIgnoreCase("200")) {
                                showPasswordLayout();

                            }
                       /* else if(checkUserResponse.getResponsecode().equalsIgnoreCase("501"))*/
                            else {
                                goToSignUpActivity();

                            }
                        }
                    }

                } else {
                    //API call Failed
                }
                break;

            case userlogin:

                if (mLoginProgressBar.isShown()) {
                    toggleVisibility(mLoginProgressBar);
                }

                if (mProgressBar.isShown()) {
                    toggleVisibility(mProgressBar);
                }

                if (isSucces) {
                    LogInResponse logInResponse = (LogInResponse) response;

                    if (logInResponse != null) {
                        if (logInResponse.getResponsecode() != null) {
                            if (logInResponse.getResponsecode().equalsIgnoreCase("200")) {

                                saveLogInSession(logInResponse);

                                mPhoneNumber.setText("");
                                mPassword.setText("");

                                //goToHomeActivity();

                            } else if (logInResponse.getResponsecode().equalsIgnoreCase("406")) {
                                if (logInResponse.getLoginMessage() != null) {
                                    Snackbar.make(mLogin, logInResponse.getLoginMessage(), Snackbar.LENGTH_LONG).show();
                                }

                                showOTPLayout();
                                callSendOTPAPI(enteredPhoneNumber);
                                // showOTPLayout();


                            } else {
                                if (logInResponse.getLoginMessage() != null) {
                                    Snackbar.make(mLogin, logInResponse.getLoginMessage(), Snackbar.LENGTH_LONG).show();
                                }

                            }
                        }
                    }

                } else {
                    //API call Failed
                }
                break;

            case forgotPassword:
                ForgotPasswordResponse forgotPasswordResponse = (ForgotPasswordResponse) response;
                if (mForgotPasswordProgressBar.isShown()) {
                    toggleVisibility(mForgotPasswordProgressBar);
                }


                if (isSucces) {

                    if (forgotPasswordResponse != null) {
                        if (forgotPasswordResponse.getResponsecode() != null) {
                            if (forgotPasswordResponse.getResponsecode().equalsIgnoreCase("200")) {

                                receivedOTPMessage = forgotPasswordResponse.getFotp().toString();
                                Log.d("parametercheck", forgotPasswordResponse.getFotp().toString());
                                Log.d("parametercheck", forgotPasswordResponse.getForgotPasswordMessage());
                                if (forgotPasswordResponse.getForgotPasswordMessage() != null) {
                                    Snackbar.make(mGetPassword, forgotPasswordResponse.getForgotPasswordMessage(), Snackbar.LENGTH_LONG).show();
                                }

                                //goToHomeActivity();
                                showUpdatePasswordPage();

                            } else if (forgotPasswordResponse.getResponsecode().equalsIgnoreCase("501")) {
                                if (forgotPasswordResponse.getCheckMessage() != null) {
                                    Snackbar.make(mGetPassword, forgotPasswordResponse.getCheckMessage(), Snackbar.LENGTH_LONG).show();
                                }
                                goToSignUpActivity();
                            } else {
                                if (forgotPasswordResponse.getForgotPasswordMessage() != null) {
                                    Snackbar.make(mLogin, forgotPasswordResponse.getForgotPasswordMessage(), Snackbar.LENGTH_LONG).show();
                                }


                            }
                        }
                    }

                } else {
                    //API call Failed
                }
                break;

            case updatePassword:
                ForgotPasswordResponse forgotPasswordResponse1 = (ForgotPasswordResponse) response;
                if (mConfirmPasswordProgressBar.isShown()) {
                    toggleVisibility(mConfirmPasswordProgressBar);

                }


                if (isSucces) {

                    if (forgotPasswordResponse1 != null) {
                        if (forgotPasswordResponse1.getResponsecode() != null) {
                            if (forgotPasswordResponse1.getResponsecode().equalsIgnoreCase("200")) {

                                if (forgotPasswordResponse1.getUpdatePasswordMessage() != null) {
                                    Snackbar.make(mUpdatePassword, forgotPasswordResponse1.getUpdatePasswordMessage(), Snackbar.LENGTH_LONG).show();
                                }

                                //goToHomeActivity();
                                //showUpdatePasswordPage();
                                //startActivity(new Intent(this,LogInActivity.class));

                                setContentView(R.layout.activity_log_in);
                                initializeViews();

                            } else {
                                if (forgotPasswordResponse1.getForgotPasswordMessage() != null) {
                                    Snackbar.make(mLogin, forgotPasswordResponse1.getForgotPasswordMessage(), Snackbar.LENGTH_LONG).show();
                                }


                            }
                        }
                    }

                } else {
                    //API call Failed
                }
                break;

            case sendOTP:
                if (mVerifyProgressBar != null) {
                    if (mVerifyProgressBar.isShown()) {
                        mVerifyProgressBar.setVisibility(View.GONE);
                    }
                }
                SendOTPResponse otpResponse = (SendOTPResponse) response;
                if (isSucces) {

                    if (otpResponse != null) {
                        if (otpResponse.getResponsecode() != null) {
                            if (otpResponse.getResponsecode().equalsIgnoreCase("200")) {
//                                showOTPLayout();
                                if (otpResponse.getGetOtpMessage() != null) {
                                    Toast.makeText(this, otpResponse.getGetOtpMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                //showOTPLayout();
                                if (otpResponse.getGetOtpMessage() != null) {
                                    Toast.makeText(this, otpResponse.getGetOtpMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    } else {
                        //Toast.makeText(this, "null response", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    //API call Failed
                    if (otpResponse != null) {

                        if (otpResponse.getGetOtpMessage() != null) {
                            Toast.makeText(this, otpResponse.getGetOtpMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;

            case verifyOTP:

                if (mVerifyProgressBar.isShown()) {
                    toggleVisibility(mVerifyProgressBar);
                }
                VerifyOTPResponse verifyOTPResponse = (VerifyOTPResponse) response;
                if (isSucces) {

                    if (verifyOTPResponse != null) {
                        if (verifyOTPResponse.getResponsecode() != null) {
                            if (verifyOTPResponse.getResponsecode().equalsIgnoreCase("200")) {
                                //startActivity(HomeActivity.class);
                                callUserLogInAPI();
                                if (verifyOTPResponse.getVerifyOtpMessage() != null) {
                                    Snackbar.make(mOTPLayoutVerify, verifyOTPResponse.getVerifyOtpMessage() + "", Snackbar.LENGTH_LONG).show();

                                }

                            } else {
                                if (verifyOTPResponse.getVerifyOtpMessage() != null) {
                                    Snackbar.make(mOTPLayoutVerify, verifyOTPResponse.getVerifyOtpMessage() + "", Snackbar.LENGTH_LONG).show();

                                }
                            }
                        }
                    } else {
                        // Snackbar.make(mOTPLayoutVerify,"Null response",Snackbar.LENGTH_LONG).show();

                    }


                } else {
                    //API call failed
                    if (verifyOTPResponse != null) {
                        if (verifyOTPResponse.getVerifyOtpMessage() != null) {
                            Snackbar.make(mOTPLayoutVerify, verifyOTPResponse.getVerifyOtpMessage() + "", Snackbar.LENGTH_LONG).show();

                        }
                    }
                }
                break;
        }

    }

    private void saveLogInSession(LogInResponse logInResponse) {

        SharedPreferences logInPreference = getSharedPreferences("LOGIN_PREFERENCE", MODE_PRIVATE);
        SharedPreferences.Editor editor = logInPreference.edit();
        if (keepMeLOGIN) {
            editor.putBoolean("IS_LOGGEDIN", true);
        } else {
            editor.putBoolean("IS_LOGGEDIN", true);
        }

        editor.putString("AUTH_KEY", logInResponse.getAuthKey());
        editor.putString("USER_ID", logInResponse.getId());
        editor.putString("EMAIL_ID", logInResponse.getEmail());
        editor.putString("USER_NAME", logInResponse.getFname());
        editor.putString("PHONE_NUMBER", logInResponse.getPhone());
        editor.putString("YOUR_REFERRAL_CODE", logInResponse.getReferralCode());
        editor.putString("APPLIED_REFERRAL_CODE", logInResponse.getAppliedReferralCode());
        editor.apply();

        goToHomeActivity();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    /* private void saveToLogInPreference(LogInResponse logInResponse) {

        SharedPreferences logInPreference=getSharedPreferences("LOGIN_PREFERENCE",MODE_PRIVATE);
        SharedPreferences.Editor editor=logInPreference.edit();
        editor.putString("AUTH_KEY",logInResponse.getAuthKey());
        editor.putString("USER_ID",logInResponse.getId());
        editor.putString("EMAIL_ID",logInResponse.getEmail());
        editor.putString("USER_NAME",logInResponse.getFname());
        editor.putString("PHONE_NUMBER",logInResponse.getPhone());
        editor.apply();

    }*/

    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        int readPhoneState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (readPhoneState != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            if ((permissions[0].equals(Manifest.permission.RECEIVE_SMS)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) &&
                    (permissions[1].equals(Manifest.permission.READ_SMS)
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED) &&
                    (permissions[2].equals(Manifest.permission.SEND_SMS)
                            && grantResults[2] == PackageManager.PERMISSION_GRANTED) &&
                    (permissions[3].equals(Manifest.permission.READ_PHONE_STATE)
                            && grantResults[3] == PackageManager.PERMISSION_GRANTED)) {
                // selectImage();

                SmsListener smsListener = new SmsListener();
                try {
                    unregisterReceiver(smsListener);
                } catch (Exception e) {
                }
                registerReceiver(smsListener, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));

            }

        } else {


            // permission denied, boo! Disable the
            // functionality that depends on this permission.

            Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onResume() {
        //LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
        SmsListener smsListener = new SmsListener();
        try {
            unregisterReceiver(smsListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
        registerReceiver(smsListener, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }


    @Override
    public void onPause() {
        super.onPause();
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        SmsListener smsListener = new SmsListener();
        try {
            unregisterReceiver(smsListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);

        SmsListener smsListener = new SmsListener();
        try {
            unregisterReceiver(smsListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
