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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zikrabyte.organic.R;
import com.zikrabyte.organic.activities.CheckOutActivity;
import com.zikrabyte.organic.activities.EditAddressActivity;
import com.zikrabyte.organic.activities.SelectAddressActivity;
import com.zikrabyte.organic.api_requests.AddToCart;
import com.zikrabyte.organic.api_requests.RemoveAddress;
import com.zikrabyte.organic.api_requests.ViewAddress;
import com.zikrabyte.organic.api_responses.addtocart.AddToCartResponse;
import com.zikrabyte.organic.api_responses.removeaddress.RemoveAddressResponse;
import com.zikrabyte.organic.api_responses.viewAddress.Response;
import com.zikrabyte.organic.api_responses.viewAddress.ViewAddressResponse;
import com.zikrabyte.organic.beanclasses.AddressBean;
import com.zikrabyte.organic.utils.EzzShoppUtils;
import com.zikrabyte.organic.utils.OnResponseListener;
import com.zikrabyte.organic.utils.WebServices;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Krish on 03-03-2018.
 */

public class SelectAddressAdapter extends RecyclerView.Adapter<SelectAddressAdapter.MyHolder> {

    View customLayout;
    private int lastPosition = -1;

    Context context;
    List<Response> mList=new ArrayList<>();
    //boolean isFromNav=false;
    private boolean isFromNavigationDrawer=false;
    private boolean isEditable=false;

    public SelectAddressAdapter(Context context, List<Response> mList,boolean isFromNavigationDrawer) {
        this.context = context;
        this.mList = mList;
        //this.isFromNav=isFromPreviousActivity;
        this.isFromNavigationDrawer=isFromNavigationDrawer;

        SharedPreferences editPtreference=context.getSharedPreferences("EDITADDRESS_PREF",MODE_PRIVATE);
        isEditable=editPtreference.getBoolean("IS_EDITABLE",false);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.selectaddress_singlerow_appearence, parent, false);
        return new SelectAddressAdapter.MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        if(isFromNavigationDrawer){

            holder.mSelectedRadioButton.setVisibility(View.GONE);
            holder.mCardLayout.setClickable(false);

        }

        if(isEditable)
        {
            holder.mCardLayout.setClickable(true);
            holder. mSelectedRadioButton.setVisibility(View.VISIBLE);

        }
        else
        {
            holder.mSelectedRadioButton.setVisibility(View.GONE);
            holder.mCardLayout.setClickable(false);
        }

        holder.mName.setText(mList.get(position).getName());
        holder.mPhoneNumber.setText(mList.get(position).getPhone());
        holder.mHouseNumber.setText(mList.get(position).getAddress());
        holder.mApartmentName.setText(mList.get(position).getApartment());
        holder.mCity.setText(mList.get(position).getCity());
        holder.mPinCode.setText(mList.get(position).getPincode());

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

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener,OnResponseListener{
        RadioButton mSelectedRadioButton;
        Button mDelete,mEdit;
        TextView mName,mPhoneNumber,mHouseNumber,mApartmentName,mCity,mPinCode;
        LinearLayout mCardLayout;

        String authKey,userId;

        public MyHolder(View itemView) {
            super(itemView);

            mCardLayout=itemView.findViewById(R.id.vL_ssa_card_layout);

            mName=itemView.findViewById(R.id.vT_ssa_name);
            mPhoneNumber=itemView.findViewById(R.id.vT_ssa_contact_number);
            mHouseNumber=itemView.findViewById(R.id.vT_ssa_house_number);
            mCity=itemView.findViewById(R.id.vT_ssa_city);
            mApartmentName=itemView.findViewById(R.id.vT_ssa_apartment_name);
            mPinCode=itemView.findViewById(R.id.vT_ssa_pincode);

            mSelectedRadioButton=itemView.findViewById(R.id.vR_ssa_radiobutton);
            mDelete=itemView.findViewById(R.id.vB_ssa_delete);
            mEdit=itemView.findViewById(R.id.vB_ssa_edit);


            mDelete.setOnClickListener(this);

            mEdit.setOnClickListener(this);
            mCardLayout.setOnClickListener(this);
            mSelectedRadioButton.setOnClickListener(this);

            /*SharedPreferences editPtreference=context.getSharedPreferences("EDITADDRESS_PREF",MODE_PRIVATE);
            boolean isEditable=editPtreference.getBoolean("IS_EDITABLE",false);*/

          /*  if(isFromNav){
                mSelectedRadioButton.setVisibility(View.GONE);
                mCardLayout.setClickable(false);
                mEdit.setClickable(false);

            }
            else {
                mCardLayout.setClickable(true);
                mSelectedRadioButton.setVisibility(View.VISIBLE);
                mCardLayout.setOnClickListener(this);
                mEdit.setClickable(true);


            }*/

            /*if(isEditable)
            {
                mCardLayout.setClickable(true);
                mSelectedRadioButton.setVisibility(View.VISIBLE);
                mCardLayout.setOnClickListener(this);
                mSelectedRadioButton.setOnClickListener(this);
            }
            else
            {
                mSelectedRadioButton.setVisibility(View.GONE);
                mCardLayout.setClickable(false);
            }*/


            getSharedPreferenceData();


        }

        private void getSharedPreferenceData() {
            SharedPreferences preferences = context.getSharedPreferences("LOGIN_PREFERENCE", MODE_PRIVATE);
            authKey = preferences.getString("AUTH_KEY", null);
            userId = preferences.getString("USER_ID", null);
        }

        private void callRemoveAddressAPI(String id) {

            RemoveAddress removeAddress=new RemoveAddress(id);

            if (EzzShoppUtils.isConnectedToInternet(context)) {
                WebServices<RemoveAddressResponse> webServices = new WebServices<RemoveAddressResponse>(context, MyHolder.this);
                webServices.removeAddress(WebServices.BASE_URL, WebServices.ApiType.removeAddress, authKey, userId, removeAddress);
            } else {
                //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
                Toast.makeText(context, R.string.err_msg_nointernet + "", Toast.LENGTH_SHORT).show();
            }
        }

        public void callViewAddressAPI(String authKey,String userId)
        {
            ViewAddress viewAddress=new ViewAddress(userId);
            if (EzzShoppUtils.isConnectedToInternet(context)) {

                WebServices<ViewAddressResponse> webServices = new WebServices<ViewAddressResponse>(context,MyHolder.this);
                webServices.viewAddressList(WebServices.BASE_URL, WebServices.ApiType.viewAddressList,authKey,userId,viewAddress);
            } else {
                //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
                Toast.makeText(context, R.string.err_msg_nointernet+"", Toast.LENGTH_SHORT).show();
            }

        }
        private void callEditAddressAPI()
        {

        }

        @Override
        public void onClick(View v) {

            switch (v.getId())
            {
                case R.id.vL_ssa_card_layout:
                    gotoCheckOutActivityWithValues(getAdapterPosition());
                    mSelectedRadioButton.setSelected(true);
                    break;
                case R.id.vR_ssa_radiobutton:
                    gotoCheckOutActivityWithValues(getAdapterPosition());
                    break;
                case R.id.vB_ssa_delete:
                    showRemoveDialog(getAdapterPosition());
                    break;
                case R.id.vB_ssa_edit:
                    gotoEditAdressWithVales();
                    break;
            }

        }

        private void gotoEditAdressWithVales() {
            int pos=getAdapterPosition();
            Intent editAdressIntent=new Intent(context, EditAddressActivity.class);
            editAdressIntent.putExtra("name",mList.get(pos).getName());
            editAdressIntent.putExtra("phone",mList.get(pos).getPhone());
            editAdressIntent.putExtra("house",mList.get(pos).getAddress());
            editAdressIntent.putExtra("apartment",mList.get(pos).getApartment());
            editAdressIntent.putExtra("city",mList.get(pos).getCity());
            editAdressIntent.putExtra("pincode",mList.get(pos).getPincode());
            editAdressIntent.putExtra("spinner_position",getAdapterPosition());
            editAdressIntent.putExtra("addressid",mList.get(pos).getId());
            context.startActivity(editAdressIntent);
        }

        private void gotoCheckOutActivityWithValues(int lastSelectedPosition) {
            int pos=lastSelectedPosition;
            Intent checkOutIntent=new Intent(context, CheckOutActivity.class);
            //checkOutIntent.putExtra("addressposition", lastSelectedPosition);
            checkOutIntent.putExtra("name",mList.get(pos).getName());
            checkOutIntent.putExtra("phone",mList.get(pos).getPhone());
            checkOutIntent.putExtra("house",mList.get(pos).getAddress());
            checkOutIntent.putExtra("apartment",mList.get(pos).getApartment());
            checkOutIntent.putExtra("city",mList.get(pos).getCity());
            checkOutIntent.putExtra("pincode",mList.get(pos).getPincode());

            context.startActivity(checkOutIntent);
        }

        private void showRemoveDialog(final int position) {

            AlertDialog.Builder removeBuilder = new AlertDialog.Builder(context);
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            customLayout = layoutInflater.inflate(R.layout.removeitem_dialog, null);
            TextView mMessage=customLayout.findViewById(R.id.message);

            mMessage.setText("Are you sure you want to remove this address ?");

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

                    callRemoveAddressAPI(mList.get(position).getId());

                   /* mList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,mList.size());*/
                    //Toast.makeText(context, "Positive button" + pos, Toast.LENGTH_SHORT).show();
                    if(mList.size()!=0){
                        mList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,mList.size());
                    }
                   // listSize--;


                    dialog.dismiss();
                }
            });
        }

        @Override
        public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {

            switch (URL)
            {
                case removeAddress:
                    if(isSucces)
                    {
                        RemoveAddressResponse removeAddressResponse= (RemoveAddressResponse) response;
                        if(removeAddressResponse!=null)
                        {
                            if(removeAddressResponse.getResponsecode()!=null) {
                                if (removeAddressResponse.getResponsecode().equalsIgnoreCase("200")) {
                                    callViewAddressAPI(authKey,userId);

                                    Intent intent = new Intent(context, SelectAddressActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);

                                }
                            }
                        }

                    }
                    else
                    {
                        //API call fail
                    }
                    break;

                case viewAddressList:
                    if (isSucces) {
                        ViewAddressResponse viewAddressResponse= (ViewAddressResponse) response;
                        if(viewAddressResponse!=null)
                        {
                            if(viewAddressResponse.getResponsecode()!=null)
                            {
                            if(viewAddressResponse.getResponsecode().equalsIgnoreCase("200")) {
                                List<Response> addressList = viewAddressResponse.getResponse();
                                if (addressList != null) {
                                    //show recycler view
                                    notifyDataSetChanged();

                                    notifyItemRemoved(getAdapterPosition());
                                    notifyItemRangeChanged(getAdapterPosition(), mList.size());

                                } else {
                                    //show Empty layout

                                }
                              }

                            }
                        }
                        else
                        {
                            //Null response
                        }
                    }
                    break;
            }

        }
    }
}
