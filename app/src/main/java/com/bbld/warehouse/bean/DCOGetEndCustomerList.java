package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/21.
 */

public class DCOGetEndCustomerList {
    /**"status": 0,
     "mes": "成功",
     "list": []*/
    private int status;
    private String mes;
    private List<DCOGetEndCustomerListlist> list;

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

    public List<DCOGetEndCustomerListlist> getList() {
        return list;
    }

    public void setList(List<DCOGetEndCustomerListlist> list) {
        this.list = list;
    }

    public static class DCOGetEndCustomerListlist{
        /**"Id": 109,
         "Name": "郑州市区终端直营",
         "Contacts": "郑州直营1",
         "ContactPhone": "13716974107",
         "Address": "郑州市区",
         "PName": "林红河南"*/
        private int Id;
        private String Name;
        private String Contacts;
        private String ContactPhone;
        private String Address;
        private String PName;

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

        public String getContacts() {
            return Contacts;
        }

        public void setContacts(String contacts) {
            Contacts = contacts;
        }

        public String getContactPhone() {
            return ContactPhone;
        }

        public void setContactPhone(String contactPhone) {
            ContactPhone = contactPhone;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getPName() {
            return PName;
        }

        public void setPName(String PName) {
            this.PName = PName;
        }
    }
}
