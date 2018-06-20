package com.cyc.newpai.ui.main.entity;

public class HomeBean {
    private int imageRes;
    private String imageUrl;
    private String countdown;
    private String prompt;

    public HomeBean() {
    }

    public HomeBean(int imageRes, String countdown, String prompt) {
        this.imageRes = imageRes;
        this.countdown = countdown;
        this.prompt = prompt;
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

    public String getCountdown() {
        return countdown;
    }

    public void setCountdown(String countdown) {
        this.countdown = countdown;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
