package com.xinyu.newdiggtest.net.bean;

import com.xinyu.newdiggtest.bean.OpBean;

import java.util.List;

public class CmpBeanCard {

    private OpBean op;
    private List<CardChildBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<CardChildBean> getData() {
        return data;
    }

    public void setData(List<CardChildBean> data) {
        this.data = data;
    }


}
