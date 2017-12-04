package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/29.
 */

public class CusInvoiceInitAddGetInfo {
    private List<CusInvoiceInitAddGetInfoDealerWarehouseList> DealerWarehouseList;
    private List<CusInvoiceInitAddGetInfoCusInvoiceList> CusInvoiceList;
    private int status;
    private String mes;

    public List<CusInvoiceInitAddGetInfoDealerWarehouseList> getDealerWarehouseList() {
        return DealerWarehouseList;
    }

    public void setDealerWarehouseList(List<CusInvoiceInitAddGetInfoDealerWarehouseList> dealerWarehouseList) {
        DealerWarehouseList = dealerWarehouseList;
    }

    public List<CusInvoiceInitAddGetInfoCusInvoiceList> getCusInvoiceList() {
        return CusInvoiceList;
    }

    public void setCusInvoiceList(List<CusInvoiceInitAddGetInfoCusInvoiceList> cusInvoiceList) {
        CusInvoiceList = cusInvoiceList;
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

    public static class CusInvoiceInitAddGetInfoDealerWarehouseList{
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

    public static class CusInvoiceInitAddGetInfoCusInvoiceList{
        /**"Id": 109,
         "Name": "郑州市区终端直营",
         "Contacts": "郑州直营1",
         "ContactPhone": "13716974107",
         "Address": "郑州市区"*/
        private int Id;
        private String Name;
        private String Contacts;
        private String ContactPhone;
        private String Address;

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

        public String getContacts() {
            return Contacts;
        }

        public void setContacts(String contacts) {
            Contacts = contacts;
        }

        public String getContactPhone() {
            return ContactPhone;
        }

        public void setContactPhone(String contactPhone) {
            ContactPhone = contactPhone;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }
    }
}
