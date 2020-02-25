package com.xinyu.newdiggtest.bean;

import java.util.List;

public class TodoRetBean {


    private OpBean op;
    private List<RetListBean> data;

    private String total = "0";


    private List<RetListBean> invitedata;//邀办(别人邀请我的)


    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<RetListBean> getData() {
        return data;
    }

    public void setData(List<RetListBean> data) {
        this.data = data;
    }

    public List<RetListBean> getInvitedata() {
        return invitedata;
    }

    public void setInvitedata(List<RetListBean> invitedata) {
        this.invitedata = invitedata;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}
