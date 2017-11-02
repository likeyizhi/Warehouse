package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/1.
 */

public class GetOrderTbList {
    /**"status": 0,
     "mes": "成功",
     "list": []*/
    private int status;
    private String mes;
    private List<GetOrderTbListlist> list;

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

    public List<GetOrderTbListlist> getList() {
        return list;
    }

    public void setList(List<GetOrderTbListlist> list) {
        this.list = list;
    }

    public static class GetOrderTbListlist{
        /**"OrderId": 150,
         "OrderCode": "XSDD-8-20170926-2",
         "OrderStatus": 2,
         "OrderStatusMessage": "待发货",
         "Name": "林红",
         "Phone": "18686405711",
         "AddDate": "2017-09-26T10:05:01.823",
         "ProductCategoryCount": 1,
         "ProductTotal": 16*/
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
