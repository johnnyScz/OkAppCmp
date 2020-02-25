package com.xinyu.newdiggtest.bean;


import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class FollowListBean implements MultiItemEntity {

    private int editPos = -1;
    int tyep = 1;
    int isVip = -1;//是否是Vip
    private String f_plan_date;
    private String f_executor = "0";
    private long f_createtime;
    private String f_state;//是否已经打卡
    private String f_content;
    private String f_img;

    private String answerId = "";//判断是否是代办


    private String f_plan_id;
    private String f_target_name = "";
    private String f_target_uuid;
    private String f_target_end_date = "";
    private String f_target_start_date = "";
    private String childUser = "";//字条目的ｕｓｅｉｄ


    private String become_vip_date = "0";//成为Vip 时间


    private String cratename = "";//目标创建者
    private String f_class_id = "";//图标列表;
    private String f_comment;//自己打卡内容

    private String f_finish_time;//打卡时间


    private String f_updatetime;//更新时间

    String f_end_date;
    String f_start_date;


    String nick_name = "";
    String head = "";
    String user_id = "";

    private String f_watch_img = "";
    private BaseUser user;


    private String f_title;


    private List<DakaBottowItem> targetlikes;//点赞
    private List<DakaBottowItem> targetcomment;//评论
    private List<DashangDataBean> excitation;//打赏

    //------外层----------
    private String imgUrl;//图像的url
    private String name;//关注人的名称

    public String getF_updatetime() {
        return f_updatetime;
    }

    public void setF_updatetime(String f_updatetime) {
        this.f_updatetime = f_updatetime;
    }

    public List<DashangDataBean> getExcitation() {
        return excitation;
    }

    public void setExcitation(List<DashangDataBean> excitation) {
        this.excitation = excitation;
    }

    public static final int ITEM_TYPR = 1;//自己打卡

    public static final int SELECTION_TYPR = 2;//别人的标题

    public static final int ITEM_TYPR_Other = 3;//别人的标题

    public String getF_target_name() {
        return f_target_name;
    }

    public String getF_target_uuid() {
        return f_target_uuid;
    }

    public String getBecome_vip_date() {
        return become_vip_date;
    }

    public void setBecome_vip_date(String become_vip_date) {
        this.become_vip_date = become_vip_date;
    }

    public BaseUser getUserBean() {
        return user;
    }


    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }


    public String getF_watch_img() {
        return f_watch_img;
    }

    public void setF_watch_img(String f_watch_img) {
        this.f_watch_img = f_watch_img;
    }


    public void setUser(BaseUser user) {
        this.user = user;
    }

    public String getF_title() {
        return f_title;
    }

    public void setF_title(String f_title) {
        this.f_title = f_title;
    }

    public void setF_target_uuid(String f_target_uuid) {
        this.f_target_uuid = f_target_uuid;
    }

    public String getF_img() {
        return f_img;
    }

    public void setF_img(String f_img) {
        this.f_img = f_img;
    }

    public String getCratename() {
        return cratename;
    }

    public void setCratename(String cratename) {
        this.cratename = cratename;
    }

    public String getF_comment() {
        return f_comment;
    }

    public void setF_comment(String f_comment) {
        this.f_comment = f_comment;
    }

    public void setF_target_name(String f_target_name) {
        this.f_target_name = f_target_name;
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

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public String getF_toUser() {
        return childUser;
    }

    public void setF_toUser(String f_toUser) {
        this.childUser = f_toUser;
    }

    public void setTyep(int mtype) {
        this.tyep = mtype;
    }


    @Override
    public int getItemType() {
        return tyep;
    }

    public String getF_finish_time() {
        return f_finish_time;
    }

    public void setF_finish_time(String f_finish_time) {
        this.f_finish_time = f_finish_time;
    }

    public String getF_class_id() {
        return f_class_id;
    }

    public void setF_class_id(String f_class_id) {
        this.f_class_id = f_class_id;
    }


    public int getEditPos() {
        return editPos;
    }

    public void setEditPos(int editPos) {
        this.editPos = editPos;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getF_plan_date() {
        return f_plan_date;
    }

    public void setF_plan_date(String f_plan_date) {
        this.f_plan_date = f_plan_date;
    }

    public String getF_executor() {
        return f_executor;
    }

    public void setF_executor(String f_executor) {
        this.f_executor = f_executor;
    }

    public long getF_createtime() {
        return f_createtime;
    }

    public void setF_createtime(long f_createtime) {
        this.f_createtime = f_createtime;
    }

    public String getF_state() {
        return f_state;
    }

    public void setF_state(String f_state) {
        this.f_state = f_state;
    }

    public String getF_content() {
        return f_content;
    }

    public void setF_content(String f_content) {
        this.f_content = f_content;
    }

    public String getF_plan_id() {
        return f_plan_id;
    }

    public void setF_plan_id(String f_plan_id) {
        this.f_plan_id = f_plan_id;
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

    //-----------群目标的关注----------


    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getF_end_date() {
        return f_end_date;
    }

    public void setF_end_date(String f_end_date) {
        this.f_end_date = f_end_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getF_start_date() {
        return f_start_date;
    }

    public void setF_start_date(String f_start_date) {
        this.f_start_date = f_start_date;
    }

}