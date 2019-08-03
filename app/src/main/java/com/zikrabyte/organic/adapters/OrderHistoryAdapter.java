package com.zikrabyte.organic.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zikrabyte.organic.R;
import com.zikrabyte.organic.activities.OrderDetailsActivity;
import com.zikrabyte.organic.api_requests.CancelOrder;
import com.zikrabyte.organic.api_requests.ViewCart;
import com.zikrabyte.organic.api_responses.cancelorder.CancelOrderResponse;
import com.zikrabyte.organic.api_responses.orderhistory.OrderDetail;
import com.zikrabyte.organic.api_responses.orderhistory.OrderDetails;
import com.zikrabyte.organic.api_responses.orderhistory.ProductDetail;
import com.zikrabyte.organic.api_responses.orderhistory.PromoDetails;
import com.zikrabyte.organic.api_responses.viewcart.ViewCartResponse;
import com.zikrabyte.organic.utils.EzzShoppUtils;
import com.zikrabyte.organic.utils.OnResponseListener;
import com.zikrabyte.organic.utils.WebServices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Krish on 08-03-2018.
 */

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyHolder> {

    int listSize = 5;
    View customLayout;

    boolean isLoggedIn=false;
    String authKey, userId;

    Context context;
    public List<OrderDetail> mList = new ArrayList<>();
    public static List<ProductDetail> mProductDetailsList = new ArrayList<>();
    public static PromoDetails promoDetails;
    public static OrderDetails orderDetails;

    OrderHistoryInnerAdapter innerAdapter;


    public OrderHistoryAdapter(Context context, List<OrderDetail> mList) {
        this.context = context;
        this.mList = mList;


       /* for (int i=0;i<mList.size();i++)
        {
            mProductDetailsList=mList.get(i).getProductDetails();
        }*/
       getSharedPreferenceData();

    }

    private void getSharedPreferenceData()
    {
        SharedPreferences preferences = context.getSharedPreferences("LOGIN_PREFERENCE", MODE_PRIVATE);
        authKey = preferences.getString("AUTH_KEY", null);
        userId = preferences.getString("USER_ID", null);
        isLoggedIn= preferences.getBoolean("IS_LOGGEDIN",false);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_singlerow_appearence, parent, false);
        return new OrderHistoryAdapter.MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        if (mList != null) {
            holder.mOrderID.setText(mList.get(position).getOrderDetails().getOrderId());
            holder.mTxnId.setText(mList.get(position).getOrderDetails().getTxnid());
            holder.mOrderdate.setText(mList.get(position).getOrderDetails().getOrderedOn());
            holder.mTotalAmount.setText(mList.get(position).getPromoDetails().getGrandTotal());
            holder.mSubTotal.setText(mList.get(position).getPromoDetails().getSubTotal());

            if(mList.get(position).getOrderDetails().getPaidOn()!=null)
            {
                if(mList.get(position).getOrderDetails().getPaidOn().equalsIgnoreCase("Pending"))
                {
                    //show delivery status layout
                    holder.mDeliveryStatusLayout.setVisibility(View.VISIBLE);
                    holder.mCancelSuccessText.setVisibility(View.GONE);
                    holder.mCancelOrder.setVisibility(View.VISIBLE);

                    holder.mDeliveryStatus.setText(mList.get(position).getOrderDetails().getPaidOn());

                }
                else if(mList.get(position).getOrderDetails().getPaidOn().equalsIgnoreCase("Cancelled"))
                {
                    holder.mDeliveryStatusLayout.setVisibility(View.GONE);
                    holder. mCancelSuccessText.setVisibility(View.VISIBLE);
                    holder.mCancelOrder.setVisibility(View.GONE);

                }
                else {

                    holder.mDeliveryStatusLayout.setVisibility(View.VISIBLE);
                    holder.mCancelSuccessText.setVisibility(View.GONE);
                    holder.mCancelOrder.setVisibility(View.GONE);

                    holder.mDeliveryStatus.setText(mList.get(position).getOrderDetails().getPaidOn());
                    holder.mDeliveryStatus.setTextColor(context.getResources().getColor(R.color.green));

                }
            }

            mProductDetailsList = mList.get(position).getProductDetails();
            promoDetails=mList.get(position).getPromoDetails();
            orderDetails=mList.get(position).getOrderDetails();
            //innerAdapter = new OrderHistoryInnerAdapter(context, mProductDetailsList);
            //holder.mRecyclerView.setAdapter(innerAdapter);

        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnResponseListener {
        LinearLayout mRootLayout;

      /*  LinearLayout mDateLayout,mProductDetailsLayout,mCostLayout;
        LinearLayout.LayoutParams layoutParamsforDateLayout,layoutParamsforProductLayout,layoutParamsforCostLayout;
        LinearLayout.LayoutParams viewParams;
        TextView mItemName,mItemQuantity,mRupeeSymbol,mItemCost;
        TextView mOrderDateText,mOrderedDate;
        TextView mTotalCostText,mRupeeSymbolForCost,mTotalCost;*/

        TextView mOrderdate, mSubTotal, mTotalAmount,mOrderID,mTxnId, mDeliveryStatus,mCancelSuccessText;
        Button mCancelOrder;
        //RecyclerView mRecyclerView;
        RelativeLayout mDeliveryStatusLayout;
        ProgressBar mProgressBar;

        public MyHolder(View itemView) {
            super(itemView);

            mRootLayout = itemView.findViewById(R.id.root_layout);

           /* mRecyclerView = itemView.findViewById(R.id.vR_oh_inner_recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));*/


            mOrderID=itemView.findViewById(R.id.vT_oh_order_id);
            mSubTotal = itemView.findViewById(R.id.vT_oh_sub_total);
            mTotalAmount = itemView.findViewById(R.id.vT_oh_total_amount);
            mOrderdate = itemView.findViewById(R.id.vT_oh_order_date);
            mTxnId = itemView.findViewById(R.id.vT_oh_txn_id);
            mDeliveryStatus = itemView.findViewById(R.id.vT_oh_delivery_status);
            mCancelOrder = itemView.findViewById(R.id.vT_oh_cancel_order);
            mCancelSuccessText=itemView.findViewById(R.id.vT_oh_order_cancelled);
            mDeliveryStatusLayout=itemView.findViewById(R.id.vR_delivery_status_layout);

            mProgressBar=itemView.findViewById(R.id.vP_oh_progressBar);
            mProgressBar.setVisibility(View.GONE);
            mCancelOrder.setOnClickListener(this);
            mRootLayout.setOnClickListener(this);


           /* viewParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


            layoutParamsforDateLayout=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParamsforDateLayout.setMargins(0,8,0,8);
            layoutParamsforDateLayout.gravity = Gravity.CENTER_HORIZONTAL;

            layoutParamsforProductLayout=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParamsforProductLayout.setMargins(56,8,16,8);
            layoutParamsforProductLayout.gravity = Gravity.CENTER;

            layoutParamsforCostLayout=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParamsforCostLayout.setMargins(0,8,26,8);
            layoutParamsforCostLayout.gravity = Gravity.END;
            mDateLayout=new LinearLayout(context);
            mProductDetailsLayout=new LinearLayout(context);
            mCostLayout=new LinearLayout(context);


            mDateLayout.setOrientation(LinearLayout.HORIZONTAL);
            mDateLayout.setLayoutParams(layoutParamsforProductLayout);
            mDateLayout.setGravity(Gravity.END);

            mOrderDateText=new TextView(context);
            mOrderDateText.setTextSize(16);
            mOrderDateText.setPadding(16,0,16,0);
            mOrderDateText.setText("Ordered on : ");


            mOrderedDate=new TextView(context);
            mOrderedDate.setPadding(16,0,16,0);
            //mOrderedDate.setTextColor(context.getResources().getColor(R.color.black));
            mOrderedDate.setText("12-03-2018 3:00 pm");
            mOrderedDate.setTextSize(16);

            mDateLayout.addView(mOrderDateText);
            mDateLayout.addView(mOrderedDate);

            mProductDetailsLayout.setOrientation(LinearLayout.HORIZONTAL);
            mProductDetailsLayout.setLayoutParams(layoutParamsforProductLayout);
            mProductDetailsLayout.setGravity(Gravity.START);
//            mProductDetailsLayout.setGravity(View.TEXT_ALIGNMENT_CENTER);

            mCostLayout.setOrientation(LinearLayout.HORIZONTAL);
            mCostLayout.setLayoutParams(layoutParamsforCostLayout);
            mCostLayout.setGravity(Gravity.END);


             mItemQuantity =new TextView(context);
             mRupeeSymbol=new TextView(context);
             mItemCost=new TextView(context);
             mItemName=new TextView(context);

             mItemName.setLayoutParams(viewParams);
             itemView.setId(View.generateViewId());
             mItemName.setText("product name");
             mItemName.setPadding(16,0,56,0);
             mItemName.setTextSize(16);

             mItemQuantity.setLayoutParams(viewParams);
             mItemQuantity.setId(View.generateViewId());
             mItemQuantity.setText("5");
             mItemQuantity.setPadding(16,0,56,0);
             mItemQuantity.setTextSize(16);

             mRupeeSymbol.setLayoutParams(viewParams);
             mRupeeSymbol.setText(context.getResources().getString(R.string.rupee_symbol));
             mRupeeSymbol.setTextColor(context.getResources().getColor(R.color.black));
             mRupeeSymbol.setPadding(16,0,8,0);
             mRupeeSymbol.setTextSize(18);

             mItemCost.setLayoutParams(viewParams);
             mItemCost.setText("123");
             mItemCost.setTextColor(context.getResources().getColor(R.color.black));
             mItemCost.setPadding(0,0,16,0);
             mItemCost.setTextSize(16);


            mProductDetailsLayout.addView(mItemName);
            mProductDetailsLayout.addView(mItemQuantity);
            mProductDetailsLayout.addView(mRupeeSymbol);
            mProductDetailsLayout.addView(mItemCost);


            mTotalCost=new TextView(context);
            mTotalCost.setLayoutParams(viewParams);

            mTotalCostText=new TextView(context);
            mTotalCostText.setLayoutParams(viewParams);

            mRupeeSymbolForCost=new TextView(context);
            mRupeeSymbolForCost.setLayoutParams(viewParams);


            mTotalCostText.setText("Total Amount");
            mTotalCostText.setTextColor(context.getResources().getColor(R.color.blue));
            mTotalCostText.setTextSize(18);
            mTotalCostText.setPadding(16,0,26,0);

            mRupeeSymbolForCost.setLayoutParams(viewParams);
            mRupeeSymbolForCost.setText(context.getResources().getString(R.string.rupee_symbol));
            mRupeeSymbolForCost.setTextColor(context.getResources().getColor(R.color.blue));
            mRupeeSymbolForCost.setPadding(16,0,8,0);
            mRupeeSymbolForCost.setTextSize(20);

            mTotalCost.setText("123");
            mTotalCost.setTextColor(context.getResources().getColor(R.color.blue));
            mTotalCost.setPadding(0,0,16,0);
            mTotalCost.setTextSize(18);

            mCostLayout.addView(mTotalCostText);
            mCostLayout.addView(mRupeeSymbolForCost);
            mCostLayout.addView(mTotalCost);

            mRootLayout.addView(mDateLayout);
            mRootLayout.addView(mProductDetailsLayout);
            mRootLayout.addView(mCostLayout);*/

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.vT_oh_cancel_order:
                    callCancelOrderAPI(authKey,userId,getAdapterPosition());
                    break;

                case R.id.root_layout:
                    gotoDetailsActivity();

                    break;
            }

        }

        private void gotoDetailsActivity() {

            mProductDetailsList = mList.get(getAdapterPosition()).getProductDetails();
            promoDetails=mList.get(getAdapterPosition()).getPromoDetails();
            orderDetails=mList.get(getAdapterPosition()).getOrderDetails();
            Intent intent=new Intent(context,OrderDetailsActivity.class);
           // intent.putExtra("name", (Serializable) mProductDetailsList);
            context.startActivity(intent);
           // context.startActivity(new Intent(context, OrderDetailsActivity.class));
        }


        private void callCancelOrderAPI(String authKey, String userId, int adapterPosition) {


            String user_id = userId;
            String order_id = mList.get(adapterPosition).getOrderDetails().getOrderId();
            String phone = mList.get(adapterPosition).getOrderDetails().getPhone();
            String amount = mList.get(adapterPosition).getPromoDetails().getGrandTotal();
            if(user_id!=null && order_id!=null && phone!=null && amount!=null) {

                CancelOrder cancelOrder = new CancelOrder(user_id, order_id, phone, amount);

                if (EzzShoppUtils.isConnectedToInternet(context)) {

                    mProgressBar.setVisibility(View.VISIBLE);

                    WebServices<CancelOrderResponse> webServices = new WebServices<CancelOrderResponse>(context, MyHolder.this);
                    webServices.cancelOrder(WebServices.BASE_URL, WebServices.ApiType.cancelOrder, authKey, userId, cancelOrder);
                } else {
                    //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
                    Toast.makeText(context, R.string.err_msg_nointernet + "", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(context, "Invalid parameters", Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {
            switch (URL) {
                case cancelOrder:
                    if(mProgressBar.isShown())
                    {
                        mProgressBar.setVisibility(View.GONE);
                    }
                    CancelOrderResponse cancelOrderResponse = (CancelOrderResponse) response;
                    if (isSucces) {
                        if (cancelOrderResponse != null) {
                            if (cancelOrderResponse.getResponsecode() != null) {
                                if (cancelOrderResponse.getResponsecode().equalsIgnoreCase("200")) {
                                    updateUI();
                                    if (cancelOrderResponse.getCancelOrderMessage() != null) {
                                        Toast.makeText(context, cancelOrderResponse.getCancelOrderMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // failed to cancel
                                    if (cancelOrderResponse.getCancelOrderMessage() != null) {
                                        Toast.makeText(context, cancelOrderResponse.getCancelOrderMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }

                    } else {
                        //API call failed
                    }
            }
        }

        private void updateUI() {
            mCancelOrder.setVisibility(View.GONE);
            mDeliveryStatusLayout.setVisibility(View.GONE);
            mCancelSuccessText.setVisibility(View.VISIBLE);
        }
    }
}
