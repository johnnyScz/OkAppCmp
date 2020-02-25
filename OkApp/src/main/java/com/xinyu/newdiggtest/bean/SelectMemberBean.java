package com.xinyu.newdiggtest.bean;


import java.util.List;
import java.util.Map;

public class SelectMemberBean {


    private Map<String, Object> org;

    private OpBean op;
    private List<OutBean> data;


    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<OutBean> getData() {
        return data;
    }

    public void setData(List<OutBean> data) {
        this.data = data;
    }

    public Map<String, Object> getOrg() {
        return org;
    }


    static class OutBean {

        String empnum;
        String f_name;
        String f_id;

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


    }


}
