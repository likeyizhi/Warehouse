package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/13.
 */

public class FHDCreateFHD {
    /**"fhdId": 1,
     "orderId": 1,
     "Remark": "这里是备注",
     "warehouseId": 1,
     "isProvice": 0,
     "dealerId": 1,
     "deliveryId": 1,
     "fhdProductList": []*/
    private int fhdId;
    private int orderId;
    private String Remark;
    private int warehouseId;
    private int isProvice;
    private int dealerId;
    private int deliveryId;
    private List<DCandDGC> fhdProductList;

    public int getFhdId() {
        return fhdId;
    }

    public void setFhdId(int fhdId) {
        this.fhdId = fhdId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getIsProvice() {
        return isProvice;
    }

    public void setIsProvice(int isProvice) {
        this.isProvice = isProvice;
    }

    public int getDealerId() {
        return dealerId;
    }

    public void setDealerId(int dealerId) {
        this.dealerId = dealerId;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public List<DCandDGC> getFhdProductList() {
        return fhdProductList;
    }

    public void setFhdProductList(List<DCandDGC> fhdProductList) {
        this.fhdProductList = fhdProductList;
    }
}
