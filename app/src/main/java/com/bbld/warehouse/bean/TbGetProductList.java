package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/2.
 */

public class TbGetProductList {
    /**"status": 0,
     "mes": "成功",
     "list": []*/
    private int status;
    private String mes;
    private List<TbGetProductListlist> list;

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

    public List<TbGetProductListlist> getList() {
        return list;
    }

    public void setList(List<TbGetProductListlist> list) {
        this.list = list;
    }

    public static class TbGetProductListlist{
        /**"Id": 11,
         "Name": "优智DHA藻油凝胶糖果（30粒550mg）",
         "Logo": "http://manager.xiuzheng.cc//UploadFile/43591cd0be39429f917654df472a8ee0.jpg",
         "ProSpecifications": "0.55g/粒*10粒/板*3板/盒*48盒/件"*/
        private int Id;
        private String Name;
        private String Logo;
        private String ProSpecifications;
        private int PackSpecifications;

        public int getPackSpecifications() {
            return PackSpecifications;
        }

        public void setPackSpecifications(int packSpecifications) {
            PackSpecifications = packSpecifications;
        }

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

        public String getLogo() {
            return Logo;
        }

        public void setLogo(String logo) {
            Logo = logo;
        }

        public String getProSpecifications() {
            return ProSpecifications;
        }

        public void setProSpecifications(String proSpecifications) {
            ProSpecifications = proSpecifications;
        }
    }
}
