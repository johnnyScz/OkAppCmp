package com.xinyu.newdiggtest.adapter.viewhelper;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class MemberTestBean implements MultiItemEntity {

    String name;
    String userId;
    String orgId;
    List<MemberTestBean> child;

    int type = 0;

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public List<MemberTestBean> getChild() {
        return child;
    }

    public void setChild(List<MemberTestBean> child) {
        this.child = child;
    }


    @Override
    public int getItemType() {
        return type;
    }
}
