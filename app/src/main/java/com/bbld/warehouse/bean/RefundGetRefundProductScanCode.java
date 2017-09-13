package com.bbld.warehouse.bean;

import java.util.List;

/**
 * 退货申请-详情-明细
 * Created by likey on 2017/9/7.
 */

public class RefundGetRefundProductScanCode {
    /**"status": 0,
     "mes": "成功",
     "list": []*/
    private int status;
    private String mes;
    private List<RefundGetRefundProductScanCodelist> list;

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

    public List<RefundGetRefundProductScanCodelist> getList() {
        return list;
    }

    public void setList(List<RefundGetRefundProductScanCodelist> list) {
        this.list = list;
    }

    public static class RefundGetRefundProductScanCodelist{
        /**"NOIDTBRefundScanCode": 1,
         "RefundId": 7,
         "ScanCode": "123",
         "ProductId": 15,
         "CodeType": 1,
         "CodeForCount": 8,
         "SerialNumber": "1",
         "BatchNumber": "1"*/
        private int NOIDTBRefundScanCode;
        private int RefundId;
        private String ScanCode;
        private int ProductId;
        private int CodeType;
        private int CodeForCount;
        private String SerialNumber;
        private String BatchNumber;

        public int getNOIDTBRefundScanCode() {
            return NOIDTBRefundScanCode;
        }

        public void setNOIDTBRefundScanCode(int NOIDTBRefundScanCode) {
            this.NOIDTBRefundScanCode = NOIDTBRefundScanCode;
        }

        public int getRefundId() {
            return RefundId;
        }

        public void setRefundId(int refundId) {
            RefundId = refundId;
        }

        public String getScanCode() {
            return ScanCode;
        }

        public void setScanCode(String scanCode) {
            ScanCode = scanCode;
        }

        public int getProductId() {
            return ProductId;
        }

        public void setProductId(int productId) {
            ProductId = productId;
        }

        public int getCodeType() {
            return CodeType;
        }

        public void setCodeType(int codeType) {
            CodeType = codeType;
        }

        public int getCodeForCount() {
            return CodeForCount;
        }

        public void setCodeForCount(int codeForCount) {
            CodeForCount = codeForCount;
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
