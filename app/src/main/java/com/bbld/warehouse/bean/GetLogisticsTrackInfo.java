package com.bbld.warehouse.bean;

import java.util.List;

/**
 * 物流跟踪查询接口
 * Created by likey on 2017/5/23.
 */

public class GetLogisticsTrackInfo {
    /**"status": 0,
     "mes": "成功",
     "list": []*/
    private int status;
    private String mes;
    private List<GetLogisticsTrackInfoList> list;

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

    public List<GetLogisticsTrackInfoList> getList() {
        return list;
    }

    public void setList(List<GetLogisticsTrackInfoList> list) {
        this.list = list;
    }

    public static class GetLogisticsTrackInfoList{
        /**"Time": "2017 年 5 月 18 日 上午 7:29:40",
         "Content": "快件交给齐桂严，正在派送途中（联系电话：13159534373）"*/
        private String Time;
        private String Content;

        public String getTime() {
            return Time;
        }

        public void setTime(String time) {
            Time = time;
        }

        public String getContent() {
            return Content;
        }

        public void setContent(String content) {
            Content = content;
        }
    }
}
