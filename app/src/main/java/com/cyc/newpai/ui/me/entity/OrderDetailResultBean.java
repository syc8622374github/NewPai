package com.cyc.newpai.ui.me.entity;

import java.io.Serializable;

public class OrderDetailResultBean implements Serializable{
    private String goods_name;
    private String image;
    private String deal_uid;
    private String deal_price;
    private String order_sn;
    private String addr_name;
    private String addr_mobile;
    private String address;
    private String last_bid_time;
    private String deal_time;
    private String add_time;
    private String pay_time;
    private Integer pay_status;
    private Integer create_order_limit_sec;
    private Integer logistics_fee;
    private Integer InternalError;

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDeal_uid() {
        return deal_uid;
    }

    public void setDeal_uid(String deal_uid) {
        this.deal_uid = deal_uid;
    }

    public String getDeal_price() {
        return deal_price;
    }

    public void setDeal_price(String deal_price) {
        this.deal_price = deal_price;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getAddr_name() {
        return addr_name;
    }

    public void setAddr_name(String addr_name) {
        this.addr_name = addr_name;
    }

    public String getAddr_mobile() {
        return addr_mobile;
    }

    public void setAddr_mobile(String addr_mobile) {
        this.addr_mobile = addr_mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLast_bid_time() {
        return last_bid_time;
    }

    public void setLast_bid_time(String last_bid_time) {
        this.last_bid_time = last_bid_time;
    }

    public String getDeal_time() {
        return deal_time;
    }

    public void setDeal_time(String deal_time) {
        this.deal_time = deal_time;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public Integer getPay_status() {
        return pay_status;
    }

    public void setPay_status(Integer pay_status) {
        this.pay_status = pay_status;
    }

    public Integer getCreate_order_limit_sec() {
        return create_order_limit_sec;
    }

    public void setCreate_order_limit_sec(Integer create_order_limit_sec) {
        this.create_order_limit_sec = create_order_limit_sec;
    }

    public Integer getLogistics_fee() {
        return logistics_fee;
    }

    public void setLogistics_fee(Integer logistics_fee) {
        this.logistics_fee = logistics_fee;
    }

    public Integer getInternalError() {
        return InternalError;
    }

    public void setInternalError(Integer internalError) {
        InternalError = internalError;
    }
}