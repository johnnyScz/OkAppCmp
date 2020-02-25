package com.xinyu.newdiggtest.bean;

import java.util.List;

public class FocusBean {

    private OpBean op;
    private List<FollowListBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<FollowListBean> getData() {
        return data;
    }

    public void setData(List<FollowListBean> data) {
        this.data = data;
    }


}
