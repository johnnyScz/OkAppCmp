package com.xinyu.newdiggtest.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class TargetKindItemBean implements MultiItemEntity {


    private String f_del_flag;
    private String f_img;
    private String f_title;
    private String f_template_class_id;
    private String f_id;

    private int viewType = 0;

    public String getF_del_flag() {
        return f_del_flag;
    }

    public void setF_del_flag(String f_del_flag) {
        this.f_del_flag = f_del_flag;
    }

    public String getF_img() {
        return f_img;
    }

    public void setF_img(String f_img) {
        this.f_img = f_img;
    }

    public String getF_title() {
        return f_title;
    }

    public void setF_title(String f_title) {
        this.f_title = f_title;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }


    public String getF_template_class_id() {
        return f_template_class_id;
    }

    public void setF_template_class_id(String f_template_class_id) {
        this.f_template_class_id = f_template_class_id;
    }

    public String getF_id() {
        return f_id;
    }

    public void setF_id(String f_id) {
        this.f_id = f_id;
    }


    @Override
    public int getItemType() {
        return viewType;
    }
}
