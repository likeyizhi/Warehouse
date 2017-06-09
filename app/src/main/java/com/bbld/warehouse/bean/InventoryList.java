package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/6/7.
 */

public class InventoryList {
    /**"status": 0,
     "mes": "成功",
     "list": []*/
    private int status;
    private String mes;
    private List<InventoryListlist> list;

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

    public List<InventoryListlist> getList() {
        return list;
    }

    public void setList(List<InventoryListlist> list) {
        this.list = list;
    }

    public static class InventoryListlist{
        /** "InventoryID": "1",
         "InventoryNumber": "239782198",
         "InventoryDate": "2017-06-01",
         "InventoryRemark": "5月底盘点",
         "InventoryStatus": "1",
         "ProductCount": "0",
         "ProductList": []*/
        private String InventoryID;
        private String InventoryNumber;
        private String InventoryDate;
        private String InventoryRemark;
        private String InventoryStatus;
        private String ProductCount;
        private List<InventoryListProductList> ProductList;

        public String getInventoryID() {
            return InventoryID;
        }

        public void setInventoryID(String inventoryID) {
            InventoryID = inventoryID;
        }

        public String getInventoryNumber() {
            return InventoryNumber;
        }

        public void setInventoryNumber(String inventoryNumber) {
            InventoryNumber = inventoryNumber;
        }

        public String getInventoryDate() {
            return InventoryDate;
        }

        public void setInventoryDate(String inventoryDate) {
            InventoryDate = inventoryDate;
        }

        public String getInventoryRemark() {
            return InventoryRemark;
        }

        public void setInventoryRemark(String inventoryRemark) {
            InventoryRemark = inventoryRemark;
        }

        public String getInventoryStatus() {
            return InventoryStatus;
        }

        public void setInventoryStatus(String inventoryStatus) {
            InventoryStatus = inventoryStatus;
        }

        public String getProductCount() {
            return ProductCount;
        }

        public void setProductCount(String productCount) {
            ProductCount = productCount;
        }

        public List<InventoryListProductList> getProductList() {
            return ProductList;
        }

        public void setProductList(List<InventoryListProductList> productList) {
            ProductList = productList;
        }

        public static class InventoryListProductList{
            /** "ProductID": "33",
             "ProductName": "商品23",
             "ProductImg": "http://182.92.183.143:8060//UploadFile/3d481a3b9915417289709dbd605b42ae.png",
             "ProductSpec": "1",
             "ProductCount": "0",
             "Unit": "盒"*/
            private String ProductID;
            private String ProductName;
            private String ProductImg;
            private String ProductSpec;
            private String ProductCount;
            private String Unit;

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

            public String getProductCount() {
                return ProductCount;
            }

            public void setProductCount(String productCount) {
                ProductCount = productCount;
            }

            public String getUnit() {
                return Unit;
            }

            public void setUnit(String unit) {
                Unit = unit;
            }
        }
    }
}
