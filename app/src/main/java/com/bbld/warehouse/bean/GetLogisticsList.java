package com.bbld.warehouse.bean;

import java.util.List;

/**
 * 获取物流公司字典接口
 * Created by likey on 2017/5/23.
 */

public class GetLogisticsList {
    /**"status": 0,
     "mes": "成功",
     "list": []*/
    private int status;
    private String mes;
    private List<GetLogisticsListList> list;

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

    public List<GetLogisticsListList> getList() {
        return list;
    }

    public void setList(List<GetLogisticsListList> list) {
        this.list = list;
    }

    public static class GetLogisticsListList{
        /**"logisticsID": 1,
         "logisticsName": "A 物流"*/
        private int logisticsID;
        private String logisticsName;

        public int getLogisticsID() {
            return logisticsID;
        }

        public void setLogisticsID(int logisticsID) {
            this.logisticsID = logisticsID;
        }

        public String getLogisticsName() {
            return logisticsName;
        }

        public void setLogisticsName(String logisticsName) {
            this.logisticsName = logisticsName;
        }
    }
}
