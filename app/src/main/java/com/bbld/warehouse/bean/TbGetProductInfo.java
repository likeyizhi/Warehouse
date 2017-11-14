package com.bbld.warehouse.bean;

/**
 * Created by likey on 2017/11/3.
 */

public class TbGetProductInfo {
    /**"Id": 11,
     "ProductCode": "0101003",
     "ProSpecifications": "0.55g/粒*10粒/板*3板/盒*48盒/件",
     "ProductName": "优智DHA藻油凝胶糖果（30粒550mg）",
     "PackSpecifications": 48,
     "status": 0,
     "mes": "成功"*/
    private int Id;
    private String ProductCode;
    private String ProSpecifications;
    private String ProductName;
    private int PackSpecifications;
    private int status;
    private String mes;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public String getProSpecifications() {
        return ProSpecifications;
    }

    public void setProSpecifications(String proSpecifications) {
        ProSpecifications = proSpecifications;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getPackSpecifications() {
        return PackSpecifications;
    }

    public void setPackSpecifications(int packSpecifications) {
        PackSpecifications = packSpecifications;
    }

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
}
