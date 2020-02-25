package com.xinyu.newdiggtest.bean;

import java.util.List;

public class RewardReturnBean {

    private OpBean op;
    private String countmoney;
    private List<RewardListBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public String getCountmoney() {
        return countmoney;
    }

    public void setCountmoney(String countmoney) {
        this.countmoney = countmoney;
    }

    public List<RewardListBean> getData() {
        return data;
    }

    public void setData(List<RewardListBean> data) {
        this.data = data;
    }


}
