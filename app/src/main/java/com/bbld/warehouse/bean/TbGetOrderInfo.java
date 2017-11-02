package com.bbld.warehouse.bean;

import java.util.List;

public class TbGetOrderInfo {
    /** "OrderId": "150",
     "OrderCode": "XSDD-8-20170926-2",
     "DealerRemark": "sjsjj",
     "HeadRemark": "",
     "DeliveryId": "24",
     "Areas": "河南省.郑州市.中原区",
     "Address": "郑州京港澳高速与南三环向东5公里国际物流园嘉里物流中心",
     "Name": "林红",
     "Phone": "18686405711",
     "OrderStatus": 3,
     "OrderStatusMessage": "订单关闭",
     "ProductList": []
     "status": 0,
     "mes": "成功"
     */
    private String OrderId;
    private String OrderCode;
    private String DealerRemark;
    private String HeadRemark;
    private String DeliveryId;
    private String Areas;
    private String Address;
    private String Name;
    private String Phone;
    private int OrderStatus;
    private String OrderStatusMessage;
    private List<TbGetOrderInfoProductList> ProductList;
    private int status;
    private String mes;

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

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getOrderCode() {
        return OrderCode;
    }

    public void setOrderCode(String orderCode) {
        OrderCode = orderCode;
    }

    public String getDealerRemark() {
        return DealerRemark;
    }

    public void setDealerRemark(String dealerRemark) {
        DealerRemark = dealerRemark;
    }

    public String getHeadRemark() {
        return HeadRemark;
    }

    public void setHeadRemark(String headRemark) {
        HeadRemark = headRemark;
    }

    public String getDeliveryId() {
        return DeliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        DeliveryId = deliveryId;
    }

    public String getAreas() {
        return Areas;
    }

    public void setAreas(String areas) {
        Areas = areas;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
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

    public List<TbGetOrderInfoProductList> getProductList() {
        return ProductList;
    }

    public void setProductList(List<TbGetOrderInfoProductList> productList) {
        ProductList = productList;
    }

    public static class TbGetOrderInfoProductList{
        private int Id;
        private int ProductId;
        private String Name;
        private String ProSpecifications;
        private int ProductAmount;
        private int GiveAmount;
        private int DeliveryCount;
        private int DeliveryGiveCount;
        private String  Remark;

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public int getProductId() {
            return ProductId;
        }

        public void setProductId(int productId) {
            ProductId = productId;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getProSpecifications() {
            return ProSpecifications;
        }

        public void setProSpecifications(String proSpecifications) {
            ProSpecifications = proSpecifications;
        }

        public int getProductAmount() {
            return ProductAmount;
        }

        public void setProductAmount(int productAmount) {
            ProductAmount = productAmount;
        }

        public int getGiveAmount() {
            return GiveAmount;
        }

        public void setGiveAmount(int giveAmount) {
            GiveAmount = giveAmount;
        }

        public int getDeliveryCount() {
            return DeliveryCount;
        }

        public void setDeliveryCount(int deliveryCount) {
            DeliveryCount = deliveryCount;
        }

        public int getDeliveryGiveCount() {
            return DeliveryGiveCount;
        }

        public void setDeliveryGiveCount(int deliveryGiveCount) {
            DeliveryGiveCount = deliveryGiveCount;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String remark) {
            Remark = remark;
        }
    }
}