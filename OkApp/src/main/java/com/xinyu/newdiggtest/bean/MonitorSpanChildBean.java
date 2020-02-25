package com.xinyu.newdiggtest.bean;

public class MonitorSpanChildBean {

    private String member_type;
    private String valid_flag;
    private String user_id;

    private MonitorUserBean user;
    private int checkTag = 0;

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

    public MonitorUserBean getUser() {
        return user;
    }

    public int getCheckTag() {
        return checkTag;
    }

    public void setCheckTag(int checkTag) {
        this.checkTag = checkTag;
    }


}
