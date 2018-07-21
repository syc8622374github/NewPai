package com.cyc.newpai.ui.common.entity;

public class SearchBean {
    private String id;
    private String market_price;
    private String goods_name;
    private String iamge;
    private String now_price;
    private String last_bid_time;
    private String begin_time;
    private String last_bid_uid;
    private String nickname;
    private String head_img;
    private Integer left_second;

    public SearchBean() {
    }

    public SearchBean(String goods_name, String now_price, String nickname, String head_img, Integer left_second) {
        this.goods_name = goods_name;
        this.now_price = now_price;
        this.nickname = nickname;
        this.head_img = head_img;
        this.left_second = left_second;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getIamge() {
        return iamge;
    }

    public void setIamge(String iamge) {
        this.iamge = iamge;
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

    public String getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }

    public String getLast_bid_uid() {
        return last_bid_uid;
    }

    public void setLast_bid_uid(String last_bid_uid) {
        this.last_bid_uid = last_bid_uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public Integer getLeft_second() {
        return left_second;
    }

    public void setLeft_second(Integer left_second) {
        this.left_second = left_second;
    }
}
