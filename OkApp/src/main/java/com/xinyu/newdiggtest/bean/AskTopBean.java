package com.xinyu.newdiggtest.bean;

import java.util.List;

public class AskTopBean {

    private String f_plan_date;
    private String f_target_uuid;
    private String f_title="";
    private String f_reminder_time;
    private String f_target_end_date;
    private String f_target_start_date;
    private String f_pid;
    private String f_state = "0";
    private String f_plan_id;
    private String f_target_name;
    private String f_executor;
    private List<BaseUser> executorlist;


    private BaseUser from_user;
    private String f_is_need = "";
    private List<ImImgBean> f_watch_img;
    private int childCount = 0;//应答的个数

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public List<BaseUser> getExecutorlist() {
        return executorlist;
    }

    public void setExecutorlist(List<BaseUser> executorlist) {
        this.executorlist = executorlist;
    }

    public String getF_executor() {
        return f_executor;
    }

    public void setF_executor(String f_executor) {
        this.f_executor = f_executor;
    }

    public BaseUser getFrom_user() {
        return from_user;
    }

    public void setFrom_user(BaseUser from_user) {
        this.from_user = from_user;
    }

    public String getF_plan_date() {
        return f_plan_date;
    }

    public void setF_plan_date(String f_plan_date) {
        this.f_plan_date = f_plan_date;
    }

    public String getF_target_uuid() {
        return f_target_uuid;
    }

    public void setF_target_uuid(String f_target_uuid) {
        this.f_target_uuid = f_target_uuid;
    }

    public String getF_title() {
        return f_title;
    }

    public void setF_title(String f_title) {
        this.f_title = f_title;
    }

    public String getF_reminder_time() {
        return f_reminder_time;
    }

    public void setF_reminder_time(String f_reminder_time) {
        this.f_reminder_time = f_reminder_time;
    }

    public String getF_target_end_date() {
        return f_target_end_date;
    }

    public void setF_target_end_date(String f_target_end_date) {
        this.f_target_end_date = f_target_end_date;
    }

    public String getF_target_start_date() {
        return f_target_start_date;
    }

    public void setF_target_start_date(String f_target_start_date) {
        this.f_target_start_date = f_target_start_date;
    }

    public String getF_pid() {
        return f_pid;
    }

    public void setF_pid(String f_pid) {
        this.f_pid = f_pid;
    }

    public String getF_state() {
        return f_state;
    }

    public void setF_state(String f_state) {
        this.f_state = f_state;
    }

    public String getF_plan_id() {
        return f_plan_id;
    }

    public void setF_plan_id(String f_plan_id) {
        this.f_plan_id = f_plan_id;
    }

    public String getF_target_name() {
        return f_target_name;
    }

    public void setF_target_name(String f_target_name) {
        this.f_target_name = f_target_name;
    }

    public String getF_is_need() {
        return f_is_need;
    }

    public void setF_is_need(String f_is_need) {
        this.f_is_need = f_is_need;
    }

    public List<ImImgBean> getF_watch_img() {
        return f_watch_img;
    }

    public void setF_watch_img(List<ImImgBean> f_watch_img) {
        this.f_watch_img = f_watch_img;
    }


}
