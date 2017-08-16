package com.bbld.warehouse.update;

/**
 * 更新信息(JSON)
 */
public class UpdateInfo {
    public int status; // 错误代码
    public String versionName;
    public String desc;
    public String url;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //    @Override public String toString() {
//        return "当前版本: " + data.curVersion + ", 下载地址: " + data.appURL + ", 描述信息: " + data.description
//                + ", 最低版本: " + data.minVersion + ", 应用代称: " + data.appName
//                + ", 错误代码: " + error_code + ", 错误信息: " + error_msg;
//    }
}
