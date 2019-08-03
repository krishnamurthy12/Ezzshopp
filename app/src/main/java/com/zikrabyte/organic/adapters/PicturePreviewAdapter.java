package com.zikrabyte.organic.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zikrabyte.organic.R;
import com.zikrabyte.organic.activities.PreviewPictureActivity;
import java.util.List;

/**
 * Created by Krish on 05-03-2018.
 */

public class PicturePreviewAdapter extends RecyclerView.Adapter<PicturePreviewAdapter.MyHolder> {

    List<Integer> imagesList;
    int row_index=-1;
    Context context;

    public PicturePreviewAdapter(List<Integer> imageList,Context context) {
        this.imagesList = imageList;
        this.context=context;
    }

    @Override
    public PicturePreviewAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_preview_item, null, false);
        return new PicturePreviewAdapter.MyHolder(v);
    }

    @Override
    public void onBindViewHolder(PicturePreviewAdapter.MyHolder holder, final int position) {

        holder.recycleViewImage.setImageResource(imagesList.get(position));

        holder.recycleViewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=position;
                notifyDataSetChanged();
            }
        });
        if(row_index==position){
            holder.recycleViewImage.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            PreviewPictureActivity.mImageView.setImageResource(imagesList.get(row_index));

        }
        else
        {
            holder.recycleViewImage.setBackgroundColor(Color.parseColor("#ffffff"));

        }




    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView recycleViewImage;
        public MyHolder(View itemView) {
            super(itemView);

            recycleViewImage=itemView.findViewById(R.id.vI_ppi_image);
            recycleViewImage.setOnClickListener(PicturePreviewAdapter.MyHolder.this);
        }

        @Override
        public void onClick(View v) {

            if(v.getId()==R.id.vI_ppi_image)
            {
                //v.setBackgroundColor(Color.parseColor("#000000"));
                //PreviewPictureActivity.mImageView.setImageResource(imagesList.get(getAdapterPosition()));
            }

        }
    }
}