package com.bbld.warehouse.bean;

import java.util.List;

/**
 * 市场交接--编辑
 * Created by likey on 2017/6/8.
 */

public class HandoverEdit {
    /**"status": 0,
     "mes": "成功",
     "Info": {}*/
    private int status;
    private String mes;
    private HandoverEditInfo Info;

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

    public HandoverEditInfo getInfo() {
        return Info;
    }

    public void setInfo(HandoverEditInfo info) {
        Info = info;
    }

    public static class HandoverEditInfo{
        /**"HandOverID": "1",
         "HandoverCode": "JJD-b6b4c5e89",
         "jjr": "经销商1",
         "zcr": "婴童营销中心吉林省办",
         "Remark": "测试添加交接单",
         "Date": "2017-06-06",
         "ProductList": []*/
        private String HandOverID;
        private String HandoverCode;
        private String jjr;
        private String zcr;
        private String Remark;
        private String Date;
        private List<HandoverEditProductList> ProductList;

        public String getHandOverID() {
            return HandOverID;
        }

        public void setHandOverID(String handOverID) {
            HandOverID = handOverID;
        }

        public String getHandoverCode() {
            return HandoverCode;
        }

        public void setHandoverCode(String handoverCode) {
            HandoverCode = handoverCode;
        }

        public String getJjr() {
            return jjr;
        }

        public void setJjr(String jjr) {
            this.jjr = jjr;
        }

        public String getZcr() {
            return zcr;
        }

        public void setZcr(String zcr) {
            this.zcr = zcr;
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

        public List<HandoverEditProductList> getProductList() {
            return ProductList;
        }

        public void setProductList(List<HandoverEditProductList> productList) {
            ProductList = productList;
        }

        public static class HandoverEditProductList{
            /**"ProductID": "13",
             "ProductName": "商品3",
             "ProductImg": "http://182.92.183.143:8060//UploadFile/dcbc8363992b4c36bf510fd5b472b9ce.png",
             "ProductSpec": "3",
             "ProductCount": "5",
             "Unit": "箱",
             "CodeList": []*/
            private String ProductID;
            private String ProductName;
            private String ProductImg;
            private String ProductSpec;
            private String ProductCount;
            private String Unit;
            private List<HandoverEditCodeList> CodeList;

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

            public List<HandoverEditCodeList> getCodeList() {
                return CodeList;
            }

            public void setCodeList(List<HandoverEditCodeList> codeList) {
                CodeList = codeList;
            }

            public static class HandoverEditCodeList{
                /**"Code": "12312",
                 "CodeType": "1",
                 "Count": "10"*/
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
