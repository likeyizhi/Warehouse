package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/22.
 */

public class FHDGetLogisticsList {
    /** "status": 0,
     "mes": "成功"
     "list": []*/
    private int status;
    private String mes;
    private List<FHDGetLogisticsListlist> list;

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

    public List<FHDGetLogisticsListlist> getList() {
        return list;
    }

    public void setList(List<FHDGetLogisticsListlist> list) {
        this.list = list;
    }

    public static class FHDGetLogisticsListlist{
        /**"NOIDTBLogistics": 17,
         "LogisticsName": "顺丰快递",
         "Remark": "顺丰快递",
         "DealerID": 8,
         "LKey": "shunfeng",
         "IsForbid": 0,
         "IsDelete": 0,
         "AddId": 14,
         "ModifyId": 0,
         "ModifyDate": "2017-11-22T11:26:33.03",
         "AddDate": "2017-11-22T11:26:33.03"*/
        private int NOIDTBLogistics;
        private String LogisticsName;
        private String Remark;
        private int DealerID;
        private String LKey;
        private int IsForbid;
        private int IsDelete;
        private int AddId;
        private int ModifyId;
        private String ModifyDate;
        private String AddDate;

        public int getNOIDTBLogistics() {
            return NOIDTBLogistics;
        }

        public void setNOIDTBLogistics(int NOIDTBLogistics) {
            this.NOIDTBLogistics = NOIDTBLogistics;
        }

        public String getLogisticsName() {
            return LogisticsName;
        }

        public void setLogisticsName(String logisticsName) {
            LogisticsName = logisticsName;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String remark) {
            Remark = remark;
        }

        public int getDealerID() {
            return DealerID;
        }

        public void setDealerID(int dealerID) {
            DealerID = dealerID;
        }

        public String getLKey() {
            return LKey;
        }

        public void setLKey(String LKey) {
            this.LKey = LKey;
        }

        public int getIsForbid() {
            return IsForbid;
        }

        public void setIsForbid(int isForbid) {
            IsForbid = isForbid;
        }

        public int getIsDelete() {
            return IsDelete;
        }

        public void setIsDelete(int isDelete) {
            IsDelete = isDelete;
        }

        public int getAddId() {
            return AddId;
        }

        public void setAddId(int addId) {
            AddId = addId;
        }

        public int getModifyId() {
            return ModifyId;
        }

        public void setModifyId(int modifyId) {
            ModifyId = modifyId;
        }

        public String getModifyDate() {
            return ModifyDate;
        }

        public void setModifyDate(String modifyDate) {
            ModifyDate = modifyDate;
        }

        public String getAddDate() {
            return AddDate;
        }

        public void setAddDate(String addDate) {
            AddDate = addDate;
        }
    }
}
