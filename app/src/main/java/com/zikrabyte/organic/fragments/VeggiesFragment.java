package com.zikrabyte.organic.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zikrabyte.organic.R;
import com.zikrabyte.organic.adapters.VegetablesAndFruitsAdapter;
import com.zikrabyte.organic.api_requests.AllGroceryProducts;
import com.zikrabyte.organic.api_requests.AllVeggieProducts;
import com.zikrabyte.organic.api_responses.all_groceryproducts.AllGroceryProductsResponse;
import com.zikrabyte.organic.api_responses.all_groceryproducts.Response;
import com.zikrabyte.organic.api_responses.allveggies.AllVeggiesProductsResponse;
import com.zikrabyte.organic.beanclasses.ItemDescription;
import com.zikrabyte.organic.utils.EzzShoppUtils;
import com.zikrabyte.organic.utils.OnResponseListener;
import com.zikrabyte.organic.utils.WebServices;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class VeggiesFragment extends Fragment implements OnResponseListener,SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    VegetablesAndFruitsAdapter adapter;
    ProgressBar mProgressBar;
    SwipeRefreshLayout mRefreshPage;

    //boolean isVisible=false;


    View rootView;
    Context context;

    String authKey,userId;

    List<ItemDescription> mList=new ArrayList<>();

    List<com.zikrabyte.organic.api_responses.allveggies.Response> mAllProductsList=new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;

    }

   /* @Override
    public boolean getUserVisibleHint() {
        if(!isVisible)
        {
            isVisible=true;
        }

        return super.getUserVisibleHint();

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_glossary, container, false);
        initializeViews();
       /* if(isVisible)
        {
            initializeViews();
        }*/
        return rootView;
    }


    private void initializeViews()
    {
        mRefreshPage=rootView.findViewById(R.id.swipeRefreshLayout_fragment_veggies);
        mProgressBar=rootView.findViewById(R.id.progressbar_fragment_glossary);
        recyclerView=rootView.findViewById(R.id.recyclerview_fragment_glossary);
        layoutManager=new GridLayoutManager(context,2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mRefreshPage.setOnRefreshListener(this);

       /* mList=getList();
        adapter=new VegetablesAndFruitsAdapter(context,mList);
        recyclerView.setAdapter(adapter);*/

        getSharedPreferenceData();
    }

    private List<ItemDescription> getList() {
        for(int i=0;i<20;i++)
        {

            ItemDescription object =new ItemDescription(R.drawable.delivery, "Carrot","each","20","4",
                    "24",getResources().getString(R.string.large_text));
            mList.add(object);
        }
        return mList;
    }

    private void getSharedPreferenceData()
    {
        SharedPreferences preferences=context.getSharedPreferences("LOGIN_PREFERENCE",MODE_PRIVATE);
        authKey=preferences.getString("AUTH_KEY",null);
        userId=preferences.getString("USER_ID",null);

        callGetAllProductSAPI(authKey,userId);
    }
    private void callGetAllProductSAPI(String authKey,String userId)
    {

        AllVeggieProducts allVeggieProducts =new AllVeggieProducts("1",userId,"");

        if (EzzShoppUtils.isConnectedToInternet(context)) {
/*
            GradientDrawable rainbow = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                    new int[] {Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED});*/

            //AnimationDrawable ad = getProgressBarAnimation();
            //mProgressBar.setIndeterminateDrawable(ad);
           // mProgressBar.setVisibility(View.VISIBLE);

            WebServices<AllVeggiesProductsResponse> webServices = new WebServices<AllVeggiesProductsResponse>(this);
            webServices.getAllVeggieProducts(WebServices.BASE_URL, WebServices.ApiType.getAllVeggieProducts,authKey,userId, allVeggieProducts);

        } else {
            //Snackbar.make(mSignup,R.string.err_msg_nointernet, Snackbar.LENGTH_SHORT).show();
            Toast.makeText(context, getResources().getString(R.string.err_msg_nointernet)+"", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces) {

        switch (URL)
        {
            case getAllVeggieProducts:

                if(mProgressBar.isShown()) {
                    mProgressBar.setVisibility(View.GONE);
                }
                if(isSucces)
                {
                    AllVeggiesProductsResponse categoryResponse= (AllVeggiesProductsResponse) response;

                    if(categoryResponse!=null)
                    {
                        if(categoryResponse.getResponsecode()!=null) {
                            if (categoryResponse.getResponsecode().equalsIgnoreCase("200")) {
                                mAllProductsList = categoryResponse.getResponse();
                                if (mAllProductsList != null) {
                                    adapter = new VegetablesAndFruitsAdapter(context, mAllProductsList);
                                    recyclerView.setAdapter(adapter);

                                }

                            } else {

                                if(categoryResponse.getResponseMessage()!=null)
                                {
                                    Toast.makeText(context, categoryResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                                }
                                if(categoryResponse.getProductsMessage()!=null)
                                {
                                    Toast.makeText(context, categoryResponse.getProductsMessage()+"", Toast.LENGTH_SHORT).show();
                                }

                                //no products available
                            }
                        }

                    }

                }
                else {
                    //API call failed
                }

                break;
        }



    }

    private AnimationDrawable getProgressBarAnimation(){

        GradientDrawable rainbow1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] {Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW});

        GradientDrawable rainbow2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] { Color.YELLOW, Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN});

        GradientDrawable rainbow3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] { Color.GREEN, Color.YELLOW, Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN });

        GradientDrawable rainbow4 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] { Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED, Color.MAGENTA, Color.BLUE });

        GradientDrawable rainbow5 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] { Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED, Color.MAGENTA });

        GradientDrawable rainbow6 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] {Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED });


        GradientDrawable[]  gds = new GradientDrawable[] {rainbow1, rainbow2, rainbow3, rainbow4, rainbow5, rainbow6};

        AnimationDrawable animation = new AnimationDrawable();

        for (GradientDrawable gd : gds){
            animation.addFrame(gd, 100);

        }

        animation.setOneShot(false);

        return animation;


    }

    @Override
    public void onRefresh() {
        mRefreshPage.setColorSchemeColors(context.getResources().getColor(R.color.blue),context.getResources().getColor(R.color.colorAccent),
                context.getResources().getColor(R.color.green));
        refreshContent();

    }

    public void refreshContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSharedPreferenceData();
                mRefreshPage.setRefreshing(false);

            }
        },3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mProgressBar!=null)
        {
            if(mProgressBar.isShown())
            {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mProgressBar!=null)
        {
            if(mProgressBar.isShown())
            {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }
}
