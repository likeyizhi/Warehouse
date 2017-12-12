package com.bbld.warehouse.bean;

/**
 * Created by likey on 2017/3/29.
 */

public class SouSuoDiZhi {

    private String x;
    private String y;
    private String dizhiName;
    private String dizhiAddress;

    public SouSuoDiZhi(String x, String y, String dizhiName, String dizhiAddress) {
        this.x = x;
        this.y = y;
        this.dizhiName = dizhiName;
        this.dizhiAddress = dizhiAddress;
    }

    public SouSuoDiZhi() {
        super();
    }

    public String getX() {

        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getDizhiName() {
        return dizhiName;
    }

    public void setDizhiName(String dizhiName) {
        this.dizhiName = dizhiName;
    }

    public String getDizhiAddress() {
        return dizhiAddress;
    }

    public void setDizhiAddress(String dizhiAddress) {
        this.dizhiAddress = dizhiAddress;
    }
}

