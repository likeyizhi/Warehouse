package com.bbld.warehouse.bean;

import java.util.List;

/**
 * 还货出库-详情
 * Created by likey on 2017/9/8.
 */

public class GivebackGetGivebackDetail {
    /**"status": 0,
     "mes": "成功",
     "Info": {}*/
    private int status;
    private String mes;
    private GivebackGetGivebackDetailInfo Info;

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

    public GivebackGetGivebackDetailInfo getInfo() {
        return Info;
    }

    public void setInfo(GivebackGetGivebackDetailInfo info) {
        Info = info;
    }

    public static class GivebackGetGivebackDetailInfo{
        /**"Id": 7,
         "ReturnCode": "HH-20170828043911",
         "ReturnStatus": 2,
         "ReturnMessage": "审核通过",
         "Remark": "申请人备注",
         "AuditRemark": "审核人备注",
         "ProductTypeCount": 1,
         "ProductTotal": 2,
         "DealerName": "孙传静山东",
         "Phone": "18663786652",
         "Date": "2017-08-28",
         "ProductList": []*/
        private int Id;
        private String ReturnCode;
        private int ReturnStatus;
        private String ReturnMessage;
        private String Remark;
        private String AuditRemark;
        private int ProductTypeCount;
        private int ProductTotal;
        private String DealerName;
        private String Phone;
        private String Date;
        private List<GivebackGetGivebackDetailProductList> ProductList;

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public String getReturnCode() {
            return ReturnCode;
        }

        public void setReturnCode(String returnCode) {
            ReturnCode = returnCode;
        }

        public int getReturnStatus() {
            return ReturnStatus;
        }

        public void setReturnStatus(int returnStatus) {
            ReturnStatus = returnStatus;
        }

        public String getReturnMessage() {
            return ReturnMessage;
        }

        public void setReturnMessage(String returnMessage) {
            ReturnMessage = returnMessage;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String remark) {
            Remark = remark;
        }

        public String getAuditRemark() {
            return AuditRemark;
        }

        public void setAuditRemark(String auditRemark) {
            AuditRemark = auditRemark;
        }

        public int getProductTypeCount() {
            return ProductTypeCount;
        }

        public void setProductTypeCount(int productTypeCount) {
            ProductTypeCount = productTypeCount;
        }

        public int getProductTotal() {
            return ProductTotal;
        }

        public void setProductTotal(int productTotal) {
            ProductTotal = productTotal;
        }

        public String getDealerName() {
            return DealerName;
        }

        public void setDealerName(String dealerName) {
            DealerName = dealerName;
        }

        public String getPhone() {
            return Phone;
        }

        public void setPhone(String phone) {
            Phone = phone;
        }

        public String getDate() {
            return Date;
        }

        public void setDate(String date) {
            Date = date;
        }

        public List<GivebackGetGivebackDetailProductList> getProductList() {
            return ProductList;
        }

        public void setProductList(List<GivebackGetGivebackDetailProductList> productList) {
            ProductList = productList;
        }

        public static class GivebackGetGivebackDetailProductList{
            /**"ProductID": 1,
             "ProductName": "修正香皂",
             "ProductImg": "http://manager.xiuzheng.cc/UploadFile/8ae00843416f4a3b86b25bda47928f4b.jpg",
             "ProductCount": 2*/
            private int ProductID;
            private String ProductName;
            private String ProductImg;
            private int ProductCount;

            public int getProductID() {
                return ProductID;
            }

            public void setProductID(int productID) {
                ProductID = productID;
            }

            public String getProductName() {
                return ProductName;
            }

            public void setProductName(String productName) {
                ProductName = productName;
            }

            public String getProductImg() {
                return ProductImg;
            }

            public void setProductImg(String productImg) {
                ProductImg = productImg;
            }

            public int getProductCount() {
                return ProductCount;
            }

            public void setProductCount(int productCount) {
                ProductCount = productCount;
            }
        }
    }
}
