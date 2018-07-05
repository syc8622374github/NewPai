package com.cyc.newpai.ui.main.entity;

import java.util.List;

public class BannerResultBean<T> {
    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
