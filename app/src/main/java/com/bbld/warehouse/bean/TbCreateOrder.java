package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/6.
 */

public class TbCreateOrder {
    /**"orderId": 1,
     "deliveryId": 1,
     "dealerRemark": "tibaodingdan",
     "productList": []*/
    private int orderId;
    private int deliveryId;
    private String dealerRemark;
    private List<TbCreateOrderproductList> productList;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getDealerRemark() {
        return dealerRemark;
    }

    public void setDealerRemark(String dealerRemark) {
        this.dealerRemark = dealerRemark;
    }

    public List<TbCreateOrderproductList> getProductList() {
        return productList;
    }

    public void setProductList(List<TbCreateOrderproductList> productList) {
        this.productList = productList;
    }

    public static class TbCreateOrderproductList{
        /** "ProductId": 2,
         "ProductAmount": 2000,
         "ProductRemark": "1222"*/
        private int ProductId;
        private int ProductAmount;
        private String ProductRemark;

        public int getProductId() {
            return ProductId;
        }

        public void setProductId(int productId) {
            ProductId = productId;
        }

        public int getProductAmount() {
            return ProductAmount;
        }

        public void setProductAmount(int productAmount) {
            ProductAmount = productAmount;
        }

        public String getProductRemark() {
            return ProductRemark;
        }

        public void setProductRemark(String productRemark) {
            ProductRemark = productRemark;
        }
    }
}
