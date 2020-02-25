package com.xinyu.newdiggtest.bean;

import java.util.List;

public class SelectDataBean {


    private String empnum;
    private String f_name;
    private String f_id;
//    private List<?> emplist;
    private List<?> child;

    public String getEmpnum() {
        return empnum;
    }

    public void setEmpnum(String empnum) {
        this.empnum = empnum;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getF_id() {
        return f_id;
    }

    public void setF_id(String f_id) {
        this.f_id = f_id;
    }

//    public List<?> getEmplist() {
//        return emplist;
//    }
//
//    public void setEmplist(List<?> emplist) {
//        this.emplist = emplist;
//    }

    public List<?> getChild() {
        return child;
    }

    public void setChild(List<?> child) {
        this.child = child;
    }
}
