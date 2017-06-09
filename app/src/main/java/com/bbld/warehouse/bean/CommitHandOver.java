package com.bbld.warehouse.bean;

/**
 * 市场交接--保存
 * Created by likey on 2017/6/8.
 */

public class CommitHandOver {
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
