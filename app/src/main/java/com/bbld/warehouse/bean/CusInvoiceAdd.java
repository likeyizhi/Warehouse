package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/29.
 */

public class CusInvoiceAdd {
    /** "Id": 1,
     "warehouseId": 1,
     "cusDealerId": 2,
     "remark": "2",
     "list": []*/
    private int Id;
    private int warehouseId;
    private int cusDealerId;
    private String remark;
    private List<CusInvoiceAddlist> list;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getCusDealerId() {
        return cusDealerId;
    }

    public void setCusDealerId(int cusDealerId) {
        this.cusDealerId = cusDealerId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<CusInvoiceAddlist> getList() {
        return list;
    }

    public void setList(List<CusInvoiceAddlist> list) {
        this.list = list;
    }

    public static class CusInvoiceAddlist{
        private int ProductId;
        private int ProductTotal;

        public int getProductId() {
            return ProductId;
        }

        public void setProductId(int productId) {
            ProductId = productId;
        }

        public int getProductTotal() {
            return ProductTotal;
        }

        public void setProductTotal(int productTotal) {
            ProductTotal = productTotal;
        }

        public CusInvoiceAddlist(int productId, int productTotal) {
            ProductId = productId;
            ProductTotal = productTotal;
        }
    }
}
