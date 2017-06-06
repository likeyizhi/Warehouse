package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/6/5.
 */

public class StorageDetails {
    /**"status": 0,
     "mes": "成功",
     "Info": {}*/
    private int status;
    private String mes;
    private StorageDetailsInfo Info;

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

    public StorageDetailsInfo getInfo() {
        return Info;
    }

    public void setInfo(StorageDetailsInfo info) {
        Info = info;
    }

    public static class StorageDetailsInfo{
        /**"StorageID": "1",
         "StorageNumber": "00011111",
         "TypeName": "到货入库",
         "LinkName": "刘先生",
         "LinkPhone": "13712344321",
         "Remark": "",
         "Date": "2017-05-09",
         "ProductCount": "10",
         "ProductList": []*/
        private String StorageID;
        private String StorageNumber;
        private String TypeName;
        private String LinkName;
        private String LinkPhone;
        private String Remark;
        private String Date;
        private String ProductCount;
        private List<StorageDetailsProductList> ProductList;

        public String getStorageID() {
            return StorageID;
        }

        public void setStorageID(String storageID) {
            StorageID = storageID;
        }

        public String getStorageNumber() {
            return StorageNumber;
        }

        public void setStorageNumber(String storageNumber) {
            StorageNumber = storageNumber;
        }

        public String getTypeName() {
            return TypeName;
        }

        public void setTypeName(String typeName) {
            TypeName = typeName;
        }

        public String getLinkName() {
            return LinkName;
        }

        public void setLinkName(String linkName) {
            LinkName = linkName;
        }

        public String getLinkPhone() {
            return LinkPhone;
        }

        public void setLinkPhone(String linkPhone) {
            LinkPhone = linkPhone;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String remark) {
            Remark = remark;
        }

        public String getDate() {
            return Date;
        }

        public void setDate(String date) {
            Date = date;
        }

        public String getProductCount() {
            return ProductCount;
        }

        public void setProductCount(String productCount) {
            ProductCount = productCount;
        }

        public List<StorageDetailsProductList> getProductList() {
            return ProductList;
        }

        public void setProductList(List<StorageDetailsProductList> productList) {
            ProductList = productList;
        }

        public static class StorageDetailsProductList{
            /**"ProductID": "11",
             "ProductName": "商品名称",
             "ProductImg": "http://182.92.183.143:8060//UploadFile/758deb95deb74b00a5d8b693400c16af.png",
             "ProductSpec": "规格",
             "ProductCount": "10",
             "Unit": "箱"*/
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
