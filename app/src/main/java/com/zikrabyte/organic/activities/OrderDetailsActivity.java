package com.zikrabyte.organic.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zikrabyte.organic.R;
import com.zikrabyte.organic.adapters.OrderHistoryAdapter;
import com.zikrabyte.organic.adapters.OrderHistoryInnerAdapter;
import com.zikrabyte.organic.api_responses.orderhistory.OrderDetails;
import com.zikrabyte.organic.api_responses.orderhistory.ProductDetail;
import com.zikrabyte.organic.api_responses.orderhistory.PromoDetails;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView mActionBack;
    RecyclerView mRecyclerView;
    LinearLayoutManager layoutManager;
    OrderHistoryInnerAdapter adapter;

    TextView mOrderedOn,mOrderID,mTxnID,mDeliveryAddress,mDeliveryDate;
    TextView mSubTotal,mDeliveryCharge,mGST,mCouponApplied,mTotalAmount;

    public static List<ProductDetail> mProductDetailsList = new ArrayList<>();
    public static PromoDetails mPromoDetails;
    public static OrderDetails mOrderDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        //mProductDetailsList =(ArrayList<ProductDetail>)getIntent().getSerializableExtra("name");
        initializeViews();
    }

    private void initializeViews()
    {
        mProductDetailsList= OrderHistoryAdapter.mProductDetailsList;
        mPromoDetails=OrderHistoryAdapter.promoDetails;
        mOrderDetails=OrderHistoryAdapter.orderDetails;

        mActionBack=findViewById(R.id.vI_aod_back_icon);

        mOrderedOn=findViewById(R.id.vT_aod_order_date);
        mOrderID=findViewById(R.id.vT_aod_order_id);
        mTxnID=findViewById(R.id.vT_aod_txn_id);
        mDeliveryAddress=findViewById(R.id.vT_aod_delivery_address);
        mDeliveryDate=findViewById(R.id.vT_aod_delivery_date);

        mSubTotal=findViewById(R.id.vT_aod_sub_total);
        mGST=findViewById(R.id.vT_aod_gst);
        mDeliveryCharge=findViewById(R.id.vT_aod_delivery_charge);
        mCouponApplied=findViewById(R.id.vT_aod_coupon_applied);
        mTotalAmount=findViewById(R.id.vT_aod_total_amount);

        layoutManager=new LinearLayoutManager(this);
        mRecyclerView=findViewById(R.id.vR_oh_inner_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter=new OrderHistoryInnerAdapter(this,mProductDetailsList);
        mRecyclerView.setAdapter(adapter);

        mActionBack.setOnClickListener(this);

        mOrderedOn.setText(mOrderDetails.getOrderedOn());
        mOrderID.setText(mOrderDetails.getOrderId());
        mTxnID.setText(mOrderDetails.getTxnid());
        mDeliveryAddress.setText(mOrderDetails.getAddress());
        mDeliveryDate.setText(mOrderDetails.getDeliveryDate()+", "+mOrderDetails.getDeliveryTime());

        mSubTotal.setText(mPromoDetails.getSubTotal());
        mDeliveryCharge.setText(mPromoDetails.getDeliveryCharges());
        mGST.setText(mPromoDetails.getGst());
        if(mPromoDetails.getPromoCode().equalsIgnoreCase(""))
        {
            mCouponApplied.setText("--");
        }
        else {
            mCouponApplied.setText(mPromoDetails.getPromoCode());

        }

        mTotalAmount.setText(mPromoDetails.getGrandTotal());

       /* Log.d("promodetails","promo code=>"+mPromoDetails.getPromoCode());
        Log.d("promodetails","delivery charges=>"+mPromoDetails.getDeliveryCharges());
        Log.d("promodetails","GST charges=>"+mPromoDetails.getGst());
        Log.d("promodetails","subTotal charges=>"+mPromoDetails.getSubTotal());
        Log.d("promodetails","grand total charges=>"+mPromoDetails.getGrandTotal());

        Log.d("orderdetails1","order id=>"+mOrderDetails.getOrderId());
        Log.d("orderdetails1","ordered on=>"+mOrderDetails.getOrderedOn());
        Log.d("orderdetails1","txn id=>"+mOrderDetails.getTxnid());
        Log.d("orderdetails1","address=>"+mOrderDetails.getAddress());
        Log.d("orderdetails1","paid on=>"+mOrderDetails.getPaidOn());*/

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.vI_aod_back_icon:
                onBackPressed();
                break;
        }

    }
}
