package com.bbld.warehouse.bean;

import java.util.List;

/**
 * 货需上报-上报
 * Created by likey on 2017/11/9.
 */

public class ProductNeedJson {
    /**"Id": 1,
     "month": "2017-11",
     "remark": "这里是备注",
     "list": []*/
    private int Id;
    private String month;
    private String remark;
    private List<ProductNeedJsonlist> list;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<ProductNeedJsonlist> getList() {
        return list;
    }

    public void setList(List<ProductNeedJsonlist> list) {
        this.list = list;
    }

    public static class ProductNeedJsonlist{
        /**"ProductId": 1,
         "NeedTotal": 100*/
        private int ProductId;
        private int NeedTotal;

        public ProductNeedJsonlist(int productId, int needTotal) {
            ProductId = productId;
            NeedTotal = needTotal;
        }

        public int getProductId() {
            return ProductId;
        }

        public void setProductId(int productId) {
            ProductId = productId;
        }

        public int getNeedTotal() {
            return NeedTotal;
        }

        public void setNeedTotal(int needTotal) {
            NeedTotal = needTotal;
        }
    }
}
