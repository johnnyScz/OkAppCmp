package com.xinyu.newdiggtest.bean;

import java.util.List;

public class DingRetBean {

    private OpBean op;
    private String ischeck;
    private List<FollowListBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public String getIscheck() {
        return ischeck;
    }

    public void setIscheck(String ischeck) {
        this.ischeck = ischeck;
    }

    public List<FollowListBean> getData() {
        return data;
    }

    public void setData(List<FollowListBean> data) {
        this.data = data;
    }


}
