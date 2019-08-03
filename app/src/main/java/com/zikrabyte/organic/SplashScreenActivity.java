package com.zikrabyte.organic;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zikrabyte.organic.activities.HomeActivity;
import com.zikrabyte.organic.activities.LogInActivity;
import com.zikrabyte.organic.activities.WalkThroughActivity;

public class SplashScreenActivity extends AppCompatActivity {

    int SPLASH_DISPLAY_LENGTH = 1500;
    Animation upToDown;
    ImageView logo;
    LinearLayout rootLayout;
    boolean isLoggedIn=false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

            setupWindowAnimations();
            init();

        //to remove "information bar" above the action bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        gotoNextPage();
    }
    private void init()
    {
        SharedPreferences preferences = getSharedPreferences("LOGIN_PREFERENCE", MODE_PRIVATE);
        isLoggedIn= preferences.getBoolean("IS_LOGGEDIN",false);
        if(!isLoggedIn)
        {
            SharedPreferences logInPreference = getSharedPreferences("LOGIN_PREFERENCE", MODE_PRIVATE);
            SharedPreferences.Editor editor = logInPreference.edit();
            editor.putString("AUTH_KEY", "1c5a4b78b14a45b2f19a7dc08b312b9D");
            editor.putString("USER_ID", "0");
            editor.putBoolean("IS_LOGGEDIN", false);
            editor.putString("EMAIL_ID", null);
            editor.putString("USER_NAME", null);
            editor.putString("PHONE_NUMBER",null);
            editor.apply();

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Transition transition;
        //transition = TransitionInflater.from(this).inflateTransition(R.transition.explode);
        transition=new Explode();
        transition.setDuration(1000);

        getWindow().setEnterTransition(transition);
        //getWindow().setExitTransition(transition);

    }

    private void gotoNextPage() {
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                try {

                    finishAfterTransition();
                    ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashScreenActivity.this,null);
                    Intent mainIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    //mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent,transitionActivityOptions.toBundle());
                   /* if(isLoggedIn)
                    {
                        finishAfterTransition();
                        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashScreenActivity.this,null);
                        Intent mainIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent,transitionActivityOptions.toBundle());

                    }
                    else
                    {
                        finishAfterTransition();
                        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashScreenActivity.this,null);
                        Intent mainIntent = new Intent(SplashScreenActivity.this, LogInActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent,transitionActivityOptions.toBundle());
                    }*/

                }catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        },SPLASH_DISPLAY_LENGTH);
    }
}
