package com.zikrabyte.organic.utils;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.util.Log;
import android.widget.Adapter;

import com.google.gson.JsonObject;
import com.zikrabyte.organic.api_requests.AddAddress;
import com.zikrabyte.organic.api_requests.AddToCart;
import com.zikrabyte.organic.api_requests.AllCoupens;
import com.zikrabyte.organic.api_requests.AllGroceryProducts;
import com.zikrabyte.organic.api_requests.AllVeggieProducts;
import com.zikrabyte.organic.api_requests.ApplyReferral;
import com.zikrabyte.organic.api_requests.BulkAPI;
import com.zikrabyte.organic.api_requests.CancelOrder;
import com.zikrabyte.organic.api_requests.CashOnDelivery;
import com.zikrabyte.organic.api_requests.CheckUser;
import com.zikrabyte.organic.api_requests.CoupenAppliedCart;
import com.zikrabyte.organic.api_requests.EditAddress;
import com.zikrabyte.organic.api_requests.ForgotPassword;
import com.zikrabyte.organic.api_requests.ItemDescription;
import com.zikrabyte.organic.api_requests.LogIn;
import com.zikrabyte.organic.api_requests.OrderHistory;
import com.zikrabyte.organic.api_requests.Register;
import com.zikrabyte.organic.api_requests.RemoveAddress;
import com.zikrabyte.organic.api_requests.RemoveFromCart;
import com.zikrabyte.organic.api_requests.Search;
import com.zikrabyte.organic.api_requests.SendOTP;
import com.zikrabyte.organic.api_requests.VerifyOTP;
import com.zikrabyte.organic.api_requests.ViewAddress;
import com.zikrabyte.organic.api_requests.ViewCart;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Krish on 08-03-2018.
 */

public class WebServices<T> {
    T t;
    Call<T> call=null;
    public T getT() {
        return t;
    }

    public void setT(T t) {

        this.t = t;
    }
    ApiType apiTypeVariable;
    Context context;
    OnResponseListener<T> onResponseListner;
    private static OkHttpClient.Builder builder;

    public enum ApiType {
        checkUser,userlogin,userSignUp,getCategories,getAllVeggieProducts,getAllGroceryProducts,itemDescription,addToCart,viewCart,removeFromCart,
        addAddress,viewAddressList,editAddress,removeAddress,viewApartments,sendOTP,verifyOTP,viewUserProfile,editUserProfile,
        updateUserDetails,allCoupens,deliveryDays,search,coupenApplied,orderHistory,applyReferralCode,forgotPassword,
        updatePassword,cod,bulkCart,cancelOrder

    }
    public static final String BASE_URL="http://mob-india.com/ezzshop/";

    public WebServices(OnResponseListener<T> onResponseListner) {
        this.onResponseListner = onResponseListner;

        if (onResponseListner instanceof Activity) {
            this.context = (Context) onResponseListner;
        } else if (onResponseListner instanceof IntentService) {
            this.context = (Context) onResponseListner;
        } else if (onResponseListner instanceof android.app.DialogFragment) {
            android.app.DialogFragment dialogFragment = (android.app.DialogFragment) onResponseListner;
            this.context = dialogFragment.getActivity();
        }else if (onResponseListner instanceof android.app.Fragment) {
            android.app.Fragment fragment = (android.app.Fragment) onResponseListner;
            this.context = fragment.getActivity();
        }
         else if (onResponseListner instanceof Adapter) {

            this.context = (Context) onResponseListner;
        }
        else if (onResponseListner instanceof Adapter) {
            this.context = (Context) onResponseListner;
        }
            else {
            android.support.v4.app.Fragment fragment = (android.support.v4.app.Fragment) onResponseListner;
            this.context = fragment.getActivity();
        }

        builder = getHttpClient();
    }

    public WebServices(Context context, OnResponseListener<T> onResponseListner) {
        this.onResponseListner = onResponseListner;
        this.context = context;
        builder = getHttpClient();
    }


    public OkHttpClient.Builder getHttpClient() {

        if (builder == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.connectTimeout(10000, TimeUnit.SECONDS);
            client.readTimeout(10000, TimeUnit.SECONDS).build();
            client.addInterceptor(loggingInterceptor);

             /*to pass header information with request*/
            client.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder().addHeader("projectauthkey", "9dec1d4091c608bf25b6881cde959ad5").build();
                    return chain.proceed(request);
                }
            });
            return client;
        }
        return builder;
    }

    private Retrofit getRetrofitClient(String api)
    {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(api)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public void checkUser(String api,ApiType apiTypes, CheckUser checkUser)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(api)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.checkUser(checkUser);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }

    public void userLogIn(String api,ApiType apiTypes, LogIn logIn)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.logIn(logIn);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }

    public void forgotPassword(String api,ApiType apiTypes, ForgotPassword forgotPassword)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.forgotPassword(forgotPassword);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }

    public void registerUser(String api, final ApiType apiTypes, Register register)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>) ezzShoppAPI.registerUser(register);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });

    }

    public void getCategories(String api,ApiType apiTypes,String authKey,String userId)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.getCategories(authKey,userId);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }

    public void getAllGroceryProducts(String api, ApiType apiTypes, String authKey, String userId, AllGroceryProducts allGroceryProducts)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.getAllGroceryProducts(authKey,userId, allGroceryProducts);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }

    public void getAllVeggieProducts(String api, ApiType apiTypes, String authKey, String userId, AllVeggieProducts allVeggieProducts)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.getAllVeggieProducts(authKey,userId, allVeggieProducts);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }

    public void getItemDescription(String api, ApiType apiTypes, String authKey, String userId, ItemDescription itemDescription)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.getItemDescription(authKey,userId,itemDescription);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }

    public void addToCart(String api, ApiType apiTypes, String authKey, String userId, AddToCart addToCart)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.addToCart(authKey,userId,addToCart);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }

    public void viewCart(String api, ApiType apiTypes, String authKey, String userId, ViewCart viewCart)
    {
        Log.d("flowcheck","Inside webservices");
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.viewCartItems(authKey,userId,viewCart);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }

    public void removeFromCart(String api, ApiType apiTypes, String authKey, String userId, RemoveFromCart removeFromCart)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.removeFromCart(authKey,userId,removeFromCart);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }

    public void viewApartments(String api, ApiType apiTypes, String authKey, String userId)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.viewApartments(authKey,userId);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }

    public void addAddress(String api, ApiType apiTypes, String authKey, String userId, AddAddress addAddress)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.addAddress(authKey,userId,addAddress);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }

    public void removeAddress(String api, ApiType apiTypes, String authKey, String userId, RemoveAddress removeAddress)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.removeAddress(authKey,userId,removeAddress);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }

    public void editAddress(String api, ApiType apiTypes, String authKey, String userId, EditAddress editAddress)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.editAddress(authKey,userId,editAddress);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }

    public void viewAddressList(String api, ApiType apiTypes, String authKey, String userId, ViewAddress viewAddress)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.viewAddressList(authKey,userId,viewAddress);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }

    public void sendOTP(String api, final ApiType apiTypes, SendOTP sendOTP)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>) ezzShoppAPI.sendOTP(sendOTP);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });

    }

    public void verifyOTP(String api, final ApiType apiTypes, VerifyOTP verifyOTP)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>) ezzShoppAPI.verifyOTP(verifyOTP);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });

    }

    public void viewUserProfile(String api, ApiType apiTypes, String authKey, String userId)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.viewUserProfile(authKey,userId,userId);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }

    public void editUserProfileDetails(String api, ApiType apiTypes, String authKey, String userId, String  user_Id,
                                       String  name, String  email, String  phone,String picture)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.editUserProfileDetails(authKey,userId,user_Id,name,email,phone,picture);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }

    public void editUserProfile(String api, ApiType apiTypes, String authKey, String userId, RequestBody  user_Id,
                                RequestBody  name, RequestBody  email, RequestBody  phone, MultipartBody.Part filePath)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.editUserProfile(authKey,userId,user_Id,name,email,phone,filePath);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }
    public void getAllCoupens(String api, ApiType apiTypes, String authKey, String userId, AllCoupens allCoupens)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.getAllCoupens(authKey,userId,allCoupens);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }

    public void getDeliveryDate(String api, ApiType apiTypes, String authKey, String userId)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.getDeliveryDays(authKey,userId);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }

    public void searchProduct(String api, ApiType apiTypes, String authKey, String userId, Search search)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.searchProduct(authKey,userId,search);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }

    public void coupenAppliedResponse(String api, ApiType apiTypes, String authKey, String userId, CoupenAppliedCart appliedCart)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.viewCartItemsAfterApplyingCoupen(authKey,userId,appliedCart);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }


    public void getOrderHistory(String api, ApiType apiTypes, String authKey, String userId, OrderHistory orderHistory)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.getOrderHistory(authKey,userId,orderHistory);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }

    public void applyReferralCode(String api, ApiType apiTypes, String authKey, String userId, ApplyReferral applyReferral)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.applyReferralCode(authKey,userId,applyReferral);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }

    public void cashOnDelivery(String api, ApiType apiTypes, String authKey, String userId, CashOnDelivery cashOnDelivery)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.cashOnDelivery(authKey,userId,cashOnDelivery);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }


    public void bulkCartAPI(String api, ApiType apiTypes, String authKey, String userId, JsonObject bulkAPI)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.bulkCartAPI(authKey,userId,bulkAPI);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);
                Log.e("response",t.toString());

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }

    public void cancelOrder(String api, ApiType apiTypes, String authKey, String userId, CancelOrder cancelOrder)
    {
        apiTypeVariable = apiTypes;
        Retrofit retrofit=getRetrofitClient(api);

        EzzShoppAPI ezzShoppAPI=retrofit.create(EzzShoppAPI.class);
        call=(Call<T>)ezzShoppAPI.cancelOrder(authKey,userId,cancelOrder);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t=(T)response.body();
                onResponseListner.onResponse(t, apiTypeVariable, true);

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                onResponseListner.onResponse(null, apiTypeVariable, false);

            }
        });
    }
}
