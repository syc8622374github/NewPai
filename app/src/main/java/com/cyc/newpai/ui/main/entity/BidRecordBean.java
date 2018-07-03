package com.cyc.newpai.ui.main.entity;

import java.util.List;

public class BidRecordBean {
    private String total;
    private int page_total;
    private List<BidRecordItemBean> list;

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

    public List<BidRecordItemBean> getItemBeans() {
        return list;
    }

    public void setItemBeans(List<BidRecordItemBean> list) {
        this.list = list;
    }
}
