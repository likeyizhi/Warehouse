package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/22.
 */

public class FHDGetCboLogistics {
    private int status;
    private String mes;
    private List<FHDGetCboLogisticslist> list;

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

    public List<FHDGetCboLogisticslist> getList() {
        return list;
    }

    public void setList(List<FHDGetCboLogisticslist> list) {
        this.list = list;
    }

    public static class FHDGetCboLogisticslist{
        private int Id;
        private String Name;

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }
    }
}
