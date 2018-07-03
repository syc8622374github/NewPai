package com.cyc.newpai.ui.main.entity;

import java.util.List;

public class ShopDetailBean {
    private String id;
    private String gid;
    private String market_price;
    private String now_price;
    private String last_bid_time;
    private String season;
    private String begin_time;
    private String deal_uid;
    private String deal_status;
    private String goods_name;
    private String detail;
    private String image;
    private String nickname;
    private String mobile;
    private List<String> images;
    private int left_timesecond;
    private int limit_second;
    private String show;

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

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
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

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }

    public String getDeal_uid() {
        return deal_uid;
    }

    public void setDeal_uid(String deal_uid) {
        this.deal_uid = deal_uid;
    }

    public String getDeal_status() {
        return deal_status;
    }

    public void setDeal_status(String deal_status) {
        this.deal_status = deal_status;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getLeft_timesecond() {
        return left_timesecond;
    }

    public void setLeft_timesecond(int left_timesecond) {
        this.left_timesecond = left_timesecond;
    }

    public int getLimit_second() {
        return limit_second;
    }

    public void setLimit_second(int limit_second) {
        this.limit_second = limit_second;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    @Override
    public String toString() {
        return "ShopDetailBean{" +
                "id=" + id +
                ", gid=" + gid +
                ", market_price=" + market_price +
                ", now_price=" + now_price +
                ", last_bid_time=" + last_bid_time +
                ", season=" + season +
                ", begin_time='" + begin_time + '\'' +
                ", deal_uid=" + deal_uid +
                ", deal_status=" + deal_status +
                ", goods_name='" + goods_name + '\'' +
                ", detail='" + detail + '\'' +
                ", image='" + image + '\'' +
                ", nickname='" + nickname + '\'' +
                ", mobile='" + mobile + '\'' +
                ", images=" + images +
                ", left_timesecond=" + left_timesecond +
                ", limit_second=" + limit_second +
                ", show=" + show +
                '}';
    }
}
