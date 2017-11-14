package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/13.
 */

public class FHDGetOrderProductList {
    /**"status": 0,
     "mes": "成功",
     "list": []*/
    private int status;
    private String mes;
    private List<FHDGetOrderProductListlist> list;

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

    public List<FHDGetOrderProductListlist> getList() {
        return list;
    }

    public void setList(List<FHDGetOrderProductListlist> list) {
        this.list = list;
    }

    public static class FHDGetOrderProductListlist{
        /**"Id": 350,
         "ProductId": 12,
         "Name": "优智DHA藻油凝胶糖果（90粒550mg）",
         "ProSpecifications": "0.55g/粒*10粒/板*3板/袋*3袋/盒*8盒/件",
         "ProductAmount": 48,
         "GiveAmount": 0,
         "DeliveryCount": 8,
         "DeliveryGiveCount": 0,
         "Remark": "1",
         "Logo": "http://manager.xiuzheng.cc//UploadFile/2eb2efd9458c481a951bbf4c4302c384.jpg"*/
        private int Id;
        private int ProductId;
        private String Name;
        private String ProSpecifications;
        private int ProductAmount;
        private int GiveAmount;
        private int DeliveryCount;
        private int DeliveryGiveCount;
        private String Remark;
        private String Logo;

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public int getProductId() {
            return ProductId;
        }

        public void setProductId(int productId) {
            ProductId = productId;
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

        public int getProductAmount() {
            return ProductAmount;
        }

        public void setProductAmount(int productAmount) {
            ProductAmount = productAmount;
        }

        public int getGiveAmount() {
            return GiveAmount;
        }

        public void setGiveAmount(int giveAmount) {
            GiveAmount = giveAmount;
        }

        public int getDeliveryCount() {
            return DeliveryCount;
        }

        public void setDeliveryCount(int deliveryCount) {
            DeliveryCount = deliveryCount;
        }

        public int getDeliveryGiveCount() {
            return DeliveryGiveCount;
        }

        public void setDeliveryGiveCount(int deliveryGiveCount) {
            DeliveryGiveCount = deliveryGiveCount;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String remark) {
            Remark = remark;
        }

        public String getLogo() {
            return Logo;
        }

        public void setLogo(String logo) {
            Logo = logo;
        }
    }
}
