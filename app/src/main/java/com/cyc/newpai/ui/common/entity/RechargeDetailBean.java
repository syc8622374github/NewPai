package com.cyc.newpai.ui.common.entity;

import java.io.Serializable;

public class RechargeDetailBean implements Serializable{
    private String orderid;
    private String qrcode;
    private String istype;
    private Double realprice;
    private int price_istype;

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getIstype() {
        return istype;
    }

    public void setIstype(String istype) {
        this.istype = istype;
    }

    public Double getRealprice() {
        return realprice;
    }

    public void setRealprice(Double realprice) {
        this.realprice = realprice;
    }

    public int getPrice_istype() {
        return price_istype;
    }

    public void setPrice_istype(int price_istype) {
        this.price_istype = price_istype;
    }
}
