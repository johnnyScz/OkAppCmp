package com.xinyu.newdiggtest.bean;

import java.io.Serializable;
import java.util.List;

public class MemberRetBean {

    private OpBean op;
    private List<MemberOutBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<MemberOutBean> getData() {
        return data;
    }

    public void setData(List<MemberOutBean> data) {
        this.data = data;
    }


    public static class MemberOutBean implements Serializable {

        private MemberBean userinfo;

        boolean isSelect = false;

        int isUnable = 0;//是否不可选

        public int getIsUnable() {
            return isUnable;
        }

        public void setIsUnable(int isUnable) {
            this.isUnable = isUnable;
        }


        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public MemberBean getUserinfo() {
            return userinfo;
        }

        public void setUserinfo(MemberBean userinfo) {
            this.userinfo = userinfo;
        }

    }
}
