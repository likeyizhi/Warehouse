package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/22.
 */

public class FHDGetLogisticsInfo {
    /**"status": 0,
     "mes": "获取快递信息成功",
     "info": []*/
    private int status;
    private String mes;
    private List<FHDGetLogisticsInfoinfo> info;

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

    public List<FHDGetLogisticsInfoinfo> getInfo() {
        return info;
    }

    public void setInfo(List<FHDGetLogisticsInfoinfo> info) {
        this.info = info;
    }

    public static class FHDGetLogisticsInfoinfo{
        private String time;
        private String status;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
