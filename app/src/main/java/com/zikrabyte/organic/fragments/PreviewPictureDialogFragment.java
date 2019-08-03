package com.zikrabyte.organic.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zikrabyte.organic.R;
import com.zikrabyte.organic.activities.ItemDescriptionActivity;
import com.zikrabyte.organic.adapters.PicturePreviewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreviewPictureDialogFragment extends DialogFragment implements View.OnClickListener {

    public static ImageView mCancel, mImageView;
    RecyclerView mRecyclerView;
    LinearLayoutManager layoutManager;
    String imageURL;
    View rootView;
    Context context;
    String mImageURL;

    List<Integer> imageList=new ArrayList<>();


    public PreviewPictureDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_preview_picture_dialog, container, false);
         mImageURL= getArguments().getString("imageurl");
         initializeViews();
        // setupWindowAnimations();
        return rootView;

    }

    private void initializeViews()
    {
        imageList=getImageList();
        //Bitmap bitmap = (Bitmap) getActivity().getIntent().getParcelableExtra("Bitmap");
        mImageView = (ImageView) rootView.findViewById(R.id.vI_app_imageview);

        Glide.with(this)
                .asBitmap()
                .load(imageURL)
                .into(mImageView);
        //mImageView.setImageBitmap(bitmap);

        mCancel = rootView.findViewById(R.id.vI_app_cancel);
        mCancel.setOnClickListener(this);

        mRecyclerView=rootView.findViewById(R.id.vR_app_recyclerview);
        layoutManager=new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        PicturePreviewAdapter adapter=new PicturePreviewAdapter(imageList,context);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vI_app_cancel:
               dismiss();
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
        getActivity().getWindow().setReturnTransition(returnTransition);

        getActivity().finishAfterTransition();
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),null);
        Intent intent = new Intent(getActivity(), ItemDescriptionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent,transitionActivityOptions.toBundle());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        goBack();
    }

    private void setupWindowAnimations() {
        Transition transition;

        transition = buildEnterTransition();

        //transition = TransitionInflater.from(this).inflateTransition(R.transition.slide_and_changebounds);
        //transition.setDuration(1000);

        getActivity().getWindow().setEnterTransition(transition);
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
