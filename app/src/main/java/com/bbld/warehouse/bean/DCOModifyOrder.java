package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/14.
 */

public class DCOModifyOrder {
    /**"orderId": 1,
     "headRemark": "更新订单",
     "productList": []*/
    private int orderId;
    private String headRemark;
    private List<DCOModifyOrderproductList> productList;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getHeadRemark() {
        return headRemark;
    }

    public void setHeadRemark(String headRemark) {
        this.headRemark = headRemark;
    }

    public List<DCOModifyOrderproductList> getProductList() {
        return productList;
    }

    public void setProductList(List<DCOModifyOrderproductList> productList) {
        this.productList = productList;
    }

    public static class DCOModifyOrderproductList{
        /**"ProductId": 1,
         "ProductAmount": 100,
         "ProductRemark": "1222",
         "GiveAmount": 100*/
        private int ProductId;
        private int ProductAmount;
        private String ProductRemark;
        private int GiveAmount;

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

        public int getGiveAmount() {
            return GiveAmount;
        }

        public void setGiveAmount(int giveAmount) {
            GiveAmount = giveAmount;
        }

        public DCOModifyOrderproductList(int productId, int productAmount, String productRemark, int giveAmount) {
            ProductId = productId;
            ProductAmount = productAmount;
            ProductRemark = productRemark;
            GiveAmount = giveAmount;
        }
    }
}
