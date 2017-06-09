package com.bbld.warehouse.bean;

/**
 * 交接订单--提交
 * Created by likey on 2017/6/8.
 */

public class HandOverSacnFinish {
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
