package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/13.
 */

public class FHDGetAddCurrentProviceFHDInfo {
    /**"status": 0,
     "mes": "成功",
     "OrderId": 151,
     "OrderCode": "XSDD-107-20171102-1",
     "Address": "河南省.郑州市.管城回族区,郑州市区",
     "DealerId": 107,
     "DealerName": "郑州地办",
     "DealerRemark": "提报订单郑州地办",
     "HeaderRemark": "",
     "DeliveryId": 25
     "DealerWarehouseList": []*/
    private int status;
    private String mes;
    private int OrderId;
    private String OrderCode;
    private String Address;
    private int DealerId;
    private String DealerName;
    private String DealerRemark;
    private String HeaderRemark;
    private int DeliveryId;
    private List<FHDGetAddCurrentProviceFHDInfoDealerWarehouseList> DealerWarehouseList;

    public int getDeliveryId() {
        return DeliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        DeliveryId = deliveryId;
    }

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

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getDealerId() {
        return DealerId;
    }

    public void setDealerId(int dealerId) {
        DealerId = dealerId;
    }

    public String getDealerName() {
        return DealerName;
    }

    public void setDealerName(String dealerName) {
        DealerName = dealerName;
    }

    public String getDealerRemark() {
        return DealerRemark;
    }

    public void setDealerRemark(String dealerRemark) {
        DealerRemark = dealerRemark;
    }

    public String getHeaderRemark() {
        return HeaderRemark;
    }

    public void setHeaderRemark(String headerRemark) {
        HeaderRemark = headerRemark;
    }

    public List<FHDGetAddCurrentProviceFHDInfoDealerWarehouseList> getDealerWarehouseList() {
        return DealerWarehouseList;
    }

    public void setDealerWarehouseList(List<FHDGetAddCurrentProviceFHDInfoDealerWarehouseList> dealerWarehouseList) {
        DealerWarehouseList = dealerWarehouseList;
    }

    public static class FHDGetAddCurrentProviceFHDInfoDealerWarehouseList{
        /** "Id": 21,
         "Name": "河南库房"*/
        private int Id;
        private String Name;

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }
    }
}
