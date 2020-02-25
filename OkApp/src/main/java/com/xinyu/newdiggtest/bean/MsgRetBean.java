package com.xinyu.newdiggtest.bean;

import java.util.List;

public class MsgRetBean {


    private OpBean op;
    private List<HomeMsgBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<HomeMsgBean> getData() {
        return data;
    }

    public void setData(List<HomeMsgBean> data) {
        this.data = data;
    }


}
