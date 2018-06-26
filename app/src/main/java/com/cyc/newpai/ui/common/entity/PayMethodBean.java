package com.cyc.newpai.ui.common.entity;

public class PayMethodBean {

    private int icResId;
    private String payMethod;
    private boolean isCheck;

    public PayMethodBean(int icResId, String payMethod, boolean isCheck) {
        this.icResId = icResId;
        this.payMethod = payMethod;
        this.isCheck = isCheck;
    }

    public int getIcResId() {
        return icResId;
    }

    public void setIcResId(int icResId) {
        this.icResId = icResId;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
