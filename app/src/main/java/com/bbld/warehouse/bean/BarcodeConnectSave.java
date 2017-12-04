package com.bbld.warehouse.bean;

import java.util.List;

/**
 * Created by likey on 2017/11/16.
 */

public class BarcodeConnectSave {
    /**"code": "13832021550100",
     "count": "8",
     "list": []*/
    private String code;
    private String count;
    private List<String> list;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
