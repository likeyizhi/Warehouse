package com.bbld.warehouse.bean;

/**
 * Created by likey on 2017/5/23.
 */

public class Login {
    /**{
     "status": 0,
     "mes": "成功",
     "token":"97ef896b5f4d4519b52379a65b272d21"
     }*/
    private int status;
    private String mes;
    private String token;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
