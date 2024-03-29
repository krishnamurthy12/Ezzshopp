package com.zikrabyte.organic.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zikrabyte.organic.R;


public class ViewPictureFragment extends Fragment {
    ImageView mPreviewImage,mCancel;
    RecyclerView mRecyclerView;
    int mImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImage = getArguments().getInt("image");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_view_picture, container, false);
        mPreviewImage=rootView.findViewById(R.id.vI_fvp_imageview);
        mCancel=rootView.findViewById(R.id.vI_fvp_cancel);

        mPreviewImage.setImageResource(mImage);

        return rootView;
    }

}
