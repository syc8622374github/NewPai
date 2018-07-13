package com.cyc.newpai.ui.me.entity;

public class AllRecordBean {

    public static final String RECORD_PRODUCT = "竞拍商品"; //竞拍商品
    public static final String RECORD_BACK = "竞拍返回"; //充值返回
    public static final String RECORD_RECHARGE = "充值"; //充值

    private String money;
    private String type;
    private String add_time;
    private String shopid;
    private String title;
    private String description;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
