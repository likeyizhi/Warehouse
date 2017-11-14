package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/7.
 */

public class GetProductNeedList {
    /**"status": 0,
     "remark": "remarkddd",
     "mes": "成功",
     "Id": 14
     "list": []*/
    private int status;
    private String mes;
    private String remark;
    private int Id;
    private List<GetProductNeedListlist> list;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public List<GetProductNeedListlist> getList() {
        return list;
    }

    public void setList(List<GetProductNeedListlist> list) {
        this.list = list;
    }

    public static class GetProductNeedListlist{
        /**"Id": 14,
         "ProductId": 11,
         "Name": "优智DHA藻油凝胶糖果（30粒550mg）",
         "NeedTotal": 960,
         "NeedJTotal": 20,
         "NeedMonth": "2017-12",
         "AddDate": "2017-11-03T13:03:43.287",
         "ProSpecifications": "0.55g/粒*10粒/板*3板/盒*48盒/件",
         "PackSpecifications": 48,
         "Logo": "http://manager.xiuzheng.cc//UploadFile/43591cd0be39429f917654df472a8ee0.jpg"*/
        private int Id;
        private int ProductId;
        private String Name;
        private int NeedTotal;
        private int NeedJTotal;
        private String NeedMonth;
        private String AddDate;
        private String ProSpecifications;
        private int PackSpecifications;
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

        public int getNeedTotal() {
            return NeedTotal;
        }

        public void setNeedTotal(int needTotal) {
            NeedTotal = needTotal;
        }

        public int getNeedJTotal() {
            return NeedJTotal;
        }

        public void setNeedJTotal(int needJTotal) {
            NeedJTotal = needJTotal;
        }

        public String getNeedMonth() {
            return NeedMonth;
        }

        public void setNeedMonth(String needMonth) {
            NeedMonth = needMonth;
        }

        public String getAddDate() {
            return AddDate;
        }

        public void setAddDate(String addDate) {
            AddDate = addDate;
        }

        public String getProSpecifications() {
            return ProSpecifications;
        }

        public void setProSpecifications(String proSpecifications) {
            ProSpecifications = proSpecifications;
        }

        public int getPackSpecifications() {
            return PackSpecifications;
        }

        public void setPackSpecifications(int packSpecifications) {
            PackSpecifications = packSpecifications;
        }

        public String getLogo() {
            return Logo;
        }

        public void setLogo(String logo) {
            Logo = logo;
        }
    }
}
