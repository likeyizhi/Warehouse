package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/16.
 */

public class BarcodeConnectWatchHM {
    /**"status": 0,
     "mes": "成功",
     "list": []*/
    private int status;
    private String mes;
    private List<WatchHMlist> list;

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

    public List<WatchHMlist> getList() {
        return list;
    }

    public void setList(List<WatchHMlist> list) {
        this.list = list;
    }

    public static class WatchHMlist{
        /**"BarCode": "10812020170740"*/
        private String BarCode;

        public String getBarCode() {
            return BarCode;
        }

        public void setBarCode(String barCode) {
            BarCode = barCode;
        }
    }
}
