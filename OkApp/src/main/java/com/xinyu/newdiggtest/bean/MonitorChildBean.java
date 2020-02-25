package com.xinyu.newdiggtest.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.xinyu.newdiggtest.adapter.FenZuAdapter;

import java.io.Serializable;

public class MonitorChildBean implements MultiItemEntity,Serializable {

    private String member_type;
    private String valid_flag;
    private String user_id;
    private int checkTag = 0;
    private CommonUserBean user;

    public String getMember_type() {
        return member_type;
    }

    public void setMember_type(String member_type) {
        this.member_type = member_type;
    }

    public String getValid_flag() {
        return valid_flag;
    }

    public void setValid_flag(String valid_flag) {
        this.valid_flag = valid_flag;
    }

    public String getUser_id() {
        return user_id;
    }

    public int getCheckTag() {
        return checkTag;
    }

    public void setCheckTag(int checkTag) {
        this.checkTag = checkTag;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public CommonUserBean getUser() {
        return user;
    }

    public void setUser(CommonUserBean user) {
        this.user = user;
    }


    @Override
    public int getItemType() {
        return FenZuAdapter.TYPE_PERSON;
    }
}
