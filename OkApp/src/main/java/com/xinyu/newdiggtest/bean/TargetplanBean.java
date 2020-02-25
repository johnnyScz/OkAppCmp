package com.xinyu.newdiggtest.bean;

import java.util.List;

public class TargetplanBean {

    private String likescount;
    private String commentcount;
    private String f_createname;//创建者的名字
    private String become_vip_date;//成为vip的时间
    private String f_createuser;//创建者的userid

    private String f_end_date;
    private String f_createtime;
    private int f_days;

    private String count = "0";
    private String f_name;
    private String f_repeat_date;


    private String f_reminder_time;

    private String f_rid;
    private String f_pid;


    private String f_is_share = "1";// 1.公开  2.私密

    private String f_uuid;
    private String f_updateuser;
    private Object f_supervisor;//监督人
    private String f_type;
    private String f_start_date;
    private String f_updatetime;
    private String f_reward = "0";//奖励金

    private String f_state;//目标是否结束（0 进行中 1已结束）

    private String f_fine = "";//挑战金
    private String f_class_id = "";//图标位置

    private ChildFromBean from;//子目标有该字段

    public List<ChildTargetBean> getChild() {
        return child;
    }


    public Object getF_supervisor() {
        return f_supervisor;
    }

    public void setF_supervisor(String f_supervisor) {
        this.f_supervisor = f_supervisor;
    }

    public String getBecome_vip_date() {
        return become_vip_date;
    }

    public void setBecome_vip_date(String become_vip_date) {
        this.become_vip_date = become_vip_date;
    }

    public ChildFromBean getFrom() {
        return from;
    }

    public void setFrom(ChildFromBean from) {
        this.from = from;
    }

    public String getF_createuser() {
        return f_createuser;
    }

    public void setF_createuser(String f_createuser) {
        this.f_createuser = f_createuser;
    }

    public String getF_rid() {
        return f_rid;
    }

    public void setF_rid(String f_rid) {
        this.f_rid = f_rid;
    }

    public String getF_pid() {
        return f_pid;
    }

    public void setF_pid(String f_pid) {
        this.f_pid = f_pid;
    }

    public String getF_end_date() {
        return f_end_date;
    }

    public void setF_end_date(String f_end_date) {
        this.f_end_date = f_end_date;
    }

    public String getF_is_share() {
        return f_is_share;
    }

    public void setF_is_share(String f_is_share) {
        this.f_is_share = f_is_share;
    }

    public String getF_createtime() {
        return f_createtime;
    }

    public void setF_createtime(String f_createtime) {
        this.f_createtime = f_createtime;
    }

    public int getF_days() {
        return f_days;
    }

    public void setF_days(int f_days) {
        this.f_days = f_days;
    }

    public void setChild(List<ChildTargetBean> child) {
        this.child = child;
    }

    private List<ChildTargetBean> child;


    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getF_class_id() {
        return f_class_id;
    }

    public void setF_class_id(String f_class_id) {
        this.f_class_id = f_class_id;
    }

    public String getF_repeat_date() {
        return f_repeat_date;
    }

    public void setF_repeat_date(String f_repeat_date) {
        this.f_repeat_date = f_repeat_date;
    }

    public String getF_uuid() {
        return f_uuid;
    }

    public void setF_uuid(String f_uuid) {
        this.f_uuid = f_uuid;
    }

    public String getF_updateuser() {
        return f_updateuser;
    }

    public void setF_updateuser(String f_updateuser) {
        this.f_updateuser = f_updateuser;
    }

    public String getF_type() {
        return f_type;
    }

    public void setF_type(String f_type) {
        this.f_type = f_type;
    }

    public String getF_start_date() {
        return f_start_date;
    }

    public void setF_start_date(String f_start_date) {
        this.f_start_date = f_start_date;
    }

    public String getF_updatetime() {
        return f_updatetime;
    }

    public void setF_updatetime(String f_updatetime) {
        this.f_updatetime = f_updatetime;
    }

    public String getF_reward() {
        return f_reward;
    }

    public void setF_reward(String f_reward) {
        this.f_reward = f_reward;
    }

    public String getF_reminder_time() {
        return f_reminder_time;
    }

    public void setF_reminder_time(String f_reminder_time) {
        this.f_reminder_time = f_reminder_time;
    }

    public String getF_state() {
        return f_state;
    }

    public void setF_state(String f_state) {
        this.f_state = f_state;
    }

    public String getLikescount() {
        return likescount;
    }

    public void setLikescount(String likescount) {
        this.likescount = likescount;
    }

    public String getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(String commentcount) {
        this.commentcount = commentcount;
    }

    public String getF_createname() {
        return f_createname;
    }

    public void setF_createname(String f_createname) {
        this.f_createname = f_createname;
    }


    public String getF_fine() {
        return f_fine;
    }

    public void setF_fine(String f_fine) {
        this.f_fine = f_fine;
    }
}
