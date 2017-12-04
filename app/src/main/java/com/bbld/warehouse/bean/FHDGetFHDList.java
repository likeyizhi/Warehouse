package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/17.
 */

public class FHDGetFHDList {
    /**"status": 0,
     "mes": "成功"
     "fhdlist": []*/
    private int status;
    private String mes;
    private List<FHDList> fhdlist;

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

    public List<FHDList> getFhdlist() {
        return fhdlist;
    }

    public void setFhdlist(List<FHDList> fhdlist) {
        this.fhdlist = fhdlist;
    }

    public static class FHDList{
        /**"Id": 71,
         "InvoiceCode": "XSFH-21-20171117-1",
         "AddDate": "2017-11-17T14:38:21.397",
         "OrderCode": "XSDD-107-20171117-1",
         "Status": 1,
         "StatusMessage": "待出库",
         "ProTCount": 1,
         "ProTotal": 8,
         "DealerName": "郑州地办",
         "DeliveryName": "郑州地办",
         "Phone": "13200000000",
         "Address": "郑州市区",
         "IsProvice": 0*/
        private int Id;
        private String InvoiceCode;
        private String AddDate;
        private String OrderCode;
        private int Status;
        private String StatusMessage;
        private int ProTCount;
        private int ProTotal;
        private String DealerName;
        private String DeliveryName;
        private String Phone;
        private String Address;
        private int IsProvice;

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public String getInvoiceCode() {
            return InvoiceCode;
        }

        public void setInvoiceCode(String invoiceCode) {
            InvoiceCode = invoiceCode;
        }

        public String getAddDate() {
            return AddDate;
        }

        public void setAddDate(String addDate) {
            AddDate = addDate;
        }

        public String getOrderCode() {
            return OrderCode;
        }

        public void setOrderCode(String orderCode) {
            OrderCode = orderCode;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int status) {
            Status = status;
        }

        public String getStatusMessage() {
            return StatusMessage;
        }

        public void setStatusMessage(String statusMessage) {
            StatusMessage = statusMessage;
        }

        public int getProTCount() {
            return ProTCount;
        }

        public void setProTCount(int proTCount) {
            ProTCount = proTCount;
        }

        public int getProTotal() {
            return ProTotal;
        }

        public void setProTotal(int proTotal) {
            ProTotal = proTotal;
        }

        public String getDealerName() {
            return DealerName;
        }

        public void setDealerName(String dealerName) {
            DealerName = dealerName;
        }

        public String getDeliveryName() {
            return DeliveryName;
        }

        public void setDeliveryName(String deliveryName) {
            DeliveryName = deliveryName;
        }

        public String getPhone() {
            return Phone;
        }

        public void setPhone(String phone) {
            Phone = phone;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public int getIsProvice() {
            return IsProvice;
        }

        public void setIsProvice(int isProvice) {
            IsProvice = isProvice;
        }
    }
}
