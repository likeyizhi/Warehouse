package com.bbld.warehouse.bean;

import java.util.List;

/**
 * 库存查询实体类
 * Created by likey on 2017/6/7.
 */

public class ProductCountList {
    /**"status": 0,
     "mes": "成功",
     "List": []*/
    private int status;
    private String mes;
    private java.util.List<ProductCountListList> List;

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

    public java.util.List<ProductCountListList> getList() {
        return List;
    }

    public void setList(java.util.List<ProductCountListList> list) {
        List = list;
    }

    public static class ProductCountListList{
        /**"ProductID": "11",
         "ProductName": "商品名称",
         "ProductImg": "http://182.92.183.143:8060//UploadFile/758deb95deb74b00a5d8b693400c16af.png",
         "ProductSpec": "规格",
         "Unit": "箱",
         "Count": "0"*/
        private String ProductID;
        private String ProductName;
        private String ProductImg;
        private String ProductSpec;
        private String Unit;
        private String Count;

        public String getProductID() {
            return ProductID;
        }

        public void setProductID(String productID) {
            ProductID = productID;
        }

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String productName) {
            ProductName = productName;
        }

        public String getProductImg() {
            return ProductImg;
        }

        public void setProductImg(String productImg) {
            ProductImg = productImg;
        }

        public String getProductSpec() {
            return ProductSpec;
        }

        public void setProductSpec(String productSpec) {
            ProductSpec = productSpec;
        }

        public String getUnit() {
            return Unit;
        }

        public void setUnit(String unit) {
            Unit = unit;
        }

        public String getCount() {
            return Count;
        }

        public void setCount(String count) {
            Count = count;
        }
    }
}
