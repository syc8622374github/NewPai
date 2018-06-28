package com.cyc.newpai.ui.main.entity;

public class HomeBean {
    private int imageRes;
    private String imageUrl;
    private Integer countdown;
    private String prompt;
    private Integer price;

    public HomeBean() {
    }

    public HomeBean(int imageRes, Integer countdown, String prompt,Integer price) {
        this.imageRes = imageRes;
        this.countdown = countdown;
        this.prompt = prompt;
        this.price = price;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getCountdown() {
        return countdown;
    }

    public void setCountdown(Integer countdown) {
        this.countdown = countdown;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
