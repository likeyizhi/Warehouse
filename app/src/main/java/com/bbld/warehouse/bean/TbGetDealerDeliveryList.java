package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/6.
 */

public class TbGetDealerDeliveryList {
    /** "status": 0,
     "mes": "成功",
     "list": []*/
    private int status;
    private String mes;
    private List<TbGetDealerDeliveryListlist> list;

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

    public List<TbGetDealerDeliveryListlist> getList() {
        return list;
    }

    public void setList(List<TbGetDealerDeliveryListlist> list) {
        this.list = list;
    }

    public static class TbGetDealerDeliveryListlist{
        /**"Id": 24,
         "Name": "林红",
         "Address": "郑州京港澳高速与南三环向东5公里国际物流园嘉里物流中心",
         "Phone": "18686405711",
         "Areas": "河南省.郑州市.中原区"*/
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
