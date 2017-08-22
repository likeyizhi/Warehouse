package com.bbld.warehouse.bean;

import java.util.List;

/**
 * 销售统计
 * Created by likey on 2017/8/21.
 */

public class SaleStatistics {
    /**"status": 0,
     "mes": "成功",
     "Info": []*/
    private int status;
    private String mes;
    private List<SaleStatisticsInfo> Info;

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

    public List<SaleStatisticsInfo> getInfo() {
        return Info;
    }

    public void setInfo(List<SaleStatisticsInfo> info) {
        Info = info;
    }

    public static class SaleStatisticsInfo{
        /**"Name": "修正牌睿迪软胶囊（30粒）",
         "Logo": "http://manager.xiuzheng.cc:81//UploadFile/241cc8bff7c6495fb3692ad29c9621bd.jpg",
         "SaleCount": "1"*/
        private String Name;
        private String Logo;
        private String SaleCount;

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getLogo() {
            return Logo;
        }

        public void setLogo(String logo) {
            Logo = logo;
        }

        public String getSaleCount() {
            return SaleCount;
        }

        public void setSaleCount(String saleCount) {
            SaleCount = saleCount;
        }
    }
}
