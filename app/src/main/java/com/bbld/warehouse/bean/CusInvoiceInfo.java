package com.bbld.warehouse.bean;

import java.util.List;

/**
 * 终端配送详情
 * Created by likey on 2017/8/11.
 */

public class CusInvoiceInfo {
    private int status;
    private String mes;
    private CusInvoiceInfoInfo Info;

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

    public CusInvoiceInfoInfo getInfo() {
        return Info;
    }

    public void setInfo(CusInvoiceInfoInfo info) {
        Info = info;
    }

    public static class CusInvoiceInfoInfo{
        /**"StorageID": "5",
         "StorageNumber": "fhd-zd8-20170811-1",
         "CusName": "郑州市区终端直营",
         "Remark": "终端直发-test22",
         "Date": "2017-08-11",
         "ProductCount": "2",
         "ProductList": []*/
        private String StorageID;
        private String StorageNumber;
        private String CusName;
        private String Remark;
        private String Date;
        private String ProductCount;
        private List<CusInvoiceInfoProductList> ProductList;

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

        public String getCusName() {
            return CusName;
        }

        public void setCusName(String cusName) {
            CusName = cusName;
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

        public List<CusInvoiceInfoProductList> getProductList() {
            return ProductList;
        }

        public void setProductList(List<CusInvoiceInfoProductList> productList) {
            ProductList = productList;
        }

        public static class CusInvoiceInfoProductList{
            /**"ProductID": "14",
             "ProductName": "修正牌睿迪软胶囊（30粒）",
             "ProductImg": "http://manager.xiuzheng.cc:81//UploadFile/241cc8bff7c6495fb3692ad29c9621bd.jpg",
             "ProductSpec": "500mg/粒*30粒/盒*48盒/件",
             "ProductCount": "2",
             "Unit": "盒"
             "CodeList": "[]"*/
            private String ProductID;
            private String ProductName;
            private String ProductImg;
            private String ProductSpec;
            private String ProductCount;
            private String Unit;
            private List<CusInvoiceInfoCodeList> CodeList;

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

            public List<CusInvoiceInfoCodeList> getCodeList() {
                return CodeList;
            }

            public void setCodeList(List<CusInvoiceInfoCodeList> codeList) {
                CodeList = codeList;
            }

            public static class CusInvoiceInfoCodeList{
                /**"Code": "10812020170841",
                 "SerialNumber": "30-3",
                 "BatchNumber": "1-1"*/
                private String Code;
                private String SerialNumber;
                private String BatchNumber;

                public String getCode() {
                    return Code;
                }

                public void setCode(String code) {
                    Code = code;
                }

                public String getSerialNumber() {
                    return SerialNumber;
                }

                public void setSerialNumber(String serialNumber) {
                    SerialNumber = serialNumber;
                }

                public String getBatchNumber() {
                    return BatchNumber;
                }

                public void setBatchNumber(String batchNumber) {
                    BatchNumber = batchNumber;
                }
            }
        }
    }
}
