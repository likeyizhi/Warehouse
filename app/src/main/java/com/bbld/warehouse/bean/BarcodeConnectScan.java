package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/16.
 */

public class BarcodeConnectScan {
    /**"status": 0,
     "mes": "获取条码信息成功",
     "Info": {}*/
    private int status;
    private String mes;
    private BarcodeConnectScanInfo Info;

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

    public BarcodeConnectScanInfo getInfo() {
        return Info;
    }

    public void setInfo(BarcodeConnectScanInfo info) {
        Info = info;
    }

    public static class BarcodeConnectScanInfo{
        /**"Type": 2,
         "Code": "10812020170733",
         "Count": 1*/
        private int Type;
        private String Code;
        private int Count;

        public int getType() {
            return Type;
        }

        public void setType(int type) {
            Type = type;
        }

        public String getCode() {
            return Code;
        }

        public void setCode(String code) {
            Code = code;
        }

        public int getCount() {
            return Count;
        }

        public void setCount(int count) {
            Count = count;
        }
    }
}
