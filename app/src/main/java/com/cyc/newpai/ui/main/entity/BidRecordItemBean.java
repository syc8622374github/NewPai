package com.cyc.newpai.ui.main.entity;

public class BidRecordItemBean {
    private String money;
    private String add_time;
    private String nickname;
    private String img;
    private String ip_address;

    public BidRecordItemBean(String money, String add_time, String nickname, String img, String ip_address) {
        this.money = money;
        this.add_time = add_time;
        this.nickname = nickname;
        this.img = img;
        this.ip_address = ip_address;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
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

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }
}
