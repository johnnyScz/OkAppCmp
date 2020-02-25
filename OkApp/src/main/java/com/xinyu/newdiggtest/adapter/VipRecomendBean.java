package com.xinyu.newdiggtest.adapter;

public class VipRecomendBean {


//    private String f_become_vip_date;//是否是vip（皇冠 亮或灰）
    private String is_use;//
    private String f_download_app_flag;

    private String nickname;
    private String f_head;
    private String f_user_id;


    private String f_city;
    private String f_province;


    public String getF_recharge_vip_date() {
        return f_recharge_vip_date;
    }

    public void setF_recharge_vip_date(String f_recharge_vip_date) {
        this.f_recharge_vip_date = f_recharge_vip_date;
    }

    private String f_recharge_vip_date;


    private String f_first_rec_users = "0";//一级推荐人
    private String f_second_rec_users = "0";//二级推荐人


    public String getF_first_rec_users() {
        return f_first_rec_users;
    }

    public void setF_first_rec_users(String f_first_rec_users) {
        this.f_first_rec_users = f_first_rec_users;
    }

    public String getF_second_rec_users() {
        return f_second_rec_users;
    }

    public String getF_city() {
        return f_city;
    }

    public void setF_city(String f_city) {
        this.f_city = f_city;
    }

    public String getF_province() {
        return f_province;
    }

    public void setF_province(String f_province) {
        this.f_province = f_province;
    }


    public void setF_second_rec_users(String f_second_rec_users) {
        this.f_second_rec_users = f_second_rec_users;
    }

    public String getRecharge_vip_date() {
        return f_recharge_vip_date;
    }

//    public String getF_become_vip_date() {
//        return f_become_vip_date;
//    }
//
//    public void setF_become_vip_date(String f_become_vip_date) {
//        this.f_become_vip_date = f_become_vip_date;
//    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getF_head() {
        return f_head;
    }

    public void setF_head(String f_head) {
        this.f_head = f_head;
    }

    public String getF_download_app_flag() {
        return f_download_app_flag;
    }

    public void setF_download_app_flag(String f_download_app_flag) {
        this.f_download_app_flag = f_download_app_flag;
    }

    public String getF_user_id() {
        return f_user_id;
    }

    public void setF_user_id(String f_user_id) {
        this.f_user_id = f_user_id;
    }

    public String getIs_use() {
        return is_use;
    }

    public void setIs_use(String is_use) {
        this.is_use = is_use;
    }
}