package com.xinyu.newdiggtest.bean;


public class Plugins {

    private String f_id;
    private String f_group_id;
    private String f_plugin_type_id;
    private String f_plugin_type_name;
    private String f_create_by;
    private String f_create_date;
    private String f_update_by;
    private String f_update_date;
    private String f_del_flag;
    private String members;
    private Object pluginregister;


    private String f_title;

    private String lookPermision = "0";


    public String getF_id() {
        return f_id;
    }

    public void setF_id(String f_id) {
        this.f_id = f_id;
    }

    public String getF_title() {
        return f_title;
    }

    public String getF_group_id() {
        return f_group_id;
    }

    public void setF_group_id(String f_group_id) {
        this.f_group_id = f_group_id;
    }

    public String getF_plugin_type_id() {
        return f_plugin_type_id;
    }

    public void setF_plugin_type_id(String f_plugin_type_id) {
        this.f_plugin_type_id = f_plugin_type_id;
    }

    public String getF_plugin_type_name() {
        return f_plugin_type_name;
    }

    public void setF_plugin_type_name(String f_plugin_type_name) {
        this.f_plugin_type_name = f_plugin_type_name;
    }

    public String getLookPermision() {
        return lookPermision;
    }

    public void setLookPermision(String lookPermision) {
        this.lookPermision = lookPermision;
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

    public void setF_update_date(String f_update_date) {
        this.f_update_date = f_update_date;
    }

    public String getF_del_flag() {
        return f_del_flag;
    }

    public void setF_del_flag(String f_del_flag) {
        this.f_del_flag = f_del_flag;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public Object getPluginregister() {
        return pluginregister;
    }


}
