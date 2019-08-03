package com.zikrabyte.organic.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.zikrabyte.organic.R;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class SignInActivity extends AppCompatActivity implements OnClickListener {

    // UI references.
    private EditText mUserNameView,mPasswordView;
    private Button mSignIn;
    private TextView mSignupFromSignInText,mForgotPassword,mSignInText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initializeViews();

    }
    private void initializeViews()
    {
        mUserNameView=findViewById(R.id.vE_asi_username);
        mPasswordView=findViewById(R.id.vE_asi_password);
        mSignIn=findViewById(R.id.vB_asi_signin);

        mSignupFromSignInText=findViewById(R.id.vT_asi_signup);
        mForgotPassword=findViewById(R.id.vT_asi_forgotpassword);
        mSignInText=findViewById(R.id.vT_asi_signintext);

        String text = "<font color=#909090>Just One Step </font>" +
                "<font color=#DE7E86>Left </font>"+
                "<font color=#909090> Lorem ipsum</font>.";
        mSignInText.setText(Html.fromHtml(text));

        mSignIn.setOnClickListener(this);
        mSignupFromSignInText.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.vB_asi_signin:
                startActivity(HomeActivity.class);
                break;

            case R.id.vT_asi_forgotpassword:
                break;

            case R.id.vT_asi_signup:
                startActivity(SignUpActivity.class);
                break;
        }

    }

    private void startActivity(Class<?> tClass)
    {
        startActivity(new Intent(SignInActivity.this,tClass));
        overridePendingTransition(R.anim.slide_in,R.anim.slide_in);
        //this.finish();
    }
}

