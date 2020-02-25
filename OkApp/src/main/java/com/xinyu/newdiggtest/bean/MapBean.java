package com.xinyu.newdiggtest.bean;

import java.util.List;

public class MapBean {


    String orgId;
    List<MemberRetBean.MemberOutBean> data;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public List<MemberRetBean.MemberOutBean> getData() {
        return data;
    }

    public void setData(List<MemberRetBean.MemberOutBean> data) {
        this.data = data;
    }


}
