package com.bbld.warehouse.bean;

import java.util.List;

/**
 * 库存盘点--编辑
 * Created by likey on 2017/6/8.
 */

public class InventoryEdit {
    /**"status": 0,
     "mes": "成功",
     "Info": {}*/
    private int status;
    private String mes;
    private InventoryEditInfo Info;

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

    public InventoryEditInfo getInfo() {
        return Info;
    }

    public void setInfo(InventoryEditInfo info) {
        Info = info;
    }

    public static class InventoryEditInfo{
        /**"InventoryID": "5",
         "InventoryNumber": "543464664",
         "InventoryDate": "2017-06-08 11:09:49",
         "InventoryRemark": "ududusus",
         "InventoryStatus": "1",
         "ProductCount": "11",
         "ProductList": []*/
        private String InventoryID;
        private String InventoryNumber;
        private String InventoryDate;
        private String InventoryRemark;
        private String InventoryStatus;
        private String ProductCount;
        private List<InventoryEditProductList> ProductList;

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

        public List<InventoryEditProductList> getProductList() {
            return ProductList;
        }

        public void setProductList(List<InventoryEditProductList> productList) {
            ProductList = productList;
        }

        public static class InventoryEditProductList{
            /**"ProductID": "11",
             "ProductName": "商品名称",
             "ProductImg": "http://182.92.183.143:8060//UploadFile/758deb95deb74b00a5d8b693400c16af.png",
             "ProductSpec": "规格",
             "ProductCount": "8",
             "Unit": "箱",
             "CodeList": []*/
            private String ProductID;
            private String ProductName;
            private String ProductImg;
            private String ProductSpec;
            private String ProductCount;
            private String Unit;
            private List<InventoryEditCodeList> CodeList;

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

            public List<InventoryEditCodeList> getCodeList() {
                return CodeList;
            }

            public void setCodeList(List<InventoryEditCodeList> codeList) {
                CodeList = codeList;
            }

            public static class InventoryEditCodeList{
                /**"Code": "14232020000100",
                 "CodeType": "1",
                 "Count": "8"*/
                private String Code;
                private String CodeType;
                private String Count;

                public String getCode() {
                    return Code;
                }

                public void setCode(String code) {
                    Code = code;
                }

                public String getCodeType() {
                    return CodeType;
                }

                public void setCodeType(String codeType) {
                    CodeType = codeType;
                }

                public String getCount() {
                    return Count;
                }

                public void setCount(String count) {
                    Count = count;
                }
            }
        }
    }
}
