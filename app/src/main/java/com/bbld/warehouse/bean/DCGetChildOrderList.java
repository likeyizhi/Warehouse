package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/8.
 */

public class DCGetChildOrderList {
    /**"status": 0,
     "mes": "成功",
     "list": []*/
    private int status;
    private String mes;
    private List<DCGetChildOrderListlist> list;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public List<DCGetChildOrderListlist> getList() {
        return list;
    }

    public void setList(List<DCGetChildOrderListlist> list) {
        this.list = list;
    }

    public static class DCGetChildOrderListlist{
        /**"OrderId": 151,
         "OrderCode": "XSDD-107-20171102-1",
         "OrderStatus": 2,
         "OrderStatusMessage": "待发货",
         "Name": "郑州地办",
         "Phone": "13200000000",
         "AddDate": "2017-11-02T17:45:20.51",
         "ProductCategoryCount": 1,
         "ProductTotal": 48*/
        private int OrderId;
        private String OrderCode;
        private int OrderStatus;
        private String OrderStatusMessage;
        private String Name;
        private String Phone;
        private String AddDate;
        private int ProductCategoryCount;
        private int ProductTotal;

        public int getOrderId() {
            return OrderId;
        }

        public void setOrderId(int orderId) {
            OrderId = orderId;
        }

        public String getOrderCode() {
            return OrderCode;
        }

        public void setOrderCode(String orderCode) {
            OrderCode = orderCode;
        }

        public int getOrderStatus() {
            return OrderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            OrderStatus = orderStatus;
        }

        public String getOrderStatusMessage() {
            return OrderStatusMessage;
        }

        public void setOrderStatusMessage(String orderStatusMessage) {
            OrderStatusMessage = orderStatusMessage;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getPhone() {
            return Phone;
        }

        public void setPhone(String phone) {
            Phone = phone;
        }

        public String getAddDate() {
            return AddDate;
        }

        public void setAddDate(String addDate) {
            AddDate = addDate;
        }

        public int getProductCategoryCount() {
            return ProductCategoryCount;
        }

        public void setProductCategoryCount(int productCategoryCount) {
            ProductCategoryCount = productCategoryCount;
        }

        public int getProductTotal() {
            return ProductTotal;
        }

        public void setProductTotal(int productTotal) {
            ProductTotal = productTotal;
        }
    }
}
