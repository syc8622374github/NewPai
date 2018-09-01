package com.cyc.newpai.ui.me.entity;

public class SubmitOrderResultBean {
    private String orderid;
    private String qrcode;
    private String istype;
    private Float realprice;
    private Float price_istype;

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

    public Float getRealprice() {
        return realprice;
    }

    public void setRealprice(Float realprice) {
        this.realprice = realprice;
    }

    public Float getPrice_istype() {
        return price_istype;
    }

    public void setPrice_istype(Float price_istype) {
        this.price_istype = price_istype;
    }
}
