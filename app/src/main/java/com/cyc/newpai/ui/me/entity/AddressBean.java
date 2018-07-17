package com.cyc.newpai.ui.me.entity;

public class AddressBean {
    private String name;
    private String phone;
    private String address;
    private boolean isCheck;

    public AddressBean(String name, String phone, String address, boolean isCheck) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.isCheck = isCheck;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
