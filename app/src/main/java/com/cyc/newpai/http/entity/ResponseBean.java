package com.cyc.newpai.http.entity;

public class ResponseBean<T> {
    private int code;
    private String msg;
    private T result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String meg) {
        this.msg = meg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ResponseBean{" +
                "code=" + code +
                ", meg='" + msg + '\'' +
                ", result=" + result.toString() +
                '}';
    }
}
