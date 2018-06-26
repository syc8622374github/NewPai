package com.cyc.newpai.ui.category.entity;

public class CategoryTitleBean {
    private String title;
    private boolean isSelect;

    public CategoryTitleBean(String title, boolean isSelect) {
        this.title = title;
        this.isSelect = isSelect;
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
