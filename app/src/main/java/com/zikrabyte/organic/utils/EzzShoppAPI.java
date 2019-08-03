package com.zikrabyte.organic.utils;

import com.google.gson.JsonObject;
import com.zikrabyte.organic.api_requests.AllVeggieProducts;
import com.zikrabyte.organic.api_requests.ApplyReferral;
import com.zikrabyte.organic.api_requests.BulkAPI;
import com.zikrabyte.organic.api_requests.CancelOrder;
import com.zikrabyte.organic.api_requests.CashOnDelivery;
import com.zikrabyte.organic.api_requests.CoupenAppliedCart;
import com.zikrabyte.organic.api_requests.ForgotPassword;
import com.zikrabyte.organic.api_requests.OrderHistory;
import com.zikrabyte.organic.api_responses.allcoupens.AllCoupensResponse;
import com.zikrabyte.organic.api_responses.allveggies.AllVeggiesProductsResponse;
import com.zikrabyte.organic.api_responses.applyreferral.ApplyReferralResponse;
import com.zikrabyte.organic.api_responses.bulkapi.BulkAPIResponse;
import com.zikrabyte.organic.api_responses.cancelorder.CancelOrderResponse;
import com.zikrabyte.organic.api_responses.cod.CODResponse;
import com.zikrabyte.organic.api_responses.coupenapplied.CoupenAppliedResponse;
import com.zikrabyte.organic.api_responses.edituserdetails.EditUserDetailsResponse;
import com.zikrabyte.organic.api_responses.forgotpassword.ForgotPasswordResponse;
import com.zikrabyte.organic.api_responses.login.LogInResponse;
import com.zikrabyte.organic.api_responses.orderhistory.OrderHistoryResponse;
import com.zikrabyte.organic.api_responses.searchresults.SearchProductsResponse;
import com.zikrabyte.organic.api_requests.AddAddress;
import com.zikrabyte.organic.api_requests.AddToCart;
import com.zikrabyte.organic.api_requests.AllCoupens;
import com.zikrabyte.organic.api_requests.AllGroceryProducts;
import com.zikrabyte.organic.api_requests.CheckUser;
import com.zikrabyte.organic.api_requests.EditAddress;
import com.zikrabyte.organic.api_requests.ItemDescription;
import com.zikrabyte.organic.api_requests.LogIn;
import com.zikrabyte.organic.api_requests.Register;
import com.zikrabyte.organic.api_requests.RemoveAddress;
import com.zikrabyte.organic.api_requests.RemoveFromCart;
import com.zikrabyte.organic.api_requests.Search;
import com.zikrabyte.organic.api_requests.SendOTP;
import com.zikrabyte.organic.api_requests.VerifyOTP;
import com.zikrabyte.organic.api_requests.ViewAddress;
import com.zikrabyte.organic.api_requests.ViewCart;
import com.zikrabyte.organic.api_responses.CheckUserResponse;
import com.zikrabyte.organic.api_responses.addaddress.AddAddressResponse;
import com.zikrabyte.organic.api_responses.addtocart.AddToCartResponse;
import com.zikrabyte.organic.api_responses.all_groceryproducts.AllGroceryProductsResponse;
import com.zikrabyte.organic.api_responses.apartmentlist.ApartmentListResponse;
import com.zikrabyte.organic.api_responses.categories.CategoriesResponse;
import com.zikrabyte.organic.api_responses.deliverydate.DeliveryDateResponse;
import com.zikrabyte.organic.api_responses.editaddress.EditAddressResponse;
import com.zikrabyte.organic.api_responses.edituserprofile.EditUserProfileResponse;
import com.zikrabyte.organic.api_responses.itemdescription.ItemDescriptionResponse;
import com.zikrabyte.organic.api_responses.register.UserRegistrationResponse;
import com.zikrabyte.organic.api_responses.removeaddress.RemoveAddressResponse;
import com.zikrabyte.organic.api_responses.removefromcart.RemoveFromCartResponse;
import com.zikrabyte.organic.api_responses.sendotp.SendOTPResponse;
import com.zikrabyte.organic.api_responses.verifyotp.VerifyOTPResponse;
import com.zikrabyte.organic.api_responses.viewAddress.ViewAddressResponse;
import com.zikrabyte.organic.api_responses.viewcart.ViewCartResponse;
import com.zikrabyte.organic.api_responses.viewuserprofile.ViewUserProfileResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Krish on 08-03-2018.
 */

public interface EzzShoppAPI {

    @POST("check.php")
    Call<CheckUserResponse>checkUser(@Body CheckUser user);

    @POST("login.php")
    Call<LogInResponse>logIn(@Body LogIn logIn);

    @POST("register.php")
    Call<UserRegistrationResponse>registerUser(@Body Register register);

    @POST("get_otp.php")
    Call<SendOTPResponse>sendOTP(@Body SendOTP sendOTP);

    @POST("verify_otp.php")
    Call<VerifyOTPResponse>verifyOTP(@Body VerifyOTP verifyOTP);

    @POST("view_categories.php")
    Call<CategoriesResponse> getCategories(@Header("authkey") String authkey,@Header("userid") String userid);

    @POST("view_products.php")
    Call<AllGroceryProductsResponse> getAllGroceryProducts(@Header("authkey") String authkey, @Header("userid") String userid, @Body AllGroceryProducts allGroceryProducts);

    @POST("view_products.php")
    Call<AllVeggiesProductsResponse> getAllVeggieProducts(@Header("authkey") String authkey, @Header("userid") String userid, @Body AllVeggieProducts allGroceryProducts);

    @POST("view_products.php")
    Call<ItemDescriptionResponse> getItemDescription(@Header("authkey") String authkey, @Header("userid") String userid, @Body ItemDescription itemDescription);

    @POST("view_cart.php")
    Call<ViewCartResponse> viewCartItems(@Header("authkey") String authkey, @Header("userid") String userid, @Body ViewCart viewCart);

    @POST("remove_from_cart.php")
    Call<RemoveFromCartResponse> removeFromCart(@Header("authkey") String authkey, @Header("userid") String userid, @Body RemoveFromCart removeFromCart);

    @POST("add_to_cart.php")
    Call<AddToCartResponse> addToCart(@Header("authkey") String authkey, @Header("userid") String userid, @Body AddToCart addToCart);

    @POST("view_apartments.php")
    Call<ApartmentListResponse> viewApartments(@Header("authkey") String authkey, @Header("userid") String userid);

    @POST("add_address.php")
    Call<AddAddressResponse> addAddress(@Header("authkey") String authkey, @Header("userid") String userid, @Body AddAddress addAddress);

    @POST("edit_address.php")
    Call<EditAddressResponse> editAddress(@Header("authkey") String authkey, @Header("userid") String userid, @Body EditAddress editAddress);

    @POST("view_address.php")
    Call<ViewAddressResponse> viewAddressList(@Header("authkey") String authkey, @Header("userid") String userid, @Body ViewAddress viewAddress);

    @POST("delete_address.php")
    Call<RemoveAddressResponse> removeAddress(@Header("authkey") String authkey, @Header("userid") String userid, @Body RemoveAddress removeAddress);

    @POST("profile.php")
    @FormUrlEncoded
    Call<ViewUserProfileResponse> viewUserProfile(@Header("authkey") String authkey, @Header("userid") String userid,@Field("user_id") String user_id);

    @POST("profile.php")
    @Multipart
    Call<EditUserProfileResponse> editUserProfile(@Header("authkey") String authkey, @Header("userid") String userid,
                                                  @Part("user_id") RequestBody user_id,
                                                  @Part("fname") RequestBody fname,
                                                  @Part("email") RequestBody email,
                                                  @Part("phone") RequestBody phone,
                                                  @Part MultipartBody.Part picture);

    @POST("profile.php")
    @FormUrlEncoded
    Call<EditUserDetailsResponse> editUserProfileDetails(@Header("authkey") String authkey, @Header("userid") String userid,
                                                         @Field("user_id") String user_id,
                                                         @Field("fname") String fname,
                                                         @Field("email") String email,
                                                         @Field("phone") String phone,
                                                         @Field("old_picture") String picture);

    @POST("coupons.php")
    Call<AllCoupensResponse> getAllCoupens(@Header("authkey") String authkey, @Header("userid") String userid, @Body AllCoupens allCoupens);

    @POST("delivery_days.php")
    Call<DeliveryDateResponse> getDeliveryDays(@Header("authkey") String authkey, @Header("userid") String userid);

    @POST("search_product.php")
    Call<SearchProductsResponse> searchProduct(@Header("authkey") String authkey, @Header("userid") String userid, @Body Search search);

    @POST("view_cart.php")
    Call<CoupenAppliedResponse> viewCartItemsAfterApplyingCoupen(@Header("authkey") String authkey, @Header("userid") String userid, @Body CoupenAppliedCart appliedCart);

    @POST("orders.php")
    Call<OrderHistoryResponse> getOrderHistory(@Header("authkey") String authkey, @Header("userid") String userid, @Body OrderHistory orderHistory);

    @POST("referral_apply.php")
    Call<ApplyReferralResponse> applyReferralCode(@Header("authkey") String authkey, @Header("userid") String userid, @Body ApplyReferral applyReferral);

    @POST("forgot.php")
    Call<ForgotPasswordResponse> forgotPassword(@Body ForgotPassword forgotPassword);

    @POST("cod.php")
    Call<CODResponse> cashOnDelivery(@Header("authkey") String authkey, @Header("userid") String userid, @Body CashOnDelivery cashOnDelivery);


    @POST("cancel_order.php")
    Call<CancelOrderResponse> cancelOrder(@Header("authkey") String authkey, @Header("userid") String userid, @Body CancelOrder cancelOrder);

    @Headers("Content-Type: application/json")
    @POST("bulk_cart.php")
    Call<BulkAPIResponse> bulkCartAPI(@Header("authkey") String authkey, @Header("userid") String userid,@Body JsonObject body);
}








