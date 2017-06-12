package com.bbld.warehouse.bean;

import java.util.List;

/**
 * 市场交接
 * Created by likey on 2017/6/8.
 */

public class HandoverList {
    /** "status": 0,
     "mes": "成功",
     "list": []*/
    private int status;
    private String mes;
    private List<HandoverListlist> list;

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

    public List<HandoverListlist> getList() {
        return list;
    }

    public void setList(List<HandoverListlist> list) {
        this.list = list;
    }

    public static class HandoverListlist{
        /**"HandOverID": "2",
         "HandoverCode": "JJD-b6b4c5e89",
         "jjr": "经销商1",
         "zcr": "",
         "Status": "Status",
         "Remark": "测试添加交接单",
         "Date": "2017-06-06",
         "Edit": 1*/
        private String HandOverID;
        private String HandoverCode;
        private String jjr;
        private String zcr;
        private String Status;
        private String Remark;
        private String Date;
        private int Edit;

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }

        public String getHandOverID() {
            return HandOverID;
        }

        public void setHandOverID(String handOverID) {
            HandOverID = handOverID;
        }

        public String getHandoverCode() {
            return HandoverCode;
        }

        public void setHandoverCode(String handoverCode) {
            HandoverCode = handoverCode;
        }

        public String getJjr() {
            return jjr;
        }

        public void setJjr(String jjr) {
            this.jjr = jjr;
        }

        public String getZcr() {
            return zcr;
        }

        public void setZcr(String zcr) {
            this.zcr = zcr;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String remark) {
            Remark = remark;
        }

        public String getDate() {
            return Date;
        }

        public void setDate(String date) {
            Date = date;
        }

        public int getEdit() {
            return Edit;
        }

        public void setEdit(int edit) {
            Edit = edit;
        }
    }
}
