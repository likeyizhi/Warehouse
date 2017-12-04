package com.bbld.warehouse.bean;

/**
 * Created by likey on 2017/11/29.
 */

public class ZdfhOtherThree {
    private String ProductName;
    private String ProductLogo;
    private String ProductSpec;

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductLogo() {
        return ProductLogo;
    }

    public void setProductLogo(String productLogo) {
        ProductLogo = productLogo;
    }

    public String getProductSpec() {
        return ProductSpec;
    }

    public void setProductSpec(String productSpec) {
        ProductSpec = productSpec;
    }

    public ZdfhOtherThree(String productName, String productLogo, String productSpec) {
        ProductName = productName;
        ProductLogo = productLogo;
        ProductSpec = productSpec;
    }
}
