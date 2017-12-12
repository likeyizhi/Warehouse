package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/12/4.
 */

public class GetParentDealerForEndCustomer {
    private int status;
    private String mes;
    private List<GetParentDealerForEndCustomerDealerList> DealerList;

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

    public List<GetParentDealerForEndCustomerDealerList> getDealerList() {
        return DealerList;
    }

    public void setDealerList(List<GetParentDealerForEndCustomerDealerList> dealerList) {
        DealerList = dealerList;
    }

    public static class GetParentDealerForEndCustomerDealerList{
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
