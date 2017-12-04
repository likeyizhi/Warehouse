package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/16.
 */

public class BarcodeConnectWatchXM {
    /**"status": 0,
     "mes": "成功",
     "list": []*/
    private int status;
    private String mes;
    private List<WatchXMlist> list;

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

    public List<WatchXMlist> getList() {
        return list;
    }

    public void setList(List<WatchXMlist> list) {
        this.list = list;
    }

    public static class WatchXMlist{
        /**"Row": 1,
         "NOIDTBConnBoxCode": 8,
         "BoxCode": "10832020170800",
         "AddTime": "2017-11-16T15:04:05.773"*/
        private int Row;
        private int NOIDTBConnBoxCode;
        private String BoxCode;
        private String AddTime;

        public int getRow() {
            return Row;
        }

        public void setRow(int row) {
            Row = row;
        }

        public int getNOIDTBConnBoxCode() {
            return NOIDTBConnBoxCode;
        }

        public void setNOIDTBConnBoxCode(int NOIDTBConnBoxCode) {
            this.NOIDTBConnBoxCode = NOIDTBConnBoxCode;
        }

        public String getBoxCode() {
            return BoxCode;
        }

        public void setBoxCode(String boxCode) {
            BoxCode = boxCode;
        }

        public String getAddTime() {
            return AddTime;
        }

        public void setAddTime(String addTime) {
            AddTime = addTime;
        }
    }
}
