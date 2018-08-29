package com.cyc.newpai.ui.common.entity;

public class AmountBean {
    private String zeng_money;
    private String money;
    private boolean isSelect;

    public AmountBean(String zeng_money, String money, boolean isSelect) {
        this.zeng_money = zeng_money;
        this.money = money;
        this.isSelect = isSelect;
    }

    public String getZeng_money() {
        return zeng_money;
    }

    public void setZeng_money(String zeng_money) {
        this.zeng_money = zeng_money;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
