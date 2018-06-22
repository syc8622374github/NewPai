package com.cyc.newpai.ui.main.entity;

public class HisTransactionBean {
    private int avatorResId;
    private boolean isFinish;

    public HisTransactionBean(int avatorResId) {
        this.avatorResId = avatorResId;
    }

    public HisTransactionBean(int avatorResId, boolean isFinish) {
        this.avatorResId = avatorResId;
        this.isFinish = isFinish;
    }

    public int getAvatorResId() {
        return avatorResId;
    }

    public void setAvatorResId(int avatorResId) {
        this.avatorResId = avatorResId;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }
}
