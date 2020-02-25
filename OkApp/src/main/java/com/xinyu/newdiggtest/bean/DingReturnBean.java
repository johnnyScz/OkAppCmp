package com.xinyu.newdiggtest.bean;

import java.util.List;

public class DingReturnBean {

    private OpBean op;
    private List<TodoDingBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<TodoDingBean> getData() {
        return data;
    }

    public void setData(List<TodoDingBean> data) {
        this.data = data;
    }


}
