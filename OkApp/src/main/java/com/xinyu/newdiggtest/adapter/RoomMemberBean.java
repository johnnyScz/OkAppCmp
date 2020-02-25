package com.xinyu.newdiggtest.adapter;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.xinyu.newdiggtest.bean.CommonUserBean;

public class RoomMemberBean implements MultiItemEntity {
    private String member_type;
    private String valid_flag;
    private String user_id;
    private CommonUserBean user;
    private int vType = 0;

    public int getvType() {
        return vType;
    }

    public void setvType(int vType) {
        this.vType = vType;
    }

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
        return vType;
    }
}
