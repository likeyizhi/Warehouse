package com.bbld.warehouse.bean;

/**
 * 自动升级
 * Created by likey on 2017/4/27.
 */

public class VersionAndroid {
    /** "version": "1.3",
     "url": "http://182.92.183.143:8050/androidApk/mmjh_yhd-1.3-3cs03.apk",
     "status": 0,
     "mes": "操作成功",
     */
    private String version;
    private String url;
    private int status;
    private String mes;

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
