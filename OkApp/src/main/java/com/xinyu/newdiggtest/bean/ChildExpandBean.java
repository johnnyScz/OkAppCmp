package com.xinyu.newdiggtest.bean;


import java.util.List;

public class ChildExpandBean {


    private String empnum = "0";
    private String f_rid;
    private String f_company_id;
    //    private String f_createtime;
    private String f_id;
    private String f_name;
    private String f_createuser;
//    private String f_updatetime;
    private String f_updateuser;


    private List<ChildExpandBean> child;

    public String getEmpnum() {
        return empnum;
    }

    public void setEmpnum(String empnum) {
        this.empnum = empnum;
    }

    public String getF_rid() {
        return f_rid;
    }

    public void setF_rid(String f_rid) {
        this.f_rid = f_rid;
    }

    public String getF_company_id() {
        return f_company_id;
    }

    public void setF_company_id(String f_company_id) {
        this.f_company_id = f_company_id;
    }

//    public String getF_createtime() {
//        return f_createtime;
//    }
//
//    public void setF_createtime(String f_createtime) {
//        this.f_createtime = f_createtime;
//    }

    public String getF_id() {
        return f_id;
    }

    public void setF_id(String f_id) {
        this.f_id = f_id;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getF_createuser() {
        return f_createuser;
    }

    public void setF_createuser(String f_createuser) {
        this.f_createuser = f_createuser;
    }

//    public String getF_updatetime() {
//        return f_updatetime;
//    }
//
//    public void setF_updatetime(String f_updatetime) {
//        this.f_updatetime = f_updatetime;
//    }

    public String getF_updateuser() {
        return f_updateuser;
    }

    public void setF_updateuser(String f_updateuser) {
        this.f_updateuser = f_updateuser;
    }


    public List<ChildExpandBean> getChild() {
        return child;
    }

    public void setChild(List<ChildExpandBean> child) {
        this.child = child;
    }


    public int getLevel() {
        return getLevel();
    }


}
