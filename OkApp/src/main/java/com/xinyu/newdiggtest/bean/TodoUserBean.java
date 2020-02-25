package com.xinyu.newdiggtest.bean;

import java.io.Serializable;

public class TodoUserBean implements Serializable {


    private String user_id;
    private String head = "";
    private String nickname = "";

    private String mobile;


    private String email;

    private String custom_head;
    private String name;
    private String company_id;


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getCustom_head() {
        return custom_head;
    }

    public void setCustom_head(String custom_head) {
        this.custom_head = custom_head;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }


}
