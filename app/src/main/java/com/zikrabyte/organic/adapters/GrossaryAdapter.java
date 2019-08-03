package com.zikrabyte.organic.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zikrabyte.organic.R;
import com.zikrabyte.organic.activities.HomeActivity;
import com.zikrabyte.organic.activities.ItemDescriptionActivity;
import com.zikrabyte.organic.activities.LogInActivity;
import com.zikrabyte.organic.api_requests.AddToCart;
import com.zikrabyte.organic.api_requests.RemoveFromCart;
import com.zikrabyte.organic.api_requests.ViewCart;
import com.zikrabyte.organic.api_responses.addtocart.AddToCartResponse;
import com.zikrabyte.organic.api_responses.all_groceryproducts.Response;
import com.zikrabyte.organic.api_responses.removefromcart.RemoveFromCartResponse;
import com.zikrabyte.organic.api_responses.viewcart.ViewCartResponse;
import com.zikrabyte.organic.beanclasses.AddToCartDummy;
import com.zikrabyte.organic.database.TinyDB;
import com.zikrabyte.organic.utils.EzzShoppUtils;
import com.zikrabyte.organic.utils.MySharedPreference;
import com.zikrabyte.organic.utils.OnResponseListener;
import com.zikrabyte.organic.utils.WebServices;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Krish on 08-03-2018.
 */

public class GrossaryAdapter extends RecyclerView.Adapter<GrossaryAdapter.MyHolder> {

    boolean isLoggedIn=false;
    String authKey, userId;

    Context context;
    List<Response> mList = new ArrayList<>();
    private int lastPosition = -1;

    //ArrayList<AddToCartDummy> addToCartList=new ArrayList<>();
    List<String > data=new ArrayList<>();
    MySharedPreference mySharedPreference=new MySharedPreference();
    ArrayList<AddToCartDummy> savedList;

    int maxQuantity;

    boolean isProductAvailable=false;
    Toast mToast;

    public GrossaryAdapter(Context context, List<Response> mList) {
        this.context = context;
        this.mList = mList;
       /* addToCartList=new ArrayList<>();
        addToCartList.clear();*/

        savedList=mySharedPreference.getFavorites(context);
        if(savedList!=null)
            HomeActivity.setCartValue(savedList.size());

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.homeactivity_singleitem_appearence, null, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

       /* Glide.with(context)
                .asBitmap()
                .load(mList.get(position).getPicture())
                .into(holder.itemImage);*/
        Picasso.get()
                .load(mList.get(position).getPicture())
                .error(context.getResources().getDrawable(R.drawable.loading))
                .placeholder(context.getResources().getDrawable(R.drawable.loading))
                .into(holder.itemImage);


        holder.itemName.setText(mList.get(position).getTitle());
        holder.itemQuantityType.setText(mList.get(position).getDescription());
        holder.itemRating.setRating(Float.parseFloat(mList.get(position).getRating()));
        holder.newPrice.setText( mList.get(position).getNewPrice());
        holder.itemQuantityType.setText(mList.get(position).getType());

        maxQuantity= Integer.parseInt(mList.get(position).getMaxQuantity());

        if(mList.get(position).getProductAvailablity()!=null)
        {
            if(!mList.get(position).getProductAvailablity().equalsIgnoreCase("YES"))
            {
                //Show outof stock mask
                isProductAvailable=false;

            }
            else {
                isProductAvailable=true;
            }
        }

        if(mList.get(position).getOldPrice()==null || mList.get(position).getOldPrice().isEmpty() || mList.get(position).getOldPrice().equalsIgnoreCase(" ")) {

            holder.oldPriceLayout.setVisibility(View.GONE);


        }
        else {
            holder.oldPrice.setText(mList.get(position).getOldPrice());
            holder.oldPrice.setPaintFlags(holder.oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.itemDiscount.setText(mList.get(position).getOffer() + " % off");

        if(isLoggedIn) {
            holder.quantity = Integer.parseInt(String.valueOf(mList.get(position).getQuantity()));
            if(holder.quantity>=1)
            {
                holder.numberofItemsLayout.setVisibility(View.VISIBLE);
                holder.addToCartLayout.setVisibility(View.GONE);
                holder.itemCount.setText(String.valueOf(mList.get(position).getQuantity()));
            }
        }
        else
        {
            if(HomeActivity.addToCartList!=null) {
                for (int i = 0; i < HomeActivity.addToCartList.size(); i++) {
                    if (mList.get(position).getId().equals(HomeActivity.addToCartList.get(i).getPriductId()) && mList.get(position).getType().equals(HomeActivity.addToCartList.get(i).getPriductQuantityType())) {
                        holder.quantity = Integer.parseInt(HomeActivity.addToCartList.get(i).getPriductQuantity());
                        Log.d("quantityy", holder.quantity + "  ");
                        if (holder.quantity >= 1) {
                            holder.numberofItemsLayout.setVisibility(View.VISIBLE);
                            holder.addToCartLayout.setVisibility(View.GONE);
                            holder.itemCount.setText(String.valueOf(holder.quantity));
                        }
                    }
                }
            }
        }



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

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnResponseListener {

        String productId, categoryId;

        LinearLayout numberofItemsLayout, addToCartLayout, mainLayout,oldPriceLayout;
        TextView itemName, itemQuantityType, newPrice,oldPrice, itemDiscount, itemCount, addToCart;
        ImageView itemImage, plusSign, minusSign;
        RatingBar itemRating;
            int quantity=0;


        public MyHolder(View itemView) {
            super(itemView);


           /* SharedPreferences preferences = context.getSharedPreferences("LOGIN_PREFERENCE", MODE_PRIVATE);
            isLoggedIn= preferences.getBoolean("IS_LOGGEDIN",false);*/

            mainLayout = itemView.findViewById(R.id.card_main_layout);

            oldPriceLayout=itemView.findViewById(R.id.vL_item_old_price_layout);
            numberofItemsLayout = itemView.findViewById(R.id.number_of_items_layout);
            addToCartLayout = itemView.findViewById(R.id.addto_cart_button_layout);

            itemName = itemView.findViewById(R.id.vT_item_name);
            itemQuantityType = itemView.findViewById(R.id.vT_item_quantity_type);
            newPrice = itemView.findViewById(R.id.vT_item_new_price);
            oldPrice=itemView.findViewById(R.id.vT_item_old_price);
            itemDiscount = itemView.findViewById(R.id.vT_item_discount);

            itemImage = itemView.findViewById(R.id.vI_item_image);
            itemRating = itemView.findViewById(R.id.vR_item_rating);

            addToCart = itemView.findViewById(R.id.vB_button_addto_cart);
            itemCount = itemView.findViewById(R.id.vT_item_count);
            plusSign = itemView.findViewById(R.id.vI_plus);
            minusSign = itemView.findViewById(R.id.vI_minus);

            plusSign.setOnClickListener(this);
            minusSign.setOnClickListener(this);
            addToCart.setOnClickListener(this);

            mainLayout.setOnClickListener(this);

          /*  if(mList.get(getAdapterPosition()+1).getQuantity()>=1)
            {
                numberofItemsLayout.setVisibility(View.VISIBLE);
                addToCartLayout.setVisibility(View.GONE);
                //itemCount.setText(mList.get(position).getQuantity());

            }*/
        }

        private void incrementCart(int quantity) {
           /* SharedPreferences preferences = context.getSharedPreferences("LOGIN_PREFERENCE", MODE_PRIVATE);
            authKey = preferences.getString("AUTH_KEY", null);
            userId = preferences.getString("USER_ID", null);
            isLoggedIn= preferences.getBoolean("IS_LOGGEDIN",false);*/
            if(isLoggedIn)
            {
                callAddToCartAPI(authKey, userId,quantity);
            }

        }

        private void callAddToCartAPI(String authKey, String userId,int quantity) {
            productId = mList.get(getAdapterPosition()).getId();
            categoryId = mList.get(getAdapterPosition()).getCategoryId();

            AddToCart addToCart = new AddToCart(productId, userId,quantity+"");
            if (EzzShoppUtils.isConnectedToInternet(context)) {
                WebServices<AddToCartResponse> webServices = new WebServices<AddToCartResponse>(context, MyHolder.this);
                webServices.addToCart(WebServices.BASE_URL, WebServices.ApiType.addToCart, authKey, userId, addToCart);
            } else {
                showToast(context.getResources().getString(R.string.err_msg_nointernet));
                //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show(); Toast.makeText(context, context.getResources().getString(R.string.err_msg_nointernet)+"", Toast.LENGTH_SHORT).show();Toast.makeText(context, R.string.err_msg_nointernet + "", Toast.LENGTH_SHORT).show();
            }
        }

        private void callRemoveFromCartAPI(String authKey, String userId)
        {
            productId=mList.get(getAdapterPosition()).getId();
            categoryId=mList.get(getAdapterPosition()).getCategoryId();

            RemoveFromCart removeFromCart=new RemoveFromCart(userId,productId);
            if(EzzShoppUtils.isConnectedToInternet(context))
            {
                WebServices<RemoveFromCartResponse> webServices=new WebServices<RemoveFromCartResponse>(context,MyHolder.this);
                webServices.removeFromCart(WebServices.BASE_URL,WebServices.ApiType.removeFromCart,authKey,userId,removeFromCart);
            }
            else {
                //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
               /* Toast.makeText(context, context.getResources().getString(R.string.err_msg_nointernet)+"", Toast.LENGTH_SHORT).show();*/
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
              /*  Toast.makeText(context, context.getResources().getString(R.string.err_msg_nointernet)+"", Toast.LENGTH_SHORT).show();*/
                showToast(context.getResources().getString(R.string.err_msg_nointernet));
            }

        }

        @Override
        public void onClick(View v) {
            int quantity = Integer.parseInt(itemCount.getText().toString());
            maxQuantity= Integer.parseInt(mList.get(getAdapterPosition()).getMaxQuantity());
            //int quantity=mList.get(getAdapterPosition()).getQuantity();

            switch (v.getId()) {
                case R.id.card_main_layout:
                    goToItemDescriptionActivity();
                    //Toast.makeText(context, mList.get(getAdapterPosition()).getId() + "", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.vB_button_addto_cart:

                    if(isLoggedIn)
                    {
                        if(isProductAvailable)
                        {
                            if(quantity<maxQuantity) {
                                numberofItemsLayout.setVisibility(View.VISIBLE);
                                addToCartLayout.setVisibility(View.GONE);
                                quantity++;
                                incrementCart(quantity);
                                itemCount.setText(String.valueOf(quantity));
                            }
                            else {
                                showToast("Exceeded max limit of cart");
                            }


                        }
                        else {
                            //Toast.makeText(context, "Out of stock", Toast.LENGTH_SHORT).show();
                            showToast("Out of stock");
                        }

                    }
                    else {
                        numberofItemsLayout.setVisibility(View.VISIBLE);
                        addToCartLayout.setVisibility(View.GONE);
                        quantity++;
                        incrementCartAndSaveLocally(quantity);
                        itemCount.setText(String.valueOf(quantity));
                    }

                    break;

                case R.id.vI_minus:


                    if(isLoggedIn)
                    {
                        if (quantity >1) {
                            quantity--;
                            incrementCart(quantity);
                            itemCount.setText(String.valueOf(quantity));

                        }
                        else if(quantity <=1)
                        {
                            quantity--;
                            itemCount.setText(String.valueOf(quantity));
                            callRemoveFromCartAPI(authKey,userId);
                            numberofItemsLayout.setVisibility(View.GONE);
                            addToCartLayout.setVisibility(View.VISIBLE);
                        }

                    }
                    else {
                        //gotoLogInActivity();

                        if (quantity >1) {
                            quantity--;
                            //incrementCartAndSaveLocally(quantity);
                            removeFromLocalCart(quantity);
                            itemCount.setText(String.valueOf(quantity));

                        }
                        else if(quantity <=1)
                        {
                            quantity--;
                            itemCount.setText(String.valueOf(quantity));
                            incrementCartAndSaveLocally(quantity);
                            // callRemoveFromLocalDatabase();
                            numberofItemsLayout.setVisibility(View.GONE);
                            addToCartLayout.setVisibility(View.VISIBLE);
                        }
                    }

                    //decrementCart();



                    /*else
                     {
                         //callRemoveFromCartAPI(authKey,userId);
                        numberofItemsLayout.setVisibility(View.GONE);
                        addToCartLayout.setVisibility(View.VISIBLE);
                    }*/
                    break;

                case R.id.vI_plus:

                    if(isLoggedIn)
                    {
                        if(isProductAvailable) {
                            if (quantity < maxQuantity) {
                                quantity++;
                                incrementCart(quantity);
                                itemCount.setText(String.valueOf(quantity));
                            }
                            else {
                                showToast("Exceeded max limit of cart");
                            }
                        }
                        else {
                            showToast("Product not available");
                        }
                    }
                    else {
                        //gotoLogInActivity();
                        if(isProductAvailable) {

                            if (quantity < maxQuantity) {
                                quantity++;
                                incrementCartAndSaveLocally(quantity);
                                itemCount.setText(String.valueOf(quantity));
                            } else {
                                showToast("Exceeded max limit of cart");
                            }
                        }else {
                            showToast("Product not available");
                        }
                    }



                    break;
            }

        }

        private void removeFromLocalCart(int quantity) {

            String productId = mList.get(getAdapterPosition()).getId();
            String categoryId = mList.get(getAdapterPosition()).getCategoryId();
            String productImage=mList.get(getAdapterPosition()).getPicture();
            String productQuantity= String.valueOf(quantity);
            String productQuantityType=mList.get(getAdapterPosition()).getType();
            String ProductName=mList.get(getAdapterPosition()).getTitle();
            String productCost=mList.get(getAdapterPosition()).getNewPrice();
            float costPerItem= Float.parseFloat(mList.get(getAdapterPosition()).getNewPrice());
            String maxQuantity=mList.get(getAdapterPosition()).getMaxQuantity();
            float subtotal;

            if (quantity > 1) {

                showToast("Cart updated");

                for (int i = 0; i < HomeActivity.addToCartList.size(); i++) {
                    if (!data.contains(productId)) {
                        data.add(productId);
                    }
                    if (productId.equalsIgnoreCase(HomeActivity.addToCartList.get(i).getPriductId()) && productQuantityType.equalsIgnoreCase(HomeActivity.addToCartList.get(i).getPriductQuantityType())) {
                        subtotal = quantity * costPerItem;
                        HomeActivity.addToCartList.get(i).setPriductQuantity(String.valueOf(quantity));
                        HomeActivity.addToCartList.get(i).setProductSubTotal(String.valueOf(subtotal));
                        mySharedPreference.saveCart(context, HomeActivity.addToCartList);
                    }
                    Log.d("localdata3", "size=>" + HomeActivity.addToCartList.size() + "");       //addToCartList.add(new AddToCartDummy(productId,ProductName,productQuantity,productQuantityType,productImage));
                }

            }
        }

        private void incrementCartAndSaveLocally(int quantity) {

            String productId = mList.get(getAdapterPosition()).getId();
            String categoryId = mList.get(getAdapterPosition()).getCategoryId();
            String productImage=mList.get(getAdapterPosition()).getPicture();
            String productQuantity= String.valueOf(quantity);
            String productQuantityType=mList.get(getAdapterPosition()).getType();
            String ProductName=mList.get(getAdapterPosition()).getTitle();
            String productCost=mList.get(getAdapterPosition()).getNewPrice();
            float costPerItem= Float.parseFloat(mList.get(getAdapterPosition()).getNewPrice());
            String maxQuantity=mList.get(getAdapterPosition()).getMaxQuantity();
            float subtotal;

          //  HomeActivity.addToCartList=mySharedPreference.getFavorites(context);
            // subtotal=quantity*costPerItem;

            //Log.d("localdata","prod ID=>"+productId+"userId=>"+userId+"quantity=>"+quantity+"Quantity Type=>"+productQuantityType+"imageurl"+productImage);

            //String productId=null;

            /* ArrayList<AddToCartDummy> addToCartList=new ArrayList<>();*/
            //addToCartList.add(new AddToCartDummy(productId,ProductName,productQuantity,productQuantityType,productImage));



            if(quantity==1)
            {
                subtotal=quantity*costPerItem;
                   /* if(addToCartList.contains(new AddToCartDummy(productId,ProductName,productCost,productQuantity,productQuantityType,productImage,subtotal+"")))
                    {*/
                if(!data.contains(productId))
                {
                    showToast("Added to cart");
                    data.add(productId);
                    AddToCartDummy addToCartDummy=new AddToCartDummy(productId,ProductName,productCost,productQuantity,productQuantityType,productImage,subtotal+"",maxQuantity);
                    if(HomeActivity.addToCartList!=null)
                        HomeActivity.addToCartList.add(addToCartDummy);
                    mySharedPreference.saveCart(context,HomeActivity.addToCartList);
                }

                //}
                Log.d("localdata1","size=>"+HomeActivity.addToCartList.size()+"");


                //mySharedPreference.saveCart(context,addToCartList);

               /* for (int i=0;i<addToCartList.size();i++)
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
                }*/
            }
            else if(quantity>1)
            {
                showToast("Cart updated");

                for (int i=0;i<HomeActivity.addToCartList.size();i++)
                {

                    if(productId.equalsIgnoreCase(HomeActivity.addToCartList.get(i).getPriductId()) && productQuantityType.equalsIgnoreCase(HomeActivity.addToCartList.get(i).getPriductQuantityType()))
                    {
                        if(!data.contains(productId))
                        {
                            data.add(productId);
                        }
                        subtotal=quantity*costPerItem;
                        HomeActivity.addToCartList.get(i).setPriductQuantity(String.valueOf(quantity));
                        HomeActivity.addToCartList.get(i).setProductSubTotal(String.valueOf(subtotal));
                        mySharedPreference.saveCart(context,HomeActivity.addToCartList);
                    }
                    Log.d("localdata3","size=>"+HomeActivity.addToCartList.size()+"");       //addToCartList.add(new AddToCartDummy(productId,ProductName,productQuantity,productQuantityType,productImage));
                }

            }
            else if(quantity<1)
            {
                showToast("Removed from cart");

                for (int i=0;i<HomeActivity.addToCartList.size();i++)
                {

                    if(productId.equalsIgnoreCase(HomeActivity.addToCartList.get(i).getPriductId()) && productQuantityType.equalsIgnoreCase(HomeActivity.addToCartList.get(i).getPriductQuantityType()))
                    {
                        //subtotal=quantity*costPerItem;
                        HomeActivity.addToCartList.remove(i);
                        data.remove(productId);
                        //addToCartList.get(i).setProductSubTotal(String.valueOf(subtotal));
                        mySharedPreference.saveCart(context,HomeActivity.addToCartList);
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

            TinyDB tinyDB=new TinyDB(context);

            savedList=mySharedPreference.getFavorites(context);
            if(savedList!=null)
                for(int i=0;i<savedList.size();i++)
                {
                    Log.d("savedlocaldata","savedList product id =>"+savedList.get(i).getPriductId()+"");
                    Log.d("savedlocaldata","savedList product id =>"+savedList.get(i).getProductName()+"");
                    Log.d("savedlocaldata","savedList quantyty =>"+savedList.get(i).getPriductQuantity()+"");
                    Log.d("savedlocaldata","savedList size =>"+savedList.size());

                }
            if(savedList!=null)
                HomeActivity.setCartValue(savedList.size());

        }

        private void goToItemDescriptionActivity() {
            int position = getAdapterPosition();

            productId = mList.get(position).getId();
            categoryId = mList.get(position).getCategoryId();
            int quantity = Integer.parseInt(itemCount.getText().toString());

            Intent itemDescription = new Intent(context, ItemDescriptionActivity.class);
            itemDescription.putExtra("categoryId", categoryId);
            itemDescription.putExtra("productId", productId);
            itemDescription.putExtra("maxQuantity", maxQuantity);
            itemDescription.putExtra("quantity",quantity);
            context.startActivity(itemDescription);
        }

        @Override
        public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {

            switch (URL) {
                case addToCart:
                    if (isSucces) {
                        AddToCartResponse addToCartResponse = (AddToCartResponse) response;

                        if (addToCartResponse != null) {
                            if(addToCartResponse.getResponsecode()!=null) {
                                if (addToCartResponse.getResponsecode().equalsIgnoreCase("200")) {

                               /* Intent intent=new Intent(context,HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);*/
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
                                    //  Toast.makeText(context, addToCartResponse.getAddCartMessage()+"", Toast.LENGTH_SHORT).show();
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
                                    List<com.zikrabyte.organic.api_responses.viewcart.Response> mCartItemList = cartResponse.getResponse();

                                    HomeActivity.setCartValue(mCartItemList.size());
                                    //Something went Wrong
                                }
                            }
                        }

                    } else {

                        HomeActivity.setCartValue(0);
                        //API call Failure
                        //Snackbar.make(mStartShopping, "API call failed", Snackbar.LENGTH_LONG).show();
                    }

                    break;

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
                                    if(mList.isEmpty())
                                    {
                                        Log.d("emptycart",mList.size()+"");
                                        HomeActivity.setCartValue(0);
                                        break;
                                    }

                                    callViewCartAPI(authKey, userId);
                                    if(removeFromCartResponse.getRemoveCartMessage()!=null)
                                    {
                                        showToast(removeFromCartResponse.getRemoveCartMessage() + "");
                                    }
                                   // Toast.makeText(context, removeFromCartResponse.getRemoveCartMessage() + "", Toast.LENGTH_SHORT).show();
                                } else {

                                    if(removeFromCartResponse.getRemoveCartMessage()!=null)
                                    {
                                        showToast(removeFromCartResponse.getRemoveCartMessage() + "");
                                    }
                                    //Toast.makeText(context, removeFromCartResponse.getRemoveCartMessage() +"", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
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