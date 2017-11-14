package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/9.
 */

public class HXSBSBAdd {
    private int Id;
    private String Name;
    private String Logo;
    private String ProSpecifications;
    private int NeedTotal;
    private int NeedJTotal;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public String getProSpecifications() {
        return ProSpecifications;
    }

    public void setProSpecifications(String proSpecifications) {
        ProSpecifications = proSpecifications;
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

    public HXSBSBAdd(int id, String name, String logo, String proSpecifications, int needTotal, int needJTotal) {

        Id = id;
        Name = name;
        Logo = logo;
        ProSpecifications = proSpecifications;
        NeedTotal = needTotal;
        NeedJTotal = needJTotal;
    }
}
