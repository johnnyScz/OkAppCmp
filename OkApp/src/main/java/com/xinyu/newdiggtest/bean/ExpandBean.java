package com.xinyu.newdiggtest.bean;

import java.util.List;

public class ExpandBean {


    private OpBean op;
    private List<Expand1LevBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<Expand1LevBean> getData() {
        return data;
    }

    public void setData(List<Expand1LevBean> data) {
        this.data = data;
    }


}
