package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/12/4.
 */

public class GetEndCustomerInfo {
    private int status;
    private String mes;
    private String Contacts;
    private String ContactPhone;
    private String Name;
    private String Address;
    private int DealerId;
    private String x;
    private String y;
    private List<GetEndCustomerInfoDealerList> DealerList;

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

    public int getDealerId() {
        return DealerId;
    }

    public void setDealerId(int dealerId) {
        DealerId = dealerId;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public List<GetEndCustomerInfoDealerList> getDealerList() {
        return DealerList;
    }

    public void setDealerList(List<GetEndCustomerInfoDealerList> dealerList) {
        DealerList = dealerList;
    }

    public static class GetEndCustomerInfoDealerList{
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

        public GetEndCustomerInfoDealerList(int id, String name) {
            Id = id;
            Name = name;
        }
    }
}
