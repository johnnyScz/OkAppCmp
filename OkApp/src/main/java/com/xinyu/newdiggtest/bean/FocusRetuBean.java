package com.xinyu.newdiggtest.bean;

import java.util.List;

public class FocusRetuBean {


    private OpBean op;
    private List<FocusTodoBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<FocusTodoBean> getData() {
        return data;
    }

    public void setData(List<FocusTodoBean> data) {
        this.data = data;
    }


}
