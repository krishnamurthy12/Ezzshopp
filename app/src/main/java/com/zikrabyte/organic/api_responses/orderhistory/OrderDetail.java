
package com.zikrabyte.organic.api_responses.orderhistory;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetail {

    @SerializedName("order_details")
    @Expose
    private OrderDetails orderDetails;
    @SerializedName("promo_details")
    @Expose
    private PromoDetails promoDetails;
    @SerializedName("product_details")
    @Expose
    private List<ProductDetail> productDetails = null;

    public OrderDetails getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }

    public PromoDetails getPromoDetails() {
        return promoDetails;
    }

    public void setPromoDetails(PromoDetails promoDetails) {
        this.promoDetails = promoDetails;
    }

    public List<ProductDetail> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<ProductDetail> productDetails) {
        this.productDetails = productDetails;
    }

}
