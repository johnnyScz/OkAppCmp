package com.xinyu.newdiggtest.bean;

public class LatestMsg {

    private String type;
    private String user_id;
    private String from_id;
    private String aim_id;
    private String aim_st; //开始时间
    private String aim_et;//结束时间

    public String getAim_st() {
        return aim_st;
    }

    public void setAim_st(String aim_st) {
        this.aim_st = aim_st;
    }

    public String getAim_et() {
        return aim_et;
    }

    public void setAim_et(String aim_et) {
        this.aim_et = aim_et;
    }


    private String aim_name;


    private String from_nickname;
    private String wish;
    private String id;
    private String name;
    private BaseUser user;
    private BaseUser from;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public String getAim_id() {
        return aim_id;
    }

    public void setAim_id(String aim_id) {
        this.aim_id = aim_id;
    }

    public String getAim_name() {
        return aim_name;
    }

    public void setAim_name(String aim_name) {
        this.aim_name = aim_name;
    }


    public String getFrom_nickname() {
        return from_nickname;
    }

    public void setFrom_nickname(String from_nickname) {
        this.from_nickname = from_nickname;
    }

    public String getWish() {
        return wish;
    }

    public void setWish(String wish) {
        this.wish = wish;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BaseUser getUser() {
        return user;
    }

    public void setUser(BaseUser user) {
        this.user = user;
    }

    public BaseUser getFrom() {
        return from;
    }

    public void setFrom(BaseUser from) {
        this.from = from;
    }


}
