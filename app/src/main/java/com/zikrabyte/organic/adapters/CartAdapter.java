package com.zikrabyte.organic.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zikrabyte.organic.R;
import com.zikrabyte.organic.activities.CartActivity;
import com.zikrabyte.organic.activities.HomeActivity;
import com.zikrabyte.organic.api_requests.AddToCart;
import com.zikrabyte.organic.api_requests.RemoveFromCart;
import com.zikrabyte.organic.api_requests.ViewCart;
import com.zikrabyte.organic.api_responses.addtocart.AddToCartResponse;
import com.zikrabyte.organic.api_responses.removefromcart.RemoveFromCartResponse;
import com.zikrabyte.organic.api_responses.viewcart.Response;
import com.zikrabyte.organic.api_responses.viewcart.ViewCartResponse;
import com.zikrabyte.organic.beanclasses.CartBeanClass;
import com.zikrabyte.organic.utils.EzzShoppUtils;
import com.zikrabyte.organic.utils.OnResponseListener;
import com.zikrabyte.organic.utils.WebServices;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Krish on 02-03-2018.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyHolder> {

    boolean isLoggedIn=false;
    String authKey, userId;

    Context context;
    TextView mSubTotal;
    List<Response> mList=new ArrayList<>();
    private int lastPosition = -1;

    View customLayout;
    int maxQuantity;
    String quantityType=null;
    boolean isProductAvailable=false;

    Toast mToast;

    public CartAdapter(Context context, List<Response> mList, TextView mSubTotal) {
        this.context = context;
        this.mList = mList;
        this.mSubTotal=mSubTotal;

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
        View v;
        if(mList.isEmpty())
        {
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_cart_layout,parent,false);

        }
        else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_singlerow_appearence, parent, false);
        }
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        //holder.mItemImage.setImageResource(Integer.parseInt(mList.get(position).getPicture()));

        quantityType=mList.get(position).getType();
        maxQuantity= Integer.parseInt(mList.get(position).getMaxQuantity());

        int itemQuantity=Integer.parseInt(holder.mItemCount.getText().toString());
        int itemCount=Integer.parseInt(holder.mItemCount.getText().toString());


        Glide.with(context)
                .asBitmap()
                .load(mList.get(position).getPicture())
                .into(holder.mItemImage);
        holder.mItemName.setText(mList.get(position).getTitle());
        if(itemQuantity>maxQuantity || itemCount>maxQuantity)
        {
            holder.mItemQuantity.setText(String.valueOf(maxQuantity)+" X "+quantityType);
            holder.mItemCount.setText(String.valueOf(maxQuantity));
        }
        else {
            holder.mItemQuantity.setText(String.valueOf(mList.get(position).getQuantity())+" X "+quantityType);
            holder.mItemCount.setText(String.valueOf(mList.get(position).getQuantity()));
        }

        holder.mItemCost.setText(String.valueOf(mList.get(position).getTotalAmt()));

        if(mList.get(position).getProductAvailablity()!=null)
        {
            if(mList.get(position).getProductAvailablity().equalsIgnoreCase("Yes"))
            {
               isProductAvailable=true;
            }
            else {
                isProductAvailable=false;
                holder.callRemoveFromCartAPI(authKey,userId,position);
            }
        }

        //setAnimation(holder.itemView, position);

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

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener,OnResponseListener{

        String productId,categoryId;

        ImageView mItemImage, mMinusSign, mPlusSign;
        TextView mItemName, mItemQuantity, mItemCost, mItemCount, mRemoveItem;

        public MyHolder(View itemView) {
            super(itemView);

            mRemoveItem = itemView.findViewById(R.id.vT_csa_item_remove);
            mItemCount = itemView.findViewById(R.id.vT_csa_item_count);
            mItemCost = itemView.findViewById(R.id.vT_csa_item_price);
            mItemQuantity = itemView.findViewById(R.id.vT_csa_item_quantity);
            mItemName = itemView.findViewById(R.id.vT_csa_item_name);
            mItemImage = itemView.findViewById(R.id.vI_csa_item_image);
            mMinusSign = itemView.findViewById(R.id.vI_csa_minus_icon);
            mPlusSign = itemView.findViewById(R.id.vI_csa_plus_icon);

            mMinusSign.setOnClickListener(this);
            mPlusSign.setOnClickListener(this);
            mRemoveItem.setOnClickListener(this);

        }

        private void decrementCart(int position)
        {
           /* SharedPreferences preferences=context.getSharedPreferences("LOGIN_PREFERENCE",MODE_PRIVATE);
            authKey=preferences.getString("AUTH_KEY",null);
            userId=preferences.getString("USER_ID",null);*/
            callRemoveFromCartAPI(authKey,userId,position);
        }

        private void callRemoveFromCartAPI(String authKey, String userId, int position)
        {
            productId=mList.get(position).getId();
            categoryId=mList.get(position).getCategoryId();

            RemoveFromCart removeFromCart=new RemoveFromCart(userId,productId);
            if(EzzShoppUtils.isConnectedToInternet(context))
            {
                WebServices<RemoveFromCartResponse> webServices=new WebServices<RemoveFromCartResponse>(context,CartAdapter.MyHolder.this);
                webServices.removeFromCart(WebServices.BASE_URL,WebServices.ApiType.removeFromCart,authKey,userId,removeFromCart);
            }
            else {
                //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
               // Toast.makeText(context, R.string.err_msg_nointernet+"", Toast.LENGTH_SHORT).show();
                showToast(context.getResources().getString(R.string.err_msg_nointernet));
            }
        }

        private void incrementCart(int number) {
            /*SharedPreferences preferences = context.getSharedPreferences("LOGIN_PREFERENCE", MODE_PRIVATE);
            authKey = preferences.getString("AUTH_KEY", null);
            userId = preferences.getString("USER_ID", null);*/
            callAddToCartAPI(authKey, userId,number);
        }

        private void callAddToCartAPI(String authKey, String userId,int number) {
            productId = mList.get(getAdapterPosition()).getId();
            categoryId = mList.get(getAdapterPosition()).getCategoryId();

            AddToCart addToCart = new AddToCart(productId, userId,number+"");
            if (EzzShoppUtils.isConnectedToInternet(context)) {
                WebServices<AddToCartResponse> webServices = new WebServices<AddToCartResponse>(context, MyHolder.this);
                webServices.addToCart(WebServices.BASE_URL, WebServices.ApiType.addToCart, authKey, userId, addToCart);
            } else {
                //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
                /*Toast.makeText(context, R.string.err_msg_nointernet + "", Toast.LENGTH_SHORT).show();*/
                showToast(context.getResources().getString(R.string.err_msg_nointernet));
            }
        }

        public void callViewCartAPI(String authKey,String userId)
        {
            ViewCart viewCart=new ViewCart(userId,"");
            if (EzzShoppUtils.isConnectedToInternet(context)) {

                WebServices<ViewCartResponse> webServices = new WebServices<ViewCartResponse>(context,MyHolder.this);
                webServices.viewCart(WebServices.BASE_URL, WebServices.ApiType.viewCart,authKey,userId,viewCart);
            } else {
                //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
               /* Toast.makeText(context, R.string.err_msg_nointernet+"", Toast.LENGTH_SHORT).show();*/
                showToast(context.getResources().getString(R.string.err_msg_nointernet));
            }

        }


        @Override
        public void onClick(View v) {

            quantityType=mList.get(getAdapterPosition()).getType();
            maxQuantity= Integer.parseInt(mList.get(getAdapterPosition()).getMaxQuantity());

            int number=Integer.parseInt(mItemCount.getText().toString());
            int id=v.getId();
            int position=getAdapterPosition();

            switch (id)
            {
                case R.id.vI_csa_plus_icon:
                    if(number<maxQuantity)
                    {
                        number++;
                        mItemCount.setText(String.valueOf(number));
                        mItemQuantity.setText(String.valueOf(number)+" X "+quantityType);
                        incrementCart(number);

                    }
                    else {
                        /*Toast.makeText(context, "Exceeded max limit of cart", Toast.LENGTH_SHORT).show();*/
                        showToast("Exceeded max limit of cart");
                    }

                    break;

                case R.id.vI_csa_minus_icon:

                    if(number>1)
                    {
                        number--;
                        mItemCount.setText(String.valueOf(number));
                        mItemQuantity.setText(String.valueOf(number)+" X "+quantityType);
                        incrementCart(number);

                    }
                    else
                    {
                        showRemoveDialog(getAdapterPosition());
                    }
                    break;

                case R.id.vT_csa_item_remove:
                    showRemoveDialog(position);
                    break;
            }

        }



        private void showRemoveDialog(final int position) {

            AlertDialog.Builder removeBuilder = new AlertDialog.Builder(context);
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            customLayout = layoutInflater.inflate(R.layout.removeitem_dialog, null);

            removeBuilder.setView(customLayout);
            removeBuilder.setCancelable(false);
            final Dialog dialog = removeBuilder.create();

            dialog.show();

            TextView cancel, confirm;
            cancel = customLayout.findViewById(R.id.vT_rd_cancel);
            confirm = customLayout.findViewById(R.id.vT_rd_confirm);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "Negativr button" + pos, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
            });
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "Positive button" + pos, Toast.LENGTH_SHORT).show();
                    if(mList.size()>0){
                        decrementCart(position);
                        mList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,mList.size());
                    }
                    dialog.dismiss();
                }
            });
        }

        @Override
        public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {

            switch (URL)
            {
                case removeFromCart:
                    if(isSucces)
                    {
                        RemoveFromCartResponse removeFromCartResponse= (RemoveFromCartResponse) response;
                        if(removeFromCartResponse!=null)
                        {
                            if(removeFromCartResponse.getResponsecode()!=null) {
                                if (removeFromCartResponse.getResponsecode().equalsIgnoreCase("200")) {
                               /* Intent intent=new Intent(context,CartActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);*/
                                    //context.startActivity(new Intent(context,CartActivity.class));

                                    if(mList.size()==0)
                                    {
                                        mSubTotal.setText("0");
                                    }
                                    callViewCartAPI(authKey, userId);
                                   /* Toast.makeText(context, removeFromCartResponse.getRemoveCartMessage() + "", Toast.LENGTH_SHORT).show();*/

                                   if(removeFromCartResponse.getRemoveCartMessage()!=null)
                                   {
                                       showToast(removeFromCartResponse.getRemoveCartMessage() + "");
                                   }
                                } else {
                                    if(removeFromCartResponse.getRemoveCartMessage()!=null)
                                    {
                                        showToast(removeFromCartResponse.getRemoveCartMessage() + "");
                                    }
                                }
                            }
                        }
                    }
                    break;

                case addToCart:
                    if (isSucces) {
                        AddToCartResponse addToCartResponse = (AddToCartResponse) response;
                        if (addToCartResponse != null) {
                            if (addToCartResponse.getResponsecode() != null) {
                                if (addToCartResponse.getResponsecode().equalsIgnoreCase("200")) {

                               /* Intent intent=new Intent(context,CartActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);*/

                                  /*  mItemQuantity.setText(String.valueOf(addToCartResponse.getQuantity()));*/
                                    mItemCost.setText(String.valueOf(addToCartResponse.getTotalAmt()));

                                    callViewCartAPI(authKey, userId);

                                    if(addToCartResponse.getUpdateCartMessage()!=null)
                                    {
                                        //Toast.makeText(context, addToCartResponse.getUpdateCartMessage() + "", Toast.LENGTH_SHORT).show();

                                        showToast(addToCartResponse.getUpdateCartMessage() + "");
                                    }
                                } else {

                                    if(addToCartResponse.getUpdateCartMessage()!=null)
                                    {
                                        //Toast.makeText(context, addToCartResponse.getUpdateCartMessage() + "", Toast.LENGTH_SHORT).show();

                                        showToast(addToCartResponse.getUpdateCartMessage() + "");
                                    }
                                    //Toast.makeText(context, addToCartResponse.getAddCartMessage() +"", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                    break;

                case viewCart:
                    if(isSucces) {
                        ViewCartResponse cartResponse = (ViewCartResponse) response;
                        if (cartResponse != null) {
                            if(cartResponse.getResponsecode()!=null) {
                                if (cartResponse.getResponsecode().equalsIgnoreCase("200")) {
                                    List<Response> mCartItemList = cartResponse.getResponse();
                                    if(mCartItemList!=null) {
                                        if (mCartItemList.isEmpty()) {

                                            HomeActivity.setCartValue(mCartItemList.size());

                                            if (mList.size() == 0) {
                                                mSubTotal.setText("0");
                                            } else {
                                                mSubTotal.setText(String.valueOf(cartResponse.getSubTotal()));
                                            }

                                   /* showEmptyLayout();*/
                                        } else {

                                            mList = mCartItemList;
                                   /* setAdapter(mCartItemList);*/
                                  /* mItemQuantity.setText(String.valueOf(mList.get(getAdapterPosition()).getQuantity()));
                                    mItemCost.setText(String.valueOf(mList.get(getAdapterPosition()).getTotalAmt()));*/
                                            HomeActivity.setCartValue(mCartItemList.size());
                                            mSubTotal.setText(String.valueOf(cartResponse.getSubTotal()));
                                            //mTotalAmount.setText(String.valueOf(cartResponse.getGrandTotal()));
                                        }
                                    }
                                    //Something went Wrong
                                }
                            }
                        }

                    } else {
                        //API call Failure
                        //Snackbar.make(mStartShopping, "API call failed", Snackbar.LENGTH_LONG).show();
                    }

                    break;
            }

        }
    }

    public void showToast (String message){
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
