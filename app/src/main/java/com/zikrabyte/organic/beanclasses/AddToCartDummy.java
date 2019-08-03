package com.zikrabyte.organic.beanclasses;

/**
 * Created by KRISH on 4/6/2018.
 */

public class AddToCartDummy {

    String PriductId,ProductName,ProductCost,PriductQuantity,PriductQuantityType,PriductImage,ProductSubTotal,maxQuantity;

    public AddToCartDummy(String priductId, String productName, String productCost, String priductQuantity, String priductQuantityType, String priductImage, String productSubTotal, String maxQuantity) {
        PriductId = priductId;
        ProductName = productName;
        ProductCost = productCost;
        PriductQuantity = priductQuantity;
        PriductQuantityType = priductQuantityType;
        PriductImage = priductImage;
        ProductSubTotal = productSubTotal;
        this.maxQuantity = maxQuantity;
    }

    public String getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(String maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public String getPriductId() {
        return PriductId;
    }

    public void setPriductId(String priductId) {
        PriductId = priductId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductCost() {
        return ProductCost;
    }

    public void setProductCost(String productCost) {
        ProductCost = productCost;
    }

    public String getPriductQuantity() {
        return PriductQuantity;
    }

    public void setPriductQuantity(String priductQuantity) {
        PriductQuantity = priductQuantity;
    }

    public String getPriductQuantityType() {
        return PriductQuantityType;
    }

    public void setPriductQuantityType(String priductQuantityType) {
        PriductQuantityType = priductQuantityType;
    }

    public String getPriductImage() {
        return PriductImage;
    }

    public void setPriductImage(String priductImage) {
        PriductImage = priductImage;
    }

    public String getProductSubTotal() {
        return ProductSubTotal;
    }

    public void setProductSubTotal(String productSubTotal) {
        ProductSubTotal = productSubTotal;
    }
}
