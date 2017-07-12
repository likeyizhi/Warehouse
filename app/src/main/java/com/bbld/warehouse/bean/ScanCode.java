package com.bbld.warehouse.bean;

/**
 * 扫码查询接口
 * Created by likey on 2017/5/23.
 */

public class ScanCode {
    /**"status": 0,
     "mes": "成功",
     "Info": {}*/
    private int status;
    private String mes;
    private ScanCodeInfo Info;

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

    public ScanCodeInfo getInfo() {
        return Info;
    }

    public void setInfo(ScanCodeInfo info) {
        Info = info;
    }

    public static class ScanCodeInfo{
        /**"IsRight": 1,
         "Type": 2,
         "Code": "1110000001005",
         "SerialNumber": "",
         "BatchNumber": "",
         "Count": 1*/
        private int IsRight;
        private int Type;
        private String Code;
        private String SerialNumber;
        private String BatchNumber;
        private int Count;

        public String getSerialNumber() {
            return SerialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            SerialNumber = serialNumber;
        }

        public String getBatchNumber() {
            return BatchNumber;
        }

        public void setBatchNumber(String batchNumber) {
            BatchNumber = batchNumber;
        }

        public int getIsRight() {
            return IsRight;
        }

        public void setIsRight(int isRight) {
            IsRight = isRight;
        }

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
