package com.xinyu.newdiggtest.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class HomeMsgBean implements MultiItemEntity {

    @Override
    public int getItemType() {
        if (type.equals("12")) {
            itemType = 12;
        } else {
            itemType = 1;
        }
        return itemType;
    }

    private int itemType = 1;//视图type
    private String create_time;
    private String flag;
    private String from_user;
    private String id;
    private String is_read = "0";
    private String name;
    private String type = "1";
    private String user;
    private String user_name;
    private String aim_id;
    private String nickname;
    private String header;
    private String aim_name;
    private String money;
    private String aim_start_time;
    private String aim_end_time;
    private String user_id;
    private String from_id;
    private String from_name;


    private String rate_type;//挑战金还是奖励金

    private String rated = "0";//是否已经评分 1表示已经评分，0表示没有评分
    private String from_nickname;
    private String from_head;
    private String wish;//祝福语

    public String getWish() {
        return wish;
    }

    public void setWish(String wish) {
        this.wish = wish;
    }


    public String getAim_money() {
        return money;
    }

    public void setAim_money(String aim_money) {
        this.money = aim_money;
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

    public String getFrom_name() {
        return from_name;
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }

    public String getFrom_nickname() {
        return from_nickname;
    }

    public void setFrom_nickname(String from_nickname) {
        this.from_nickname = from_nickname;
    }

    public String getFrom_head() {
        return from_head;
    }

    public void setFrom_head(String from_head) {
        this.from_head = from_head;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getAim_start_time() {
        return aim_start_time;
    }

    public void setAim_start_time(String aim_start_time) {
        this.aim_start_time = aim_start_time;
    }

    public String getAim_end_time() {
        return aim_end_time;
    }

    public void setAim_end_time(String aim_end_time) {
        this.aim_end_time = aim_end_time;
    }


    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getAim_name() {
        return aim_name;
    }

    public void setAim_name(String aim_name) {
        this.aim_name = aim_name;
    }


    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFrom_user() {
        return from_user;
    }

    public void setFrom_user(String from_user) {
        this.from_user = from_user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate_type() {
        return rate_type;
    }

    public void setRate_type(String rate_type) {
        this.rate_type = rate_type;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAim_id() {
        return aim_id;
    }

    public void setAim_id(String aim_id) {
        this.aim_id = aim_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
