package com.bbld.warehouse.bean;

/**
 * 登录接口
 * Created by likey on 2017/5/23.
 */

public class Login {
    private int status;
    private String mes;
    private String token;
    private String name;
    private int type;
    private String dealerName;
    private String warehouseName;
    private int ishandover;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public int getIshandover() {
        return ishandover;
    }

    public void setIshandover(int ishandover) {
        this.ishandover = ishandover;
    }
}
