package com.zikrabyte.organic.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zikrabyte.organic.R;
import com.zikrabyte.organic.api_responses.orderhistory.ProductDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krish on 25-03-2018.
 */

public class OrderHistoryInnerAdapter extends RecyclerView.Adapter<OrderHistoryInnerAdapter.MyHolder> {

    Context context;
    List<ProductDetail> mList=new ArrayList<>();

    public OrderHistoryInnerAdapter(Context context, List<ProductDetail> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.orderhistory_inner_recycler_view, parent, false);
        return new OrderHistoryInnerAdapter.MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.mItemName.setText(mList.get(position).getTitle());
        holder.mItemQuantity.setText(mList.get(position).getQuantity());
        holder.mItemCost.setText(mList.get(position).getProductAmt()+"/"+mList.get(position).getProductType());

                Log.e("mList",mList.size()+"<><>");

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView mItemName,mItemQuantity,mItemCost;
        public MyHolder(View itemView) {
            super(itemView);
            mItemName=itemView.findViewById(R.id.vT_oh_inner_item_name);
            mItemQuantity=itemView.findViewById(R.id.vT_oh_inner_item_quantity);
            mItemCost=itemView.findViewById(R.id.vT_oh_inner_item_cost);
        }
    }
}
