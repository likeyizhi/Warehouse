package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/14.
 */

public class FHDGetAddOtherProviceFHDInfo {
    /**"status": 0,
     "OrderId": 151,
     "OrderCode": "XSDD-107-20171102-1",
     "DealerName": "郑州地办",
     "DealerRemark": "提报订单郑州地办",
     "HeaderRemark": "",
     "DealerWarehouseList": []
     "ReceiveDealerList": []*/
    private int status;
    private int OrderId;
    private String OrderCode;
    private String DealerName;
    private String DealerRemark;
    private String HeaderRemark;
    private List<FHDGetAddOtherProviceFHDInfoD> DealerWarehouseList;
    private List<FHDGetAddOtherProviceFHDInfoR> ReceiveDealerList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public List<FHDGetAddOtherProviceFHDInfoD> getDealerWarehouseList() {
        return DealerWarehouseList;
    }

    public void setDealerWarehouseList(List<FHDGetAddOtherProviceFHDInfoD> dealerWarehouseList) {
        DealerWarehouseList = dealerWarehouseList;
    }

    public List<FHDGetAddOtherProviceFHDInfoR> getReceiveDealerList() {
        return ReceiveDealerList;
    }

    public void setReceiveDealerList(List<FHDGetAddOtherProviceFHDInfoR> receiveDealerList) {
        ReceiveDealerList = receiveDealerList;
    }

    public static class FHDGetAddOtherProviceFHDInfoD{
        /**"Id": 21,
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

    public static class FHDGetAddOtherProviceFHDInfoR{
        /**"Id": 21,
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
