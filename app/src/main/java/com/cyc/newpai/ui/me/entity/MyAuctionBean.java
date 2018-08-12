package com.cyc.newpai.ui.me.entity;

import java.io.Serializable;

public class MyAuctionBean implements Serializable {
    private String id;
    private String gid;
    private String now_price;
    private String last_bid_time;
    private String server_time;
    private String last_bid_uid;
    private String deal_uid;
    private String deal_price;
    private String mid;
    private String shopid;
    private String goods_name;
    private String image;
    private String pay_status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getNow_price() {
        return now_price;
    }

    public void setNow_price(String now_price) {
        this.now_price = now_price;
    }

    public String getLast_bid_time() {
        return last_bid_time;
    }

    public void setLast_bid_time(String last_bid_time) {
        this.last_bid_time = last_bid_time;
    }

    public String getServer_time() {
        return server_time;
    }

    public void setServer_time(String server_time) {
        this.server_time = server_time;
    }

    public String getLast_bid_uid() {
        return last_bid_uid;
    }

    public void setLast_bid_uid(String last_bid_uid) {
        this.last_bid_uid = last_bid_uid;
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

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }
}
