package com.bbld.warehouse.bean;

/**
 * 首页接口
 * Created by likey on 2017/5/23.
 */

public class IndexInfo {
    /**    {
     "status": 0,
     "mes": "成功",
     "type": 1,
     "typeinfo": "类型[1=总部,2=经销商]",
     "dck": 0,
     "yck": 1,
     "dsh": 0
     "qtckdck": 1
     "zdps": 1,
     "zdsh": 1
     }*/
    private int status;
    private String mes;
    private int type;
    private String typeinfo;
    private int dck;
    private int yck;
    private int dsh;
    private int qtckdck;
    private int zdps;
    private int zdsh;

    public int getZdps() {
        return zdps;
    }

    public void setZdps(int zdps) {
        this.zdps = zdps;
    }

    public int getZdsh() {
        return zdsh;
    }

    public void setZdsh(int zdsh) {
        this.zdsh = zdsh;
    }

    public int getQtckdck() {
        return qtckdck;
    }

    public void setQtckdck(int qtckdck) {
        this.qtckdck = qtckdck;
    }

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeinfo() {
        return typeinfo;
    }

    public void setTypeinfo(String typeinfo) {
        this.typeinfo = typeinfo;
    }

    public int getDck() {
        return dck;
    }

    public void setDck(int dck) {
        this.dck = dck;
    }

    public int getYck() {
        return yck;
    }

    public void setYck(int yck) {
        this.yck = yck;
    }

    public int getDsh() {
        return dsh;
    }

    public void setDsh(int dsh) {
        this.dsh = dsh;
    }
}
