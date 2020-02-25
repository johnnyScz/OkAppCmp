package com.xinyu.newdiggtest.bean;

import java.util.List;

public class CompanyUserBean {


    private String id;
    private String user_id;
    private String company_id;
    private String update_time;
    private String create_time;
    private String status;
    private String type;
    private String company_name;
    private List<CommonUserBean> user_list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public List<CommonUserBean> getUser_list() {
        return user_list;
    }

    public void setUser_list(List<CommonUserBean> user_list) {
        this.user_list = user_list;
    }


}
