package com.xinyu.newdiggtest.net.bean;

import com.xinyu.newdiggtest.bean.OpBean;

public class BasePraiseBean {

    private OpBean op;
    private Object data;


    private String islike;

    private String likecount;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public Object getData() {
        return data;
    }

    public String getIslike() {
        return islike;
    }

    public String getLikecount() {
        return likecount;
    }
}
