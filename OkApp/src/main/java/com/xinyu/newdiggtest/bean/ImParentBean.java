package com.xinyu.newdiggtest.bean;

import java.util.List;

public class ImParentBean {

    private OpBean op;
    private List<ImItemMsgBean> data;


    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<ImItemMsgBean> getData() {
        return data;
    }

    public void setData(List<ImItemMsgBean> data) {
        this.data = data;
    }


}
