package com.bbld.warehouse.bean;

import java.util.List;

/**
 * 销售出库-扫码
 * Created by likey on 2017/8/15.
 */

public class SaleScanCode {
    /**"status": 0,
     "mes": "成功",
     "Info": {}*/
    private int status;
    private String mes;
    private SaleScanCodeInfo Info;

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

    public SaleScanCodeInfo getInfo() {
        return Info;
    }

    public void setInfo(SaleScanCodeInfo info) {
        Info = info;
    }

    public static class SaleScanCodeInfo{
        /** "IsRight": 1,
         "Type": 2,
         "Code": "10812020170732",
         "SerialNumber": "",
         "BatchNumber": "",
         "Count": 1,
         "ProductInfo": {}*/
        private int IsRight;
        private int Type;
        private String Code;
        private String SerialNumber;
        private String BatchNumber;
        private int Count;
        private SaleScanCodeProductInfo ProductInfo;

        public int getIsRight() {
            return IsRight;
        }

        public void setIsRight(int isRight) {
            IsRight = isRight;
        }

        public int getType() {
            return Type;
        }

        public void setType(int type) {
            Type = type;
        }

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

        public int getCount() {
            return Count;
        }

        public void setCount(int count) {
            Count = count;
        }

        public SaleScanCodeProductInfo getProductInfo() {
            return ProductInfo;
        }

        public void setProductInfo(SaleScanCodeProductInfo productInfo) {
            ProductInfo = productInfo;
        }

        public static class SaleScanCodeProductInfo{
            /**"ProductID": "14",
             "ProductName": "修正牌睿迪软胶囊（30粒）",
             "ProductImg": "http://manager.xiuzheng.cc:81//UploadFile/241cc8bff7c6495fb3692ad29c9621bd.jpg",
             "ProductSpec": "500mg/粒*30粒/盒*48盒/件",
             "Unit": "盒"
             "CodeList": "[]"*/
            private String ProductID;
            private String ProductName;
            private String ProductImg;
            private String ProductSpec;
            private String Unit;
            private List<SaleScanCodeCodeList> CodeList;

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

            public List<SaleScanCodeCodeList> getCodeList() {
                return CodeList;
            }

            public void setCodeList(List<SaleScanCodeCodeList> codeList) {
                CodeList = codeList;
            }

            public static class SaleScanCodeCodeList{
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
