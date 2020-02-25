package com.xinyu.newdiggtest.bean;

import java.io.Serializable;


public class WxUserBean implements Serializable {

    private String user_id;
    private String head = "";
    private String nickname = "";
    private String mobile = "";//手机号码
    private String is_online;

    public String getEmail() {
        return email;
    }

    String city = "";

    String email = "";

    private String custom_head = "";
    String sex = "M";
    String province = "";
    String subsidiary_expand = "";
    String first_rec_users = "";

    public String getCustom_head() {
        return custom_head;
    }

    public void setCustom_head(String custom_head) {
        this.custom_head = custom_head;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getSubsidiary_expand() {
        return subsidiary_expand;
    }

    public void setSubsidiary_expand(String subsidiary_expand) {
        this.subsidiary_expand = subsidiary_expand;
    }

    public String getFirst_rec_users() {
        return first_rec_users;
    }

    public void setFirst_rec_users(String first_rec_users) {
        this.first_rec_users = first_rec_users;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIs_online() {
        return is_online;
    }

    public void setIs_online(String is_online) {
        this.is_online = is_online;
    }
}