package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/22.
 */

public class FHDGetInvoiceLogisticsList {
    private int status;
    private String mes;
    private List<FHDGetInvoiceLogisticsListlist> list;

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

    public List<FHDGetInvoiceLogisticsListlist> getList() {
        return list;
    }

    public void setList(List<FHDGetInvoiceLogisticsListlist> list) {
        this.list = list;
    }

    public static class FHDGetInvoiceLogisticsListlist{
        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public String getLogisticsName() {
            return LogisticsName;
        }

        public void setLogisticsName(String logisticsName) {
            LogisticsName = logisticsName;
        }

        public String getLogisticalCode() {
            return LogisticalCode;
        }

        public void setLogisticalCode(String logisticalCode) {
            LogisticalCode = logisticalCode;
        }

        public String getDeliveryName() {
            return DeliveryName;
        }

        public void setDeliveryName(String deliveryName) {
            DeliveryName = deliveryName;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getPhone() {
            return Phone;
        }

        public void setPhone(String phone) {
            Phone = phone;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String remark) {
            Remark = remark;
        }

        /** "Id": 59,
         "LogisticsName": "中通快递",
         "LogisticalCode": "457011853574",
         "DeliveryName": "郑州地办",
         "Address": "郑州市区",
         "Phone": "13200000000",
         "Remark": "zto"*/
        private int Id;
        private String LogisticsName;
        private String LogisticalCode;
        private String DeliveryName;
        private String Address;
        private String Phone;
        private String Remark;
    }
}
