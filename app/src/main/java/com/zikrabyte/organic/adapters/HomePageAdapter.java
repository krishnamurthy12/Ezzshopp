package com.zikrabyte.organic.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.zikrabyte.organic.R;
import com.zikrabyte.organic.activities.ItemDescriptionActivity;
import com.zikrabyte.organic.beanclasses.ItemDescription;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krish on 27-02-2018.
 */

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.MyHolder> {
    Context context;
    List<ItemDescription> mList=new ArrayList<>();
    private int lastPosition = -1;

    public HomePageAdapter(Context context, List<ItemDescription> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.homeactivity_singleitem_appearence, null, false);
        return new HomePageAdapter.MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        holder.itemImage.setImageResource(mList.get(position).getImage());
        holder.itemName.setText(mList.get(position).getName());
        holder.itemQuantityType.setText(mList.get(position).getQuantityType());
        holder.itemRating.setRating(Float.parseFloat(mList.get(position).getRating()));
        holder.itemCost.setText("Rs "+mList.get(position).getPrice());
        holder.itemDiscount.setText(mList.get(position).getDiscount()+" % off");

        // Here you apply the animation when the view is bound
        setAnimation(holder.itemView, position);


    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
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

        LinearLayout numberofItemsLayout,addToCartLayout,mainLayout;
        TextView itemName,itemQuantityType,itemCost,itemDiscount,itemCount,addToCart;
        ImageView itemImage,plusSign,minusSign;
        RatingBar itemRating;
        public MyHolder(View itemView) {
            super(itemView);
            mainLayout=itemView.findViewById(R.id.card_main_layout);

            numberofItemsLayout=itemView.findViewById(R.id.number_of_items_layout);
            addToCartLayout=itemView.findViewById(R.id.addto_cart_button_layout);

            itemName=itemView.findViewById(R.id.vT_item_name);
            itemQuantityType=itemView.findViewById(R.id.vT_item_quantity_type);
            itemCost=itemView.findViewById(R.id.vT_item_new_price);
            itemDiscount=itemView.findViewById(R.id.vT_item_discount);

            itemImage=itemView.findViewById(R.id.vI_item_image);
            itemRating=itemView.findViewById(R.id.vR_item_rating);

            addToCart=itemView.findViewById(R.id.vB_button_addto_cart);
            itemCount=itemView.findViewById(R.id.vT_item_count);
            plusSign=itemView.findViewById(R.id.vI_plus);
            minusSign=itemView.findViewById(R.id.vI_minus);

            plusSign.setOnClickListener(this);
            minusSign.setOnClickListener(this);
            addToCart.setOnClickListener(this);

            mainLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int number=Integer.parseInt(itemCount.getText().toString());

            switch (v.getId())
            {
                case R.id.card_main_layout:
                    goToItemDescriptionActivity();
                    break;
                case R.id.vB_button_addto_cart:
                    numberofItemsLayout.setVisibility(View.VISIBLE);
                    addToCartLayout.setVisibility(View.GONE);
                    break;

                case R.id.vI_minus:

                    if(number>1)
                    {
                        number--;
                        itemCount.setText(String.valueOf(number));

                    }
                    else {
                        numberofItemsLayout.setVisibility(View.GONE);
                        addToCartLayout.setVisibility(View.VISIBLE);
                    }
                    break;

                case R.id.vI_plus:
                    number++;
                    itemCount.setText(String.valueOf(number));
                    break;
            }

        }
        private void goToItemDescriptionActivity()
        {
            int position=getAdapterPosition();

            int image=mList.get(position).getImage();
            String title=mList.get(position).getName();
            String description=mList.get(position).getDescription();
            String price=mList.get(position).getPrice();
            String discount=mList.get(position).getDiscount();
            float rating=Float.parseFloat(mList.get(position).getRating());

            Intent itemDescription=new Intent(context, ItemDescriptionActivity.class);
            itemDescription.putExtra("title",title);
            itemDescription.putExtra("description",description);
            itemDescription.putExtra("price",price);
            itemDescription.putExtra("discount",discount);
            itemDescription.putExtra("rating",rating);
            itemDescription.putExtra("image",image);
            context.startActivity(itemDescription);
        }
    }
}
