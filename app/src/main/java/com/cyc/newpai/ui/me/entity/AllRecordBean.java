package com.cyc.newpai.ui.me.entity;

public class AllRecordBean {

    public static final String RECORD_PRODUCT = "竞拍商品"; //竞拍商品
    public static final String RECORD_BACK = "竞拍返回"; //充值返回
    public static final String RECORD_RECHARGE = "充值"; //充值

    private String shopName;
    private String pay;
    private String type;
    private String time;

    public AllRecordBean(String shopName, String pay, String type, String time) {
        this.shopName = shopName;
        this.pay = pay;
        this.type = type;
        this.time = time;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
