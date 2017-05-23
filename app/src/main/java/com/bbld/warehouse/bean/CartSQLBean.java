package com.bbld.warehouse.bean;

/**
 * Created by likey on 2017/4/28.
 */

public class CartSQLBean {
    private String productId;
    private String productCode;
    private int proCount;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getProCount() {
        return proCount;
    }

    public void setProCount(int proCount) {
        this.proCount = proCount;
    }
}
