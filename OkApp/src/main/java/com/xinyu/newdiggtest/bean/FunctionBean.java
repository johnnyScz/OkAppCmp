package com.xinyu.newdiggtest.bean;

public class FunctionBean {

    private String f_id;
    private String f_group_id;
    private String f_function_id;
    private String f_create_by;
    private String f_create_date;


    private String f_url = "";//进入下一层

    private String f_function_name = "";


    private String f_update_by;
    private String f_update_date;
    private String f_del_flag;


    public String getF_id() {
        return f_id;
    }

    public void setF_id(String f_id) {
        this.f_id = f_id;
    }

    public String getF_group_id() {
        return f_group_id;
    }

    public void setF_group_id(String f_group_id) {
        this.f_group_id = f_group_id;
    }

    public String getF_function_id() {
        return f_function_id;
    }


    public String getF_url() {
        return f_url;
    }


    public String getF_create_by() {
        return f_create_by;
    }

    public void setF_create_by(String f_create_by) {
        this.f_create_by = f_create_by;
    }

    public String getF_create_date() {
        return f_create_date;
    }

    public void setF_create_date(String f_create_date) {
        this.f_create_date = f_create_date;
    }

    public String getF_update_by() {
        return f_update_by;
    }

    public void setF_update_by(String f_update_by) {
        this.f_update_by = f_update_by;
    }

    public String getF_update_date() {
        return f_update_date;
    }


    public String getF_function_name() {
        return f_function_name;
    }

    public void setF_function_name(String f_function_name) {
        this.f_function_name = f_function_name;
    }

    public void setF_update_date(String f_update_date) {
        this.f_update_date = f_update_date;
    }

    public String getF_del_flag() {
        return f_del_flag;
    }

    public void setF_del_flag(String f_del_flag) {
        this.f_del_flag = f_del_flag;
    }


}
