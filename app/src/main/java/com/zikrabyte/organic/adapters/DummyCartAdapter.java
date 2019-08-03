package com.zikrabyte.organic.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.zikrabyte.organic.beanclasses.AddToCartDummy;
import com.zikrabyte.organic.database.TinyDB;
import com.zikrabyte.organic.utils.EzzShoppUtils;
import com.zikrabyte.organic.utils.MySharedPreference;
import com.zikrabyte.organic.utils.OnResponseListener;
import com.zikrabyte.organic.utils.WebServices;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by KRISH on 4/6/2018.
 */

public class DummyCartAdapter extends RecyclerView.Adapter<DummyCartAdapter.MyHolder> {
    Context context;
    TextView mSubTotal;
    List<AddToCartDummy> mList=new ArrayList<>();
    private int lastPosition = -1;
    int maxQuantity;

    View customLayout;
    double subTotalAmount=0.0;

    List<String > data=new ArrayList<>();
   // ArrayList<AddToCartDummy> addToCartList=new ArrayList<>();
    MySharedPreference mySharedPreference=new MySharedPreference();
    ArrayList<AddToCartDummy> savedList;
    CartActivity cartActivity;

    Toast mToast;

    public DummyCartAdapter(Context context, ArrayList<AddToCartDummy> mList, TextView mSubTotal, CartActivity cartActivity) {
        this.context = context;
        this.mList = mList;
        this.mSubTotal=mSubTotal;
        this.cartActivity=cartActivity;

        savedList=mySharedPreference.getFavorites(context);

        if(savedList!=null) {
            HomeActivity.setCartValue(savedList.size());
            for(int i=0;i<savedList.size();i++)
            {
                subTotalAmount=subTotalAmount+Double.parseDouble(savedList.get(i).getProductCost())*Double.parseDouble(savedList.get(i).getPriductQuantity());

                //mSubTotal.setText(String.valueOf(new DecimalFormat("##.##").format(subTotalAmount)));

            }
        }

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

        maxQuantity= Integer.parseInt(mList.get(position).getMaxQuantity());

        //holder.mItemImage.setImageResource(Integer.parseInt(mList.get(position).getPicture()));

       /* maxQuantity= Integer.parseInt(mList.get(position).getMaxQuantity());*/

        Glide.with(context)
                .asBitmap()
                .load(mList.get(position).getPriductImage())
                .into(holder.mItemImage);
        holder.mItemName.setText(mList.get(position).getProductName());
        holder.mItemQuantity.setText(String.valueOf(mList.get(position).getPriductQuantity())+" X "+mList.get(position).getPriductQuantityType());
        holder.mItemCount.setText(String.valueOf(mList.get(position).getPriductQuantity()));

        Double costperItem= Double.valueOf(mList.get(position).getProductCost());
        int number=Integer.parseInt(holder.mItemCount.getText().toString());
        Double finalCost=costperItem*number;

        //subTotalAmount=Double.parseDouble(mList.get(position).getProductSubTotal());


        //holder.mItemCost.setText(finalCost+"");

        holder.mItemCost.setText(new DecimalFormat("##.##").format(finalCost));
        mSubTotal.setText(String.valueOf(new DecimalFormat("##.##").format(subTotalAmount)));

       /* for(int i=0;i<mList.size();i++)
        {
            subTotalAmount+=finalCost;

        }*/
       // mSubTotal.setText(subTotalAmount+"");


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

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener,OnResponseListener {

        String authKey,userId;
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
            SharedPreferences preferences=context.getSharedPreferences("LOGIN_PREFERENCE",MODE_PRIVATE);
            authKey=preferences.getString("AUTH_KEY",null);
            userId=preferences.getString("USER_ID",null);
            //callRemoveFromCartAPI(authKey,userId,position);
        }

       /* private void callRemoveFromCartAPI(String authKey, String userId, int position)
        {
            productId=mList.get(position).getId();
            categoryId=mList.get(position).getCategoryId();

            RemoveFromCart removeFromCart=new RemoveFromCart(userId,productId);
            if(EzzShoppUtils.isConnectedToInternet(context))
            {
                WebServices<RemoveFromCartResponse> webServices=new WebServices<RemoveFromCartResponse>(context,DummyCartAdapter.MyHolder.this);
                webServices.removeFromCart(WebServices.BASE_URL,WebServices.ApiType.removeFromCart,authKey,userId,removeFromCart);
            }
            else {
                //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
                Toast.makeText(context, R.string.err_msg_nointernet+"", Toast.LENGTH_SHORT).show();
            }
        }

        private void incrementCart(int number) {
            SharedPreferences preferences = context.getSharedPreferences("LOGIN_PREFERENCE", MODE_PRIVATE);
            authKey = preferences.getString("AUTH_KEY", null);
            userId = preferences.getString("USER_ID", null);
            callAddToCartAPI(authKey, userId,number);
        }

        private void callAddToCartAPI(String authKey, String userId,int number) {
            productId = mList.get(getAdapterPosition()).getId();
            categoryId = mList.get(getAdapterPosition()).getCategoryId();

            AddToCart addToCart = new AddToCart(productId, userId,number+"");
            if (EzzShoppUtils.isConnectedToInternet(context)) {
                WebServices<AddToCartResponse> webServices = new WebServices<AddToCartResponse>(context, DummyCartAdapter.MyHolder.this);
                webServices.addToCart(WebServices.BASE_URL, WebServices.ApiType.addToCart, authKey, userId, addToCart);
            } else {
                //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
                Toast.makeText(context, R.string.err_msg_nointernet + "", Toast.LENGTH_SHORT).show();
            }
        }*/

       /* public void callViewCartAPI(String authKey,String userId)
        {
            ViewCart viewCart=new ViewCart(userId,"");
            if (EzzShoppUtils.isConnectedToInternet(context)) {

                WebServices<ViewCartResponse> webServices = new WebServices<ViewCartResponse>(context,MyHolder.this);
                webServices.viewCart(WebServices.BASE_URL, WebServices.ApiType.viewCart,authKey,userId,viewCart);
            } else {
                //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
                Toast.makeText(context, R.string.err_msg_nointernet+"", Toast.LENGTH_SHORT).show();
            }

        }*/


        @Override
        public void onClick(View v) {

            int number=Integer.parseInt(mItemCount.getText().toString());
            int id=v.getId();
            int position=getAdapterPosition();
            Double costperItem= Double.valueOf(mList.get(position).getProductCost());

            switch (id)
            {
                case R.id.vI_csa_plus_icon:


                    if(number<maxQuantity) {
                        number++;
                        mItemCount.setText(String.valueOf(number));
                        mItemQuantity.setText(String.valueOf(number)+" X "+mList.get(position).getPriductQuantityType());
                        //incrementCart(number);
                        Double cost=number*costperItem;
                        subTotalAmount=subTotalAmount+costperItem;
                       // mItemCost.setText(cost+"");

                        mItemCost.setText(new DecimalFormat("##.##").format(cost));

                        mSubTotal.setText(String.valueOf(new DecimalFormat("##.##").format(subTotalAmount)));
                        //mSubTotal.setText(String.valueOf(subTotalAmount+cost));
                        incrementCartAndSaveLocally(number);

                    }
                    else {
                        showToast("Exceeded max limit of cart");
                        /*Toast.makeText(context, , Toast.LENGTH_SHORT).show();*/
                    }
                    break;

                case R.id.vI_csa_minus_icon:

                    Double cost1=number*costperItem;
                    if(number>1)
                    {
                        number--;
                        mItemCount.setText(String.valueOf(number));
                      //  incrementCart(number);
                        cost1=number*costperItem;
                        subTotalAmount=subTotalAmount-costperItem;
                        mItemQuantity.setText(String.valueOf(number)+" X "+mList.get(position).getPriductQuantityType());
                        //mItemCost.setText(cost1+"");
                        mItemCost.setText(new DecimalFormat("##.##").format(cost1));
                        mSubTotal.setText(String.valueOf(new DecimalFormat("##.##").format(subTotalAmount)));
                        incrementCartAndSaveLocally(number);


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

        private void incrementCartAndSaveLocally(int quantity) {

            showToast("Cart updated");

            String productId = mList.get(getAdapterPosition()).getPriductId();
           // String categoryId = mList.get(getAdapterPosition()).getCategoryId();
            String productImage=mList.get(getAdapterPosition()).getPriductImage();
            String productQuantity= String.valueOf(quantity);
            String productQuantityType=mList.get(getAdapterPosition()).getPriductQuantityType();
            String ProductName=mList.get(getAdapterPosition()).getProductName();
            String productCost=mList.get(getAdapterPosition()).getProductCost();
            float costPerItem= Float.parseFloat(mList.get(getAdapterPosition()).getProductCost());
            float subtotal;

            //HomeActivity.addToCartList=mySharedPreference.getFavorites(context);
            // subtotal=quantity*costPerItem;

            //Log.d("localdata","prod ID=>"+productId+"userId=>"+userId+"quantity=>"+quantity+"Quantity Type=>"+productQuantityType+"imageurl"+productImage);

            //String productId=null;

           /* ArrayList<AddToCartDummy> addToCartList=new ArrayList<>();*/
            //addToCartList.add(new AddToCartDummy(productId,ProductName,productQuantity,productQuantityType,productImage));



          /*  if(quantity==1)
            {
                subtotal=quantity*costPerItem;
                    if(addToCartList.contains(new AddToCartDummy(productId,ProductName,productCost,productQuantity,productQuantityType,productImage,subtotal+"")))
                    {
                if(!data.contains(productId))
                {
                    data.add(productId);
                    AddToCartDummy addToCartDummy=new AddToCartDummy(productId,ProductName,productCost,productQuantity,productQuantityType,productImage,subtotal+"");
                    HomeActivity.addToCartList.add(addToCartDummy);
                    mySharedPreference.saveCart(context,HomeActivity.addToCartList);
                    //cartActivity.showDuummmyCart();
                }

                //}
                Log.d("localdata1","size=>"+HomeActivity.addToCartList.size()+"");


                //mySharedPreference.saveCart(context,addToCartList);

                for (int i=0;i<addToCartList.size();i++)
                {

                    if(productId.equalsIgnoreCase(addToCartList.get(i).getPriductId()))
                    {
                        addToCartList.remove(i);
                        subtotal=quantity*costPerItem;
                        addToCartList.get(i).setPriductQuantity(String.valueOf(quantity));
                        addToCartList.get(i).setProductSubTotal(String.valueOf(subtotal));
                        mySharedPreference.saveCart(context,addToCartList);
                    }
                    //addToCartList.add(new AddToCartDummy(productId,ProductName,productQuantity,productQuantityType,productImage));
                }
            }
            else*/ if(quantity>=1)
            {

                for (int i=0;i<HomeActivity.addToCartList.size();i++)
                {

                    if(productId.equalsIgnoreCase(mList.get(i).getPriductId()) && productQuantityType.equalsIgnoreCase(HomeActivity.addToCartList.get(i).getPriductQuantityType()))
                    {
                        //subtotal=quantity*costPerItem;
                        //mSubTotal.setText(String.valueOf(subTotalAmount+subtotal));
                        HomeActivity.addToCartList.get(i).setPriductQuantity(String.valueOf(quantity));
                        HomeActivity.addToCartList.get(i).setProductSubTotal(String.valueOf(subTotalAmount));
                        mySharedPreference.saveCart(context,HomeActivity.addToCartList);
                        //cartActivity.showDuummmyCart();


                        //mSubTotal.setText(String.valueOf(new DecimalFormat("##.##").format(subTotalAmount)));
                    }
                    Log.d("localdata3","size=>"+HomeActivity.addToCartList.size()+"");       //addToCartList.add(new AddToCartDummy(productId,ProductName,productQuantity,productQuantityType,productImage));
                }

            }
            else if(quantity<1)
            {
                double costperitem=Double.valueOf(HomeActivity.addToCartList.get(getAdapterPosition()).getProductCost());
                subTotalAmount=subTotalAmount-Double.valueOf(HomeActivity.addToCartList.get(getAdapterPosition()).getProductCost());

                for (int i=0;i<HomeActivity.addToCartList.size();i++)
                {

                    if(productId.equalsIgnoreCase(HomeActivity.addToCartList.get(i).getPriductId()) && productQuantityType.equalsIgnoreCase(HomeActivity.addToCartList.get(i).getPriductQuantityType()))
                    {
                        //subtotal=quantity*costPerItem;

                        //mSubTotal.setText(String.valueOf(subTotalAmount+subtotal));
                        HomeActivity.addToCartList.remove(i);
                        data.remove(productId);
                       // HomeActivity.addToCartList.get(i).setProductSubTotal(String.valueOf(subTotalAmount));
                        //addToCartList.get(i).setProductSubTotal(String.valueOf(subtotal));
                        mySharedPreference.saveCart(context,HomeActivity.addToCartList);
                        //cartActivity.showDuummmyCart();

                        mSubTotal.setText(new DecimalFormat("##.##").format(subTotalAmount));
                    }
                    //addToCartList.add(new AddToCartDummy(productId,ProductName,productQuantity,productQuantityType,productImage));
                }
                Log.d("localdata2","size=>"+HomeActivity.addToCartList.size()+"");
            }

            // Log.d("localdata","list=>"+String.valueOf(addToCartList));
            Log.d("localdata","size=>"+HomeActivity.addToCartList.size()+"");
            for(int i=0;i<HomeActivity.addToCartList.size();i++)
            {
                Log.d("localdata","product id =>"+HomeActivity.addToCartList.get(i).getPriductId()+"");
                Log.d("localdata","product id =>"+HomeActivity.addToCartList.get(i).getProductName()+"");
                Log.d("localdata","quantyty =>"+HomeActivity.addToCartList.get(i).getPriductQuantity()+"");
                Log.d("localdata","cost =>"+HomeActivity.addToCartList.get(i).getProductCost()+"");
                Log.d("localdata","subtotal =>"+HomeActivity.addToCartList.get(i).getProductSubTotal()+"");

            }

            //TinyDB tinyDB=new TinyDB(context);

            if(savedList!=null)
                for(int i=0;i<savedList.size();i++)
                {
                    Log.d("savedlocaldata","savedList product id =>"+savedList.get(i).getPriductId()+"");
                    Log.d("savedlocaldata","savedList product id =>"+savedList.get(i).getProductName()+"");
                    Log.d("savedlocaldata","savedList quantyty =>"+savedList.get(i).getPriductQuantity()+"");
                    Log.d("savedlocaldata","savedList size =>"+savedList.size());

                }
            if(savedList!=null) {
                HomeActivity.setCartValue(savedList.size());
            }

        }
        private void removeItem(){

            String productId = mList.get(getAdapterPosition()).getPriductId();
            String productQuantityType=mList.get(getAdapterPosition()).getPriductQuantityType();

            int items= Integer.parseInt(mItemCount.getText().toString());
            double cost= Double.parseDouble(mItemCost.getText().toString());
            subTotalAmount=subTotalAmount-cost;

            for (int i=0;i<HomeActivity.addToCartList.size();i++)
            {

                if(productId.equalsIgnoreCase(HomeActivity.addToCartList.get(i).getPriductId()) && productQuantityType.equalsIgnoreCase(HomeActivity.addToCartList.get(i).getPriductQuantityType()))
                {
                    //subtotal=quantity*costPerItem;

                    //mSubTotal.setText(String.valueOf(subTotalAmount+subtotal));
                    HomeActivity.addToCartList.remove(i);
                    data.remove(productId);
                    // HomeActivity.addToCartList.get(i).setProductSubTotal(String.valueOf(subTotalAmount));
                    //addToCartList.get(i).setProductSubTotal(String.valueOf(subtotal));
                    mySharedPreference.saveCart(context,HomeActivity.addToCartList);
                    //cartActivity.showDuummmyCart();

                    mSubTotal.setText(new DecimalFormat("##.##").format(subTotalAmount));
                }
                //addToCartList.add(new AddToCartDummy(productId,ProductName,productQuantity,productQuantityType,productImage));
            }

            if(savedList!=null)
                HomeActivity.setCartValue(savedList.size());

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

                    /*if(mList.size()==1)
                    {
                        incrementCartAndSaveLocally(0);
                        mList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,mList.size());
                        mSubTotal.setText("0");

                    }*/
                    if(mList.size()>0){
                        //decrementCart(position);
                        //mSubTotal.setText("");
                       // incrementCartAndSaveLocally(0);
                        removeItem();
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
                                    //callViewCartAPI(authKey, userId);
                                    Toast.makeText(context, removeFromCartResponse.getRemoveCartMessage() + "", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, removeFromCartResponse.getRemoveCartMessage() +"", Toast.LENGTH_SHORT).show();
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

                                    mItemQuantity.setText(String.valueOf(addToCartResponse.getQuantity()));
                                    mItemCost.setText(String.valueOf(addToCartResponse.getTotalAmt()));

                                    //callViewCartAPI(authKey, userId);

                                    if(addToCartResponse.getUpdateCartMessage()!=null)
                                    {
                                        Toast.makeText(context, addToCartResponse.getUpdateCartMessage() + "", Toast.LENGTH_SHORT).show();

                                    }
                                } else {
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
                                    if (mCartItemList.isEmpty()) {
                                        HomeActivity.setCartValue(mCartItemList.size());

                                        if(mList.size()==0)
                                        {
                                            mSubTotal.setText("0");
                                        }
                                        else
                                        {
                                            mSubTotal.setText(String.valueOf(cartResponse.getSubTotal()));
                                        }

                                   /* showEmptyLayout();*/
                                    } else {

                                        //mList = mCartItemList;
                                   /* setAdapter(mCartItemList);*/
                                  /* mItemQuantity.setText(String.valueOf(mList.get(getAdapterPosition()).getQuantity()));
                                    mItemCost.setText(String.valueOf(mList.get(getAdapterPosition()).getTotalAmt()));*/
                                        HomeActivity.setCartValue(mCartItemList.size());
                                        mSubTotal.setText(String.valueOf(cartResponse.getSubTotal()));
                                        //mTotalAmount.setText(String.valueOf(cartResponse.getGrandTotal()));
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
