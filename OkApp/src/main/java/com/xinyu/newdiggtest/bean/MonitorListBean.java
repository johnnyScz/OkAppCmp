package com.xinyu.newdiggtest.bean;

import java.util.List;

public class MonitorListBean {

    private OpBean op;
    private List<MonitorParentBean> datalist;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<MonitorParentBean> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<MonitorParentBean> datalist) {
        this.datalist = datalist;
    }




}
