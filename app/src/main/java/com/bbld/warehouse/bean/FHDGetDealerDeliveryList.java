package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/14.
 */

public class FHDGetDealerDeliveryList {
    /**"status": 0,
     "mes": "成功",
     "list": []*/
    private int status;
    private String mes;
    private List<FHDGetDealerDeliveryListlist> list;

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

    public List<FHDGetDealerDeliveryListlist> getList() {
        return list;
    }

    public void setList(List<FHDGetDealerDeliveryListlist> list) {
        this.list = list;
    }

    public static class FHDGetDealerDeliveryListlist{
        /**"Id": 25,
         "Name": "郑州地办",
         "Address": "郑州市区",
         "Phone": "13200000000",
         "Areas": "河南省.郑州市.管城回族区"*/
        private int Id;
        private String Name;
        private String Address;
        private String Phone;
        private String Areas;

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

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getPhone() {
            return Phone;
        }

        public void setPhone(String phone) {
            Phone = phone;
        }

        public String getAreas() {
            return Areas;
        }

        public void setAreas(String areas) {
            Areas = areas;
        }
    }
}
