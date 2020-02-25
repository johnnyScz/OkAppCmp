package com.xinyu.newdiggtest.bean;


import com.chad.library.adapter.base.entity.MultiItemEntity;

public class TargetBean implements MultiItemEntity {

    CommonUserBean user;//为了方便显示其他人的图标
    private int showType;//显示类型(头部还是 item)

    private String end_date;
    private String type;
    private String repeat_date;
    private String name;
    private int days;
    private String target_uuid;
    private String createuser;
    private String createtime;
    private String updatetime;
    private String completion_degree;
    private String state = "0";//0进行中1 已结束
    private String start_date;
    private String updateuser;
    private String class_id;//图标类别


    private String follow_count;//关注的数量


    private String userId;//该目标的userID

    private int fine;
    private int comment_count;


    private int like_count;//点赞
    private int count;
    private String reward;


    private String toUser;//付给别人的id

    public CommonUserBean getUser() {
        return user;
    }

    public void setUser(CommonUserBean user) {
        this.user = user;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public String getFollow_count() {
        return follow_count;
    }

    public void setFollow_count(String follow_count) {
        this.follow_count = follow_count;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTarget_uuid() {
        return target_uuid;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public void setTarget_uuid(String target_uuid) {
        this.target_uuid = target_uuid;
    }

    public String getCreateuser() {
        return createuser;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    public String getCompletion_degree() {
        return completion_degree;
    }

    public void setCompletion_degree(String completion_degree) {
        this.completion_degree = completion_degree;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getUpdateuser() {
        return updateuser;
    }

    public void setUpdateuser(String updateuser) {
        this.updateuser = updateuser;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRepeat_date() {
        return repeat_date;
    }

    public void setRepeat_date(String repeat_date) {
        this.repeat_date = repeat_date;
    }

    @Override
    public int getItemType() {
        return showType;
    }
}
