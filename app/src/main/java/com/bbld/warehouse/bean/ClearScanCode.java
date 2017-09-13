package com.bbld.warehouse.bean;

/**
 * 清空已扫条码
 * Created by likey on 2017/9/4.
 */

public class ClearScanCode {
    private int status;
    private String mes;

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
}
