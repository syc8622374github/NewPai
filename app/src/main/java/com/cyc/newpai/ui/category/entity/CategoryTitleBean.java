package com.cyc.newpai.ui.category.entity;

public class CategoryTitleBean {
    private int flag;
    private String title;
    private boolean isSelect;

    public CategoryTitleBean(int flag, String title, boolean isSelect) {
        this.flag = flag;
        this.title = title;
        this.isSelect = isSelect;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
