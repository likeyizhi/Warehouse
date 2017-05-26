package com.bbld.warehouse.bean;

/**
 *  增加物流信息接口
 * Created by likey on 2017/5/23.
 */

public class AddOrderLogisticsInfo {
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
