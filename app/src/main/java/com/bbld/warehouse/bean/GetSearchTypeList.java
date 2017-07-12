package com.bbld.warehouse.bean;

import java.util.List;

/**
 * 出入库更改筛选
 * Created by likey on 2017/6/12.
 */

public class GetSearchTypeList {
    /**"status": 0,
     "mes": "成功",
     "list": []*/
    private int status;
    private String mes;
    private List<GetSearchTypeListlist> list;

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

    public List<GetSearchTypeListlist> getList() {
        return list;
    }

    public void setList(List<GetSearchTypeListlist> list) {
        this.list = list;
    }

    public static class GetSearchTypeListlist{
        /** "ID": "0",
         "Name": "全部"*/
        private String ID;
        private String Name;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }
    }
}
