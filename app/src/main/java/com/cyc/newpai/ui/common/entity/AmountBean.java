package com.cyc.newpai.ui.common.entity;

public class AmountBean {
    private String amount;
    private String rmb;
    private boolean isSelect;

    public AmountBean(String amount, String rmb, boolean isSelect) {
        this.amount = amount;
        this.rmb = rmb;
        this.isSelect = isSelect;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRmb() {
        return rmb;
    }

    public void setRmb(String rmb) {
        this.rmb = rmb;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
