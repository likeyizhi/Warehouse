package com.bbld.warehouse.bean;

/**
 * Created by likey on 2017/4/27.
 */

public class VersionAndroid {
    /**    "version": "1.3",
     "url": "http://182.92.183.143:8050/androidApk/mmjh_yhd-1.3-3cs03.apk",
     "logo": "",
     "status": 0,
     "mes": "操作成功",
     "res": "{}*/
    private String version;
    private String url;
    private String logo;
    private int status;
    private String mes;
    private String res;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }
}
