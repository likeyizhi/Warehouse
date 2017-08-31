package com.bbld.warehouse.bean;

import java.util.List;

/**
 * 退货单
 * Created by likey on 2017/8/28.
 */

public class RefundList {
    /** "status": 0,
     "mes": "成功",
     "list": []*/
    private int status;
    private String mes;
    private List<RefundListlist> list;

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

    public List<RefundListlist> getList() {
        return list;
    }

    public void setList(List<RefundListlist> list) {
        this.list = list;
    }

    public static class RefundListlist{
        /**"Id": "5",
         "RefundCode": "TH-20170827060408",
         "RefundDealerName": "郑州地办",
         "LinkName": "郑州地办",
         "LinkPhone": "13200000000",
         "AddDate": "2017-08-27",
         "ProductCount": "2",
         "ProductTypeCount": "1",
         "ProductList": []*/
        private String Id;
        private String RefundCode;
        private String RefundDealerName;
        private String LinkName;
        private String LinkPhone;
        private String AddDate;
        private String ProductCount;
        private String ProductTypeCount;
        private List<RefundListProductList> ProductList;

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getRefundCode() {
            return RefundCode;
        }

        public void setRefundCode(String refundCode) {
            RefundCode = refundCode;
        }

        public String getRefundDealerName() {
            return RefundDealerName;
        }

        public void setRefundDealerName(String refundDealerName) {
            RefundDealerName = refundDealerName;
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

        public String getAddDate() {
            return AddDate;
        }

        public void setAddDate(String addDate) {
            AddDate = addDate;
        }

        public String getProductCount() {
            return ProductCount;
        }

        public void setProductCount(String productCount) {
            ProductCount = productCount;
        }

        public String getProductTypeCount() {
            return ProductTypeCount;
        }

        public void setProductTypeCount(String productTypeCount) {
            ProductTypeCount = productTypeCount;
        }

        public List<RefundListProductList> getProductList() {
            return ProductList;
        }

        public void setProductList(List<RefundListProductList> productList) {
            ProductList = productList;
        }

        public static class RefundListProductList{
            /** "ProductID": "11",
             "ProductName": "优智DHA藻油凝胶糖果（30粒550mg）",
             "ProductImg": "http://manager.xiuzheng.cc:81//UploadFile/43591cd0be39429f917654df472a8ee0.jpg",
             "ProductSpec": "0.55g/粒*10粒/板*3板/盒*48盒/件",
             "ProductAmount": "2",
             "Unit": "盒"*/
            private String ProductID;
            private String ProductName;
            private String ProductImg;
            private String ProductSpec;
            private String ProductAmount;
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

            public String getProductAmount() {
                return ProductAmount;
            }

            public void setProductAmount(String productAmount) {
                ProductAmount = productAmount;
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
