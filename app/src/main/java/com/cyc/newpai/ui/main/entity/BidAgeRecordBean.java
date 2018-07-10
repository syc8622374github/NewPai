package com.cyc.newpai.ui.main.entity;

public class BidAgeRecordBean {
    private String market_price;
    private String deal_price;
    private String nickname;
    private String img;
    private String deal_time;
    private String rate;

    public BidAgeRecordBean(String market_price, String deal_price, String nickname, String img, String deal_time, String rate) {
        this.market_price = market_price;
        this.deal_price = deal_price;
        this.nickname = nickname;
        this.img = img;
        this.deal_time = deal_time;
        this.rate = rate;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getDeal_price() {
        return deal_price;
    }

    public void setDeal_price(String deal_price) {
        this.deal_price = deal_price;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDeal_time() {
        return deal_time;
    }

    public void setDeal_time(String deal_time) {
        this.deal_time = deal_time;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
