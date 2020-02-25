package com.xinyu.newdiggtest.bean;

import java.util.List;

public class AnswerBean {
    private String f_answer_plan_id;
    private String f_audio;
    private String f_finish_time;
    private String f_state;


    private String f_title = "";

    private String f_target_name;

    private String f_content;
    private String f_audio_length;
    private HomeUser user;
    private String f_comment;
    private Object f_img;//用通用类型接收
    private List<ImImgBean> f_imgObj;


    private List<DakaBottowItem> targetlikes;
    private List<DakaBottowItem> targetcomment;
    private List<DashangDataBean> excitation;//打赏相关



    public String getF_answer_plan_id() {
        return f_answer_plan_id;
    }

    public void setF_answer_plan_id(String f_answer_plan_id) {
        this.f_answer_plan_id = f_answer_plan_id;
    }

    public String getF_title() {
        return f_title;
    }

    public void setF_title(String f_title) {
        this.f_title = f_title;
    }

    public String getF_audio() {
        return f_audio;
    }

    public void setF_audio(String f_audio) {
        this.f_audio = f_audio;
    }

    public String getF_finish_time() {
        return f_finish_time;
    }

    public void setF_finish_time(String f_finish_time) {
        this.f_finish_time = f_finish_time;
    }

    public String getF_state() {
        return f_state;
    }

    public void setF_state(String f_state) {
        this.f_state = f_state;
    }

    public String getF_target_name() {
        return f_target_name;
    }

    public void setF_target_name(String f_target_name) {
        this.f_target_name = f_target_name;
    }

    public String getF_content() {
        return f_content;
    }

    public void setF_content(String f_content) {
        this.f_content = f_content;
    }

    public Object getF_img() {
        return f_img;
    }

    public void setF_img(Object f_img) {
        this.f_img = f_img;
    }

    public List<DashangDataBean> getExcitation() {
        return excitation;
    }

    public void setExcitation(List<DashangDataBean> excitation) {
        this.excitation = excitation;
    }

    public List<ImImgBean> getF_imgObj() {
        return f_imgObj;
    }

    public void setF_imgObj(List<ImImgBean> f_imgObj) {
        this.f_imgObj = f_imgObj;
    }


    public String getF_audio_length() {
        return f_audio_length;
    }

    public void setF_audio_length(String f_audio_length) {
        this.f_audio_length = f_audio_length;
    }

    public HomeUser getUser() {
        return user;
    }

    public void setUser(HomeUser user) {
        this.user = user;
    }

    public String getF_comment() {
        return f_comment;
    }

    public void setF_comment(String f_comment) {
        this.f_comment = f_comment;
    }


    public List<DakaBottowItem> getTargetlikes() {
        return targetlikes;
    }

    public void setTargetlikes(List<DakaBottowItem> targetlikes) {
        this.targetlikes = targetlikes;
    }

    public List<DakaBottowItem> getTargetcomment() {
        return targetcomment;
    }

    public void setTargetcomment(List<DakaBottowItem> targetcomment) {
        this.targetcomment = targetcomment;
    }


}
