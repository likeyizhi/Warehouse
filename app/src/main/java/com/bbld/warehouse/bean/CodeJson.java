package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/5/25.
 */

public class CodeJson {
//    {
//        List: [
//        {
//            ProductID: 1,
//            CodeList: [
//            {
//                Code: "1110000001000"
//            },
//            {
//                Code: "1110000001001"
//            },
//            {
//                Code: "1110000001003"
//            },
//            {
//                Code: "1110000001005"
//            }
//        {
//            ProductID: 1,
//            CodeList: [
//            {
//                Code: "1110000001000"
//                "SerialNumber": "123123123",
//                "BatchNumber": "12312312312"
//            },
//            {
//                Code: "1110000001001"
//            },
//            {
//                Code: "1110000001003"
//            },
//            {
//                Code: "1110000001005"
//            }
//      ]
//        }
//  ]
//    }
    private List<CodeJsonList> List;

    public List<CodeJsonList> getList() {
        return List;
    }

    public void setList(List<CodeJsonList> list) {
        List = list;
    }

    public static class CodeJsonList{
        private int ProductID;
        private List<CodeJsonCodeList> CodeList;

        public int getProductID() {
            return ProductID;
        }

        public void setProductID(int productID) {
            ProductID = productID;
        }

        public java.util.List<CodeJsonCodeList> getCodeList() {
            return CodeList;
        }

        public void setCodeList(java.util.List<CodeJsonCodeList> codeList) {
            CodeList = codeList;
        }

        public static class CodeJsonCodeList{
            private String Code;
            private String SerialNumber;
            private String BatchNumber;
            private String Count;
            private String Type;

            public String getCount() {
                return Count;
            }

            public void setCount(String count) {
                Count = count;
            }

            public String getType() {
                return Type;
            }

            public void setType(String type) {
                Type = type;
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

            public String getCode() {
                return Code;
            }

            public void setCode(String code) {
                Code = code;
            }
        }
    }
}
