package com.xinyu.newdiggtest.adapter.viewhelper;

import java.util.List;

public class ChildBean {

    String userId;
    String orgId = "11";
    String headUrl;
    String name;

    int viewType = 0;//1 需要请求接口数据(0,已经有子条目，不用请求子条目)


    List<ChildBean> child;


    public ChildBean(String grName) {
        this.name = grName;
    }


    public List<ChildBean> getChild() {
        return child;
    }

    public void setChild(List<ChildBean> child) {
        this.child = child;
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

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }


}
