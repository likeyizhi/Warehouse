package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/22.
 */

public class StorageGetSaleList {
    /**"status": 0,
     "mes": "成功",
     "list": []*/
    private int status;
    private String mes;
    private List<StorageGetSaleListlist> list;

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

    public List<StorageGetSaleListlist> getList() {
        return list;
    }

    public void setList(List<StorageGetSaleListlist> list) {
        this.list = list;
    }

    public static class StorageGetSaleListlist{
        /** "BarCode": "13812021550201",
         "LinkName": "刘建素",
         "LinkPhone": "13716974107",
         "Name": "优智DHA藻油凝胶糖果（90粒550mg）",
         "AddDate": "2017-11-21T15:50:31.247",
         "SerialNumber": "2",
         "BatchNumber": "1111333"*/
        private String BarCode;
        private String LinkName;
        private String LinkPhone;
        private String Name;
        private String AddDate;
        private String SerialNumber;
        private String BatchNumber;

        public String getBarCode() {
            return BarCode;
        }

        public void setBarCode(String barCode) {
            BarCode = barCode;
        }

        public String getLinkName() {
            return LinkName;
        }

        public void setLinkName(String linkName) {
            LinkName = linkName;
        }

        public String getLinkPhone() {
            return LinkPhone;
        }

        public void setLinkPhone(String linkPhone) {
            LinkPhone = linkPhone;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getAddDate() {
            return AddDate;
        }

        public void setAddDate(String addDate) {
            AddDate = addDate;
        }

        public String getSerialNumber() {
            return SerialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            SerialNumber = serialNumber;
        }

        public String getBatchNumber() {
            return BatchNumber;
        }

        public void setBatchNumber(String batchNumber) {
            BatchNumber = batchNumber;
        }
    }
}
