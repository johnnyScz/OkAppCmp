package com.xinyu.newdiggtest.net.bean;

import com.xinyu.newdiggtest.bean.OpBean;

import java.util.List;

public class CmpCardBean {


    private OpBean op;
    private List<CardBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<CardBean> getData() {
        return data;
    }

    public void setData(List<CardBean> data) {
        this.data = data;
    }



}
