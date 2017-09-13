package com.bbld.warehouse.bean;

import java.util.List;

/**
 * 退货申请列表
 * Created by likey on 2017/9/7.
 */

public class RefundGetRefundList {
    /**"status": 0,
     "mes": "成功",
     "list": []*/
    private int status;
    private String mes;
    private List<RefundGetRefundListlist> list;

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

    public List<RefundGetRefundListlist> getList() {
        return list;
    }

    public void setList(List<RefundGetRefundListlist> list) {
        this.list = list;
    }

    public static class RefundGetRefundListlist{
        /**"Id": 7,
         "RefundCode": "TH-20170828043911",
         "RefundStatus": 2,
         "RefundMessage": "审核通过",
         "Remark": "111",
         "AuditRemark": null,
         "AddDate": "2017-08-28T16:39:11.363",
         "ProductTypeCount": 1,
         "ProductTotal": 2,
         "DealerName": "孙传静山东",
         "Phone": "18663786652",
         "Date": "2017-08-28"*/
        private int Id;
        private String RefundCode;
        private int RefundStatus;
        private String RefundMessage;
        private String Remark;
        private String AuditRemark;
        private String AddDate;
        private String ProductTypeCount;
        private int ProductTotal;
        private String DealerName;
        private String Phone;
        private String Date;

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public String getRefundCode() {
            return RefundCode;
        }

        public void setRefundCode(String refundCode) {
            RefundCode = refundCode;
        }

        public int getRefundStatus() {
            return RefundStatus;
        }

        public void setRefundStatus(int refundStatus) {
            RefundStatus = refundStatus;
        }

        public String getRefundMessage() {
            return RefundMessage;
        }

        public void setRefundMessage(String refundMessage) {
            RefundMessage = refundMessage;
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

        public String getAddDate() {
            return AddDate;
        }

        public void setAddDate(String addDate) {
            AddDate = addDate;
        }

        public String getProductTypeCount() {
            return ProductTypeCount;
        }

        public void setProductTypeCount(String productTypeCount) {
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
    }
}
