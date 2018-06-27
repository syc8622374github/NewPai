package com.cyc.newpai.ui.category.entity;

public class CategoryDetailBean {
    private int iconResId;
    private String title;
    private String price;
    private String countDown;
    private int parentFlag;
    private int flag;

    public CategoryDetailBean(int iconResId, String title, String price, String countDown, int parentFlag, int flag) {
        this.iconResId = iconResId;
        this.title = title;
        this.price = price;
        this.countDown = countDown;
        this.parentFlag = parentFlag;
        this.flag = flag;
    }

    public CategoryDetailBean(int iconResId, String title, String price, String countDown) {
        this.iconResId = iconResId;
        this.title = title;
        this.price = price;
        this.countDown = countDown;
    }

    public int getParentFlag() {
        return parentFlag;
    }

    public void setParentFlag(int parentFlag) {
        this.parentFlag = parentFlag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCountDown() {
        return countDown;
    }

    public void setCountDown(String countDown) {
        this.countDown = countDown;
    }
}
