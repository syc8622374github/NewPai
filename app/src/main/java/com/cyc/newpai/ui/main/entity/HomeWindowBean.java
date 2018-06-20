package com.cyc.newpai.ui.main.entity;

public class HomeWindowBean {

    private String imageurl;
    private String title;
    private String url;
    private int imageRes;

    public HomeWindowBean() {
    }

    public HomeWindowBean(String title, int imageRes) {
        this.title = title;
        this.imageRes = imageRes;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }
}
