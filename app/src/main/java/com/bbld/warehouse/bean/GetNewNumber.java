package com.bbld.warehouse.bean;

/**
 * Created by likey on 2017/6/12.
 * 创建新单号
 */

public class GetNewNumber {
    private int status;
    private String mes;
    private String Number;

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

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }
}
