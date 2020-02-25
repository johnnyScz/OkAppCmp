package com.xinyu.newdiggtest.bean;


import java.util.List;

public class DaKaItemBean {


    private String head;
    private String user_id;
    private String user_name;
    private String nick_name;
    private String become_vip_date = "0";//成为Vip时间
    private List<FollowListBean> follow_list;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public String getBecome_vip_date() {
        return become_vip_date;
    }

    public void setBecome_vip_date(String become_vip_date) {
        this.become_vip_date = become_vip_date;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public List<FollowListBean> getFollow_list() {
        return follow_list;
    }

    public void setFollow_list(List<FollowListBean> follow_list) {
        this.follow_list = follow_list;
    }


}