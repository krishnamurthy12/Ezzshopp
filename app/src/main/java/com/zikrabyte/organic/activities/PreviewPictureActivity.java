package com.zikrabyte.organic.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zikrabyte.organic.R;
import com.zikrabyte.organic.adapters.HomePageAdapter;
import com.zikrabyte.organic.adapters.PicturePreviewAdapter;

import java.util.ArrayList;
import java.util.List;

public class PreviewPictureActivity extends AppCompatActivity implements View.OnClickListener {

    public static ImageView mCancel, mImageView;
    RecyclerView mRecyclerView;
    LinearLayoutManager layoutManager;
    String imageURL;

    List<Integer> imageList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_picture);
        setupWindowAnimations();
        initializeViews();
    }
    private void initializeViews()
    {
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            imageURL=bundle.getString("imageURL",null);
           /* Glide.with(this)
                    .asBitmap()
                    .load(imageURL)
                    .into(mImageView);*/
        }
        imageList=getImageList();
        Bitmap bitmap = (Bitmap) this.getIntent().getParcelableExtra("Bitmap");
        mImageView = (ImageView) findViewById(R.id.vI_app_imageview);

        Glide.with(this)
                .asBitmap()
                .load(imageURL)
                .into(mImageView);
        //mImageView.setImageBitmap(bitmap);

        mCancel = findViewById(R.id.vI_app_cancel);
        mCancel.setOnClickListener(this);

        mRecyclerView=findViewById(R.id.vR_app_recyclerview);
        layoutManager=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        PicturePreviewAdapter adapter=new PicturePreviewAdapter(imageList,this);
        mRecyclerView.setAdapter(adapter);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vI_app_cancel:
                goBack();
                break;
        }

    }
    private List<Integer> getImageList() {

        for(int i=0;i<=5;i++)
        {
            imageList.add(R.drawable.bag);
        }
        return imageList;
    }

    private void goBack() {
        Transition returnTransition=buildReturnTransition();
        getWindow().setReturnTransition(returnTransition);

        finishAfterTransition();
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this,null);
        Intent intent = new Intent(this, ItemDescriptionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent,transitionActivityOptions.toBundle());

    }
    @Override
    public void onBackPressed() {
        goBack();
    }


    private void setupWindowAnimations() {
        Transition transition;

            transition = buildEnterTransition();

            //transition = TransitionInflater.from(this).inflateTransition(R.transition.slide_and_changebounds);
            //transition.setDuration(1000);

        getWindow().setEnterTransition(transition);
    }

    private Visibility buildEnterTransition() {
        Visibility retutnTransition = new Explode();
        retutnTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        return retutnTransition;
    }
    private Visibility buildReturnTransition() {
        Visibility retutnTransition = new Slide();
        retutnTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        return retutnTransition;
    }

}

