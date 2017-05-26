package com.bbld.warehouse.bean;

import java.util.List;

/**
 * 物流信息查询接口
 * Created by likey on 2017/5/23.
 */

public class GetOrderLogisticsInfo {
    /**"status": 0,
     "mes": "成功",
     "list": []*/
    private int status;
    private String mes;
    private List<GetOrderLogisticsInfoList> list;

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

    public List<GetOrderLogisticsInfoList> getList() {
        return list;
    }

    public void setList(List<GetOrderLogisticsInfoList> list) {
        this.list = list;
    }

    public static class GetOrderLogisticsInfoList{
        /**"LogisticsID": "1",
         "LogisticsName": "物流公司名称",
         "LogisticsNumber": "11100000010051"*/
        private String LogisticsID;
        private String LogisticsName;
        private String LogisticsNumber;

        public String getLogisticsID() {
            return LogisticsID;
        }

        public void setLogisticsID(String logisticsID) {
            LogisticsID = logisticsID;
        }

        public String getLogisticsName() {
            return LogisticsName;
        }

        public void setLogisticsName(String logisticsName) {
            LogisticsName = logisticsName;
        }

        public String getLogisticsNumber() {
            return LogisticsNumber;
        }

        public void setLogisticsNumber(String logisticsNumber) {
            LogisticsNumber = logisticsNumber;
        }
    }
}
