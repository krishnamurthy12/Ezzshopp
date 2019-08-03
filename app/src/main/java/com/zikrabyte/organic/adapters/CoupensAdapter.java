package com.zikrabyte.organic.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zikrabyte.organic.R;
import com.zikrabyte.organic.activities.PaymentDetailsActivity;
import com.zikrabyte.organic.api_responses.allcoupens.CouponCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krish on 19-03-2018.
 */

public class CoupensAdapter extends RecyclerView.Adapter<CoupensAdapter.MyHolder>

{
    List<CouponCode> mList=new ArrayList<>();
    Context context;
    private int lastPosition = -1;
    private int lastCheckedPosition = -1;
    Button mApply;
    boolean isFromNavigationDrawer=false;
    String rupeeSymbol=null;


    public CoupensAdapter(List<CouponCode> mList, Context context, Button mApply, boolean isFromNavigationDrawer) {
        this.mList = mList;
        this.context = context;
        this.mApply=mApply;
        this.isFromNavigationDrawer=isFromNavigationDrawer;

        rupeeSymbol=context.getResources().getString(R.string.rupee_symbol);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.coupens_single_row_appearence, parent, false);
        return new CoupensAdapter.MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        holder.mCoupenName.setText(mList.get(position).getCouponCode());
        holder.mCoupenDescription.setText(mList.get(position).getCouponDescription());


        if(mList.get(position).getCouponType().equalsIgnoreCase("1"))
        {
            holder.mCoupenAmount.setText(mList.get(position).getCouponAmt()+" %");
        }
        else if(mList.get(position).getCouponType().equalsIgnoreCase("2"))
        {
            holder.mCoupenAmount.setText(rupeeSymbol+" "+mList.get(position).getCouponAmt());
        }

        holder.mCheckBox.setChecked(position == lastCheckedPosition);


        setAnimation(holder.itemView, position);

    }

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

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int lastSelectedPosition=-1;

        TextView mCoupenName,mCoupenDescription,mCoupenAmount;
        CheckBox mCheckBox;
        //Button mApplyCoupen;
        RelativeLayout mCardlayout;
        public MyHolder(View itemView) {
            super(itemView);
            mCheckBox=itemView.findViewById(R.id.vRB_ssa_radio);
           // mApplyCoupen=itemView.findViewById(R.id.vT_ssa_apply);
            mCoupenName=itemView.findViewById(R.id.vT_ssa_coupen_name);
            mCoupenDescription=itemView.findViewById(R.id.vT_ssa_coupen_description);
            mCardlayout=itemView.findViewById(R.id.coupen_main_layout);
            mCoupenAmount=itemView.findViewById(R.id.vT_ssa_amount);
            //mApplyCoupen=itemView.findViewById(R.id.vT_ssa_coupen_apply);

            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    Toast.makeText(context, isChecked+"", Toast.LENGTH_SHORT).show();

                }
            });

            mCheckBox.setOnClickListener(this);
            disableViews(mApply);
           // mCardlayout.setOnClickListener(this);

            if(isFromNavigationDrawer){
                mCheckBox.setVisibility(View.GONE);

            }
            else {
                mCheckBox.setVisibility(View.VISIBLE);
                mCardlayout.setOnClickListener(this);

            }
            //mApplyCoupen.setOnClickListener(this);
            mApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoPaymentActivityWithCoupen(mList.get(lastCheckedPosition).getCouponCode());

                }
            });

            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {
                        //mApplyCoupen.setVisibility(View.VISIBLE);
                        enableViews(mApply);
                        //mList.get(lastCheckedPosition).getCouponCode();

                    }
                    else
                    {
                        //disableViews(mApply);
                        //mApplyCoupen.setVisibility(View.GONE);
                    }
                }
            });

            //mApplyCoupen.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
            switch (v.getId())
            {
                case R.id.vRB_ssa_radio:
                  /* gotoPaymentActivityWithCoupen(mList.get(position).getCouponCode());*/
                  if(mCheckBox.isChecked())
                  {
                      mCheckBox.setChecked(false);
                  }
                  else {
                      mCheckBox.setChecked(true);
                  }
                    lastCheckedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                    break;

                case R.id.coupen_main_layout:
                    if(mCheckBox.isChecked())
                    {
                        mCheckBox.setChecked(false);
                    }
                    else {
                        mCheckBox.setChecked(true);
                    }
                    lastCheckedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                    break;

                case R.id.vT_ssa_coupen_apply:
                    //gotoPaymentActivityWithCoupen(mList.get(position).getCouponCode());
                   /* if(!mCheckBox.isChecked())
                    {
                        mCheckBox.setChecked(true);
                        gotoPaymentActivityWithCoupen(mList.get(position).getCouponCode());
                    }*/
                    break;
            }

        }

        private void gotoPaymentActivityWithCoupen(String coupen) {
             lastCheckedPosition = -1;
            Intent intent=new Intent(context, PaymentDetailsActivity.class);
            intent.putExtra("COUPEN",coupen);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }

        private void enableViews(View... views) {
            for (View v : views) {
                v.setEnabled(true);
                v.setBackground(context.getResources().getDrawable(R.drawable.roundeded_button));

            }
        }

        private void disableViews(View... views) {
            for (View v : views) {
                v.setEnabled(false);
                v.setBackground(context.getResources().getDrawable(R.drawable.disabled_button));
            }
        }




    }
}