package com.xinyu.newdiggtest.bean;

import java.util.List;

public class HomeMsgRetBean {


    private OpBean op;
    private List<MsgNewHomeBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<MsgNewHomeBean> getData() {
        return data;
    }

    public void setData(List<MsgNewHomeBean> data) {
        this.data = data;
    }



}
