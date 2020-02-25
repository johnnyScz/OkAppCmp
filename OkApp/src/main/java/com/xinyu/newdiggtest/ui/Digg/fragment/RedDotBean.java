package com.xinyu.newdiggtest.ui.Digg.fragment;

import com.xinyu.newdiggtest.bean.BaseCommonUser;

public class RedDotBean {


    private String room_id;
    private String room_name;
    private String room_type;
    private String room_property;
    private String room_level;
    private String room_head;
    private String union_id;
    private String room_member_number;
    private String main_object;
    private String create_time;
    private BaseCommonUser user;
    private String last_msg = "";
    private String sender_id;
    private String msg_time;
    private DetailBean detail;

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }

    public String getRoom_property() {
        return room_property;
    }

    public void setRoom_property(String room_property) {
        this.room_property = room_property;
    }

    public String getRoom_level() {
        return room_level;
    }

    public void setRoom_level(String room_level) {
        this.room_level = room_level;
    }

    public String getRoom_head() {
        return room_head;
    }

    public void setRoom_head(String room_head) {
        this.room_head = room_head;
    }

    public String getUnion_id() {
        return union_id;
    }

    public void setUnion_id(String union_id) {
        this.union_id = union_id;
    }

    public String getRoom_member_number() {
        return room_member_number;
    }

    public void setRoom_member_number(String room_member_number) {
        this.room_member_number = room_member_number;
    }

    public String getMain_object() {
        return main_object;
    }

    public void setMain_object(String main_object) {
        this.main_object = main_object;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public BaseCommonUser getUser() {
        return user;
    }

    public void setUser(BaseCommonUser user) {
        this.user = user;
    }

    public String getLast_msg() {
        return last_msg;
    }

    public void setLast_msg(String last_msg) {
        this.last_msg = last_msg;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getMsg_time() {
        return msg_time;
    }

    public void setMsg_time(String msg_time) {
        this.msg_time = msg_time;
    }

    public DetailBean getDetail() {
        return detail;
    }

    public void setDetail(DetailBean detail) {
        this.detail = detail;
    }


    public static class DetailBean {

        private String topic;
        private String count = "0";

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }


}
