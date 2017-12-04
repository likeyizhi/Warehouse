package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/29.
 */

public class CusInvoiceGetInfo {
    /**"CusCode": "FHD-ZD-8-20171129-1",
     "CusDealerId": 109,
     "WarehouseId": 21,
     "Remark": "11",
     "CusStatus": 1,
     "StatusMessage": "待出库",
     "DealerWarehouseList": []
     "CusInvoiceList": []
     "CusInvoiceProductList": []
     "status": 0,
     "mes": "成功"*/
    private String CusCode;
    private int CusDealerId;
    private int WarehouseId;
    private String Remark;
    private int CusStatus;
    private String StatusMessage;
    private List<CusInvoiceGetInfoDealerWarehouseList> DealerWarehouseList;
    private List<CusInvoiceGetInfoCusInvoiceList> CusInvoiceList;
    private List<CusInvoiceGetInfoCusInvoiceProductList> CusInvoiceProductList;
    private int status;
    private String mes;

    public String getCusCode() {
        return CusCode;
    }

    public void setCusCode(String cusCode) {
        CusCode = cusCode;
    }

    public int getCusDealerId() {
        return CusDealerId;
    }

    public void setCusDealerId(int cusDealerId) {
        CusDealerId = cusDealerId;
    }

    public int getWarehouseId() {
        return WarehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        WarehouseId = warehouseId;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public int getCusStatus() {
        return CusStatus;
    }

    public void setCusStatus(int cusStatus) {
        CusStatus = cusStatus;
    }

    public String getStatusMessage() {
        return StatusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        StatusMessage = statusMessage;
    }

    public List<CusInvoiceGetInfoDealerWarehouseList> getDealerWarehouseList() {
        return DealerWarehouseList;
    }

    public void setDealerWarehouseList(List<CusInvoiceGetInfoDealerWarehouseList> dealerWarehouseList) {
        DealerWarehouseList = dealerWarehouseList;
    }

    public List<CusInvoiceGetInfoCusInvoiceList> getCusInvoiceList() {
        return CusInvoiceList;
    }

    public void setCusInvoiceList(List<CusInvoiceGetInfoCusInvoiceList> cusInvoiceList) {
        CusInvoiceList = cusInvoiceList;
    }

    public List<CusInvoiceGetInfoCusInvoiceProductList> getCusInvoiceProductList() {
        return CusInvoiceProductList;
    }

    public void setCusInvoiceProductList(List<CusInvoiceGetInfoCusInvoiceProductList> cusInvoiceProductList) {
        CusInvoiceProductList = cusInvoiceProductList;
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

    public static class CusInvoiceGetInfoDealerWarehouseList{
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

    public static class CusInvoiceGetInfoCusInvoiceList{
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

    public static class CusInvoiceGetInfoCusInvoiceProductList{
        /**"Id": 28,
         "ProductId": 12,
         "Name": "优智DHA藻油凝胶糖果（90粒550mg）",
         "PackSpecifications": 8,
         "ProductTotal": 8,
         "ProSpecifications": "0.55g/粒*10粒/板*3板/袋*3袋/盒*8盒/件",
         "Logo": "http://manager.xiuzheng.cc//UploadFile/2eb2efd9458c481a951bbf4c4302c384.jpg"*/
        private int Id;
        private int ProductId;
        private String Name;
        private int PackSpecifications;
        private int ProductTotal;
        private String ProSpecifications;
        private String Logo;

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

        public int getPackSpecifications() {
            return PackSpecifications;
        }

        public void setPackSpecifications(int packSpecifications) {
            PackSpecifications = packSpecifications;
        }

        public int getProductTotal() {
            return ProductTotal;
        }

        public void setProductTotal(int productTotal) {
            ProductTotal = productTotal;
        }

        public String getProSpecifications() {
            return ProSpecifications;
        }

        public void setProSpecifications(String proSpecifications) {
            ProSpecifications = proSpecifications;
        }

        public String getLogo() {
            return Logo;
        }

        public void setLogo(String logo) {
            Logo = logo;
        }
    }
}
