package com.zikrabyte.organic.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.zikrabyte.organic.R;
import com.zikrabyte.organic.api_responses.searchresults.Response;
import com.zikrabyte.organic.activities.ItemDescriptionActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krish on 30-12-2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyHolder> /*implements Filterable*/ {

    Context context;
    List<Response> mList=new ArrayList<>();
    /*List<Response> mFilterList=new ArrayList<>();
    List<Response> mArrayList=new ArrayList<>();
    LinearLayout mainLayout;
*/


    public SearchAdapter(Context context, List<Response> list, LinearLayout mainLayout) {
        this.context = context;
        this.mList = list;
        /*this.mFilterList.addAll(list);
        mArrayList.addAll(list);
        this.mainLayout=mainLayout;*/

    }
   /* @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().trim().toLowerCase();

                if (charString.isEmpty()) {
                    mFilterList = mArrayList;
                }
                else {
                    ArrayList<ItemDescription> filteredList = new ArrayList<>();

                    for (ItemDescription datum : mArrayList) {

                        if (datum.getName().toLowerCase().contains(charString)) {
                            filteredList.add(datum);

                        }

                    }
                   *//* if(filteredList.isEmpty())
                    {
                        //mainLayout.addView(LayoutInflater.from(context).inflate(R.layout.searchfailview,null));
                        LayoutInflater.from(context).inflate(R.layout.searchfailview,mainLayout,false);
                        Log.d("listemptycheck","no match found in filtered list");
                    }*//*
                    mFilterList = filteredList;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mList= (List<ItemDescription>) results.values;
               *//* HashSet hs=new HashSet();
                hs.addAll(mList);
                mList.clear();
                mList.addAll(hs);
                hs.clear();*//*
                if(mList.isEmpty())
                {
                    LayoutInflater.from(context).inflate(R.layout.searchfailview,mainLayout,false);
                }
                else {
                    LayoutInflater.from(context).inflate(R.layout.search_singlerow_appearence,mainLayout,false);
                }
                notifyDataSetChanged();

            }
        };
    }*/

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int i) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_singlerow_appearence,parent,false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        //holder.image.setImageResource(mList.get(position).getPicture());

        if(mList.get(position).getPicture()!=null || !mList.get(position).getPicture().isEmpty()) {
            Picasso.get()
                    .load(mList.get(position).getPicture())
                    .placeholder(R.drawable.background_drawable)
                    .error(R.drawable.logo)
                    .into(holder.image);
        }
        holder.title.setText(mList.get(position).getTitle());


    }

    @Override
    public int getItemCount() {
        return mList.size();
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
        TextView title;
        ImageView image;

        public MyHolder(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.vT_ssa_item_name);
            image=itemView.findViewById(R.id.vI_ssa_item_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();

            String image= mList.get(position).getPicture();
            String title=mList.get(position).getTitle();
            String description=mList.get(position).getDescription();
            String price=mList.get(position).getNewPrice();
            String discount= String.valueOf(mList.get(position).getDiscountPrice());
            float rating=Float.parseFloat(mList.get(position).getRating());

            String productId = mList.get(position).getId();
            String categoryId = mList.get(position).getCategoryId();

            Intent itemDescription=new Intent(context, ItemDescriptionActivity.class);
            itemDescription.putExtra("title",title);
            itemDescription.putExtra("description",description);
            itemDescription.putExtra("price",price);
            itemDescription.putExtra("discount",discount);
            itemDescription.putExtra("rating",rating);
            itemDescription.putExtra("image",image);

            itemDescription.putExtra("categoryId", categoryId);
            itemDescription.putExtra("productId", productId);
            context.startActivity(itemDescription);

        }

    }
}
