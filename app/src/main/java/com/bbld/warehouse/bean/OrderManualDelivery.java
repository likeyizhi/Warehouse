package com.bbld.warehouse.bean;

/**
 * Created by likey on 2017/9/4.
 */

public class OrderManualDelivery {
    /**"status": 0,
     "mes": "成功"*/
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
