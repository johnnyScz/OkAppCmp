package com.xinyu.newdiggtest.bean;

import com.xinyu.newdiggtest.adapter.RoomMemberBean;

import java.util.List;

public class GroupMemberBean {

    private OpBean op;
    private List<RoomMemberBean> datalist;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<RoomMemberBean> getMember_list() {
        return datalist;
    }

    public void setMember_list(List<RoomMemberBean> member_list) {
        this.datalist = member_list;
    }


}
