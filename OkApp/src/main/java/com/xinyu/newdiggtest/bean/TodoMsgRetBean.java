package com.xinyu.newdiggtest.bean;

import java.util.List;

public class TodoMsgRetBean {


    private OpBean op;
    private List<MsgTodoBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<MsgTodoBean> getData() {
        return data;
    }

    public void setData(List<MsgTodoBean> data) {
        this.data = data;
    }



}
