package com.zikrabyte.organic.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;
import com.zikrabyte.organic.R;
import com.zikrabyte.organic.beanclasses.ImageItemViewpager;
import com.zikrabyte.organic.adapters.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WalkThroughActivity extends AppCompatActivity implements View.OnClickListener{

    CirclePageIndicator oCirclePageIndicator;
    Button mSignUp,mSignIn;
    ViewPager mViewPager;

    List<ImageItemViewpager> list_for_viewpager;
    ViewPagerAdapter adapterForViewPager;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 2000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_through);
        initializeViews();
        setValue();
    }

    private void initializeViews()
    {
        list_for_viewpager=getImageResource();

        oCirclePageIndicator=findViewById(R.id.circle_indicator_id);
        mViewPager=findViewById(R.id.viewpager);
        mSignIn=findViewById(R.id.vB_wa_signin);
        mSignUp=findViewById(R.id.vB_wa_signup);

        mSignUp.setOnClickListener(this);
        mSignIn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.vB_wa_signin)
        {
            startActivity(new Intent(this,SignInActivity.class));
            overridePendingTransition(R.anim.slide_in,R.anim.slide_in);

        }
        else if (v.getId()==R.id.vB_wa_signup)
        {
            startActivity(new Intent(this,SignUpActivity.class));
            overridePendingTransition(R.anim.slide_in,R.anim.slide_in);
        }

    }

    public void setValue() {
        adapterForViewPager = new ViewPagerAdapter(this, list_for_viewpager);
        oCirclePageIndicator.setSnap(true);
        oCirclePageIndicator.setFillColor(getResources().getColor(R.color.colorPrimary));
        oCirclePageIndicator.setPageColor(getResources().getColor(R.color.gray));
        oCirclePageIndicator.setRadius(15);
        mViewPager.setAdapter(adapterForViewPager);
        oCirclePageIndicator.setViewPager(mViewPager);
        adapterForViewPager.notifyDataSetChanged();
        mViewPager.getAdapter().notifyDataSetChanged();

        // use this oly after setting adapter to the view pager ,code to make view pager items scollable begins//
        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable update=new Runnable() {
            @Override
            public void run() {
                if(currentPage>3){
                    currentPage=0;
                }
                mViewPager.setCurrentItem(currentPage++,true);
            }
        };
        timer = new Timer(); // This will create a new Thread
        timer .schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_MS, PERIOD_MS);
//code to make view pager items scrollable ends//

    }

    private List<ImageItemViewpager> getImageResource() {
        List<ImageItemViewpager> list = new ArrayList<ImageItemViewpager>();
        list.add(new ImageItemViewpager(R.drawable.ic_shopping_cart,"Heading","sub heading","Description"));
        list.add(new ImageItemViewpager(R.drawable.ic_shopping_cart,"Heading","sub heading","Description"));
        list.add(new ImageItemViewpager(R.drawable.ic_shopping_cart,"Heading","sub heading","Description"));
        list.add(new ImageItemViewpager(R.drawable.ic_shopping_cart,"Heading","sub heading","Description"));

        return list;
    }
}
