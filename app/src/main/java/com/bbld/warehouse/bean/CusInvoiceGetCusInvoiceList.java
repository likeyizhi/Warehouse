package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/29.
 */

public class CusInvoiceGetCusInvoiceList {
    /**"status": 0,
     "mes": "成功",
     "list": []*/
    private int status;
    private String mes;
    private List<CusInvoiceGetCusInvoiceListlist> list;

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

    public List<CusInvoiceGetCusInvoiceListlist> getList() {
        return list;
    }

    public void setList(List<CusInvoiceGetCusInvoiceListlist> list) {
        this.list = list;
    }

    public static class CusInvoiceGetCusInvoiceListlist{
        /**"Id": 28,
         "CustomerInvoiceCode": "FHD-ZD-8-20171129-1",
         "AddDate": "2017-11-29T10:00:46.073",
         "Status": 1,
         "StatusMessage": "待出库",
         "Name": "郑州市区终端直营",
         "ContactPhone": "13716974107",
         "ProductTotal": 8,
         "TypeTotal": 1*/
        private int Id;
        private String CustomerInvoiceCode;
        private String AddDate;
        private int Status;
        private String StatusMessage;
        private String Name;
        private String ContactPhone;
        private int ProductTotal;
        private int TypeTotal;

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public String getCustomerInvoiceCode() {
            return CustomerInvoiceCode;
        }

        public void setCustomerInvoiceCode(String customerInvoiceCode) {
            CustomerInvoiceCode = customerInvoiceCode;
        }

        public String getAddDate() {
            return AddDate;
        }

        public void setAddDate(String addDate) {
            AddDate = addDate;
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

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getContactPhone() {
            return ContactPhone;
        }

        public void setContactPhone(String contactPhone) {
            ContactPhone = contactPhone;
        }

        public int getProductTotal() {
            return ProductTotal;
        }

        public void setProductTotal(int productTotal) {
            ProductTotal = productTotal;
        }

        public int getTypeTotal() {
            return TypeTotal;
        }

        public void setTypeTotal(int typeTotal) {
            TypeTotal = typeTotal;
        }
    }
}
