package com.xinyu.newdiggtest.bean;

public class BaseUser {
    private String nickname;
    private String head;
    private String check_state;
    private String become_vip_date;
    private String user_id;


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getBecome_vip_date() {
        return become_vip_date;
    }

    public void setBecome_vip_date(String become_vip_date) {
        this.become_vip_date = become_vip_date;
    }

    public String getCheck_state() {
        return check_state;
    }

    public void setCheck_state(String check_state) {
        this.check_state = check_state;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


}
