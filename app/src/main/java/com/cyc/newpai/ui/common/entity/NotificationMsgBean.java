package com.cyc.newpai.ui.common.entity;

public class NotificationMsgBean {

    public final static int TYPE_NOTIFICATION_MSG_PAY = 0x212001;
    public final static int TYPE_NOTIFICATION_MSG_INCOMING = 0x212002;

    private String title;
    private String content;
    private String time;
    private int type;

    public NotificationMsgBean(String title, String content, String time, int type) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
