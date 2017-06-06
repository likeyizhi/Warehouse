package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/6/5.
 */

public class GetTypeList {
    /**"status": 0,
     "mes": "成功",
     "list": []*/
    private int status;
    private String mes;
    private List<GetTypeListList> list;

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

    public List<GetTypeListList> getList() {
        return list;
    }

    public void setList(List<GetTypeListList> list) {
        this.list = list;
    }

    public static class GetTypeListList{
        /**"ID": "2",
         "Name": "非正常出库"*/
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
