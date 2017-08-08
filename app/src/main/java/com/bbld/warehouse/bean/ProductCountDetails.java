package com.bbld.warehouse.bean;

import java.util.List;

/**
 * 库存盘点详情
 * Created by likey on 2017/7/31.
 */

public class ProductCountDetails {
    /**"status": 0,
     "mes": "成功",
     "List": []*/
    private int status;
    private String mes;
    private java.util.List<ProductCountDetailsList> List;

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

    public java.util.List<ProductCountDetailsList> getList() {
        return List;
    }

    public void setList(java.util.List<ProductCountDetailsList> list) {
        List = list;
    }

    public static class ProductCountDetailsList{
        private String BatchNumber;
        private String Count;

        public String getBatchNumber() {
            return BatchNumber;
        }

        public void setBatchNumber(String batchNumber) {
            BatchNumber = batchNumber;
        }

        public String getCount() {
            return Count;
        }

        public void setCount(String count) {
            Count = count;
        }
    }
}
