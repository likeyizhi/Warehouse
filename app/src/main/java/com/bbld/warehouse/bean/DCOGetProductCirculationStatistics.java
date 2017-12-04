package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/22.
 */

public class DCOGetProductCirculationStatistics {
    /**"status": 0,
     "mes": "成功",
     "list": []*/
    private int status;
    private String mes;
    private List<DCOGetProductCirculationStatisticslist>list;

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

    public List<DCOGetProductCirculationStatisticslist> getList() {
        return list;
    }

    public void setList(List<DCOGetProductCirculationStatisticslist> list) {
        this.list = list;
    }

    public static class DCOGetProductCirculationStatisticslist{
        /**"Code": "0101004",
         "Name": "优智DHA藻油凝胶糖果（90粒550mg）",
         "ProSpecifications": "0.55g/粒*10粒/板*3板/袋*3袋/盒*8盒/件",
         "TypeId": 1,
         "DealerName": "郑州市区终端直营",
         "BarCode": "13812021550208",
         "SerialNumber": "2",
         "BatchNumber": "1111333",
         "WarehouseName": "郑州市区终端直营",
         "AddDate": "2017-11-21T15:50:31.26"*/
        private String Code;
        private String Name;
        private String ProSpecifications;
        private int TypeId;
        private String DealerName;
        private String BarCode;
        private String SerialNumber;
        private String BatchNumber;
        private String WarehouseName;
        private String AddDate;

        public String getCode() {
            return Code;
        }

        public void setCode(String code) {
            Code = code;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getProSpecifications() {
            return ProSpecifications;
        }

        public void setProSpecifications(String proSpecifications) {
            ProSpecifications = proSpecifications;
        }

        public int getTypeId() {
            return TypeId;
        }

        public void setTypeId(int typeId) {
            TypeId = typeId;
        }

        public String getDealerName() {
            return DealerName;
        }

        public void setDealerName(String dealerName) {
            DealerName = dealerName;
        }

        public String getBarCode() {
            return BarCode;
        }

        public void setBarCode(String barCode) {
            BarCode = barCode;
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

        public String getWarehouseName() {
            return WarehouseName;
        }

        public void setWarehouseName(String warehouseName) {
            WarehouseName = warehouseName;
        }

        public String getAddDate() {
            return AddDate;
        }

        public void setAddDate(String addDate) {
            AddDate = addDate;
        }
    }
}
