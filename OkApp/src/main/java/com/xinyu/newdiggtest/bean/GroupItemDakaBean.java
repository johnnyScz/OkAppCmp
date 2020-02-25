package com.xinyu.newdiggtest.bean;

import java.util.List;

public class GroupItemDakaBean {

    //----------------------群目标用以下字段----------------------------------
    private String user_id;
    private CommonUserBean user;
    private List<TargetBean> targetplan;


    //----------------------关注目标需要用到以下字段---------------------
    private String head;
    private String nick_name;
    private List<TargetBean> follow_list;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public List<TargetBean> getFollow_list() {
        return follow_list;
    }

    public void setFollow_list(List<TargetBean> follow_list) {
        this.follow_list = follow_list;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public CommonUserBean getUser() {
        return user;
    }

    public void setUser(CommonUserBean user) {
        this.user = user;
    }

    public List<TargetBean> getTargetplan() {
        return targetplan;
    }

    public void setTargetplan(List<TargetBean> targetplan) {
        this.targetplan = targetplan;
    }

}
