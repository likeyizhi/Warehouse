package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/8/21.
 */

public class StorageCodeList {
    /**"status": 0,
     "mes": "成功",
     "Info": {}*/
    private int status;
    private String mes;
    private StorageCodeListInfo Info;

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

    public StorageCodeListInfo getInfo() {
        return Info;
    }

    public void setInfo(StorageCodeListInfo info) {
        Info = info;
    }

    public static class StorageCodeListInfo{
        private List<StorageCodeListCodeList> CodeList;

        public List<StorageCodeListCodeList> getCodeList() {
            return CodeList;
        }

        public void setCodeList(List<StorageCodeListCodeList> codeList) {
            CodeList = codeList;
        }

        public static class StorageCodeListCodeList{
            /**"Name": "修正牌睿迪软胶囊（30粒）",
             "BarCode": "10812020170833",
             "SerialNumber": "081901",
             "BatchNumber": "1-1"*/
            private String Name;
            private String BarCode;
            private String SerialNumber;
            private String BatchNumber;

            public String getName() {
                return Name;
            }

            public void setName(String name) {
                Name = name;
            }

            public String getBarCode() {
                return BarCode;
            }

            public void setBarCode(String barCode) {
                BarCode = barCode;
            }

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
        }
    }
}
