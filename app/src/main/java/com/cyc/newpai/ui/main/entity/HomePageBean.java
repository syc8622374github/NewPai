package com.cyc.newpai.ui.main.entity;

import java.util.List;

public class HomePageBean {
    private String total;
    private int page_total;
    private List<HomeBean> list;

    public HomePageBean() {
    }

    public HomePageBean(String total, int page_total, List<HomeBean> list) {
        this.total = total;
        this.page_total = page_total;
        this.list = list;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getPage_total() {
        return page_total;
    }

    public void setPage_total(int page_total) {
        this.page_total = page_total;
    }

    public List<HomeBean> getList() {
        return list;
    }

    public void setList(List<HomeBean> list) {
        this.list = list;
    }
}
