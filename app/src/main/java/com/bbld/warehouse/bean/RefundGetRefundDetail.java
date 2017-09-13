package com.bbld.warehouse.bean;

import java.util.List;

/**
 * 退货申请-详情
 * Created by likey on 2017/9/7.
 */

public class RefundGetRefundDetail {
    /**"status": 0,
     "mes": "成功",
     "Info": {}*/
    private int status;
    private String mes;
    private RefundGetRefundDetailInfo Info;

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

    public RefundGetRefundDetailInfo getInfo() {
        return Info;
    }

    public void setInfo(RefundGetRefundDetailInfo info) {
        Info = info;
    }

    public static class RefundGetRefundDetailInfo{
        /**"Id": "7",
         "RefundCode": "TH-20170828043911",
         "DealerName": "孙传静山东",
         "ContactPhone": "18663786652",
         "Remark": "111",
         "AuditRemark": "",
         "ProductTypeCount": "1",
         "ProductTotal": "2",
         "RefundStatus": "审核通过",
         "Date": "2017-08-28",
         "ProductList": []*/
        private String Id;
        private String RefundCode;
        private String DealerName;
        private String ContactPhone;
        private String Remark;
        private String AuditRemark;
        private String ProductTypeCount;
        private String ProductTotal;
        private String RefundStatus;
        private String Date;
        private List<RefundGetRefundDetailProductList> ProductList;

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getRefundCode() {
            return RefundCode;
        }

        public void setRefundCode(String refundCode) {
            RefundCode = refundCode;
        }

        public String getDealerName() {
            return DealerName;
        }

        public void setDealerName(String dealerName) {
            DealerName = dealerName;
        }

        public String getContactPhone() {
            return ContactPhone;
        }

        public void setContactPhone(String contactPhone) {
            ContactPhone = contactPhone;
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

        public String getProductTypeCount() {
            return ProductTypeCount;
        }

        public void setProductTypeCount(String productTypeCount) {
            ProductTypeCount = productTypeCount;
        }

        public String getProductTotal() {
            return ProductTotal;
        }

        public void setProductTotal(String productTotal) {
            ProductTotal = productTotal;
        }

        public String getRefundStatus() {
            return RefundStatus;
        }

        public void setRefundStatus(String refundStatus) {
            RefundStatus = refundStatus;
        }

        public String getDate() {
            return Date;
        }

        public void setDate(String date) {
            Date = date;
        }

        public List<RefundGetRefundDetailProductList> getProductList() {
            return ProductList;
        }

        public void setProductList(List<RefundGetRefundDetailProductList> productList) {
            ProductList = productList;
        }

        public static class RefundGetRefundDetailProductList{
            /**"ProductID": "15",
             "ProductName": "金装睿迪低糖型DHA藻油夹心软糖90粒",
             "ProductImg": "http://manager.xiuzheng.cc//UploadFile/8ae00843416f4a3b86b25bda47928f4b.jpg",
             "ProductCount": "2"*/
            private String ProductID;
            private String ProductName;
            private String ProductImg;
            private String ProductCount;

            public String getProductID() {
                return ProductID;
            }

            public void setProductID(String productID) {
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

            public String getProductCount() {
                return ProductCount;
            }

            public void setProductCount(String productCount) {
                ProductCount = productCount;
            }
        }
    }
}
