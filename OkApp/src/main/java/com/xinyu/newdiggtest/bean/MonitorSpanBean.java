package com.xinyu.newdiggtest.bean;

import java.util.List;

public class MonitorSpanBean {

    private OpBean op;
    private List<DataParentBean> datalist;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<DataParentBean> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<DataParentBean> datalist) {
        this.datalist = datalist;
    }


    public static class DataParentBean {

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
        private MonitorUserBean user;
        private String msg_time;
        private List<MonitorSpanChildBean> member_list;

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

        public MonitorUserBean getUser() {
            return user;
        }

        public void setUser(MonitorUserBean user) {
            this.user = user;
        }

        public String getMsg_time() {
            return msg_time;
        }

        public void setMsg_time(String msg_time) {
            this.msg_time = msg_time;
        }

        public List<MonitorSpanChildBean> getMember_list() {
            return member_list;
        }


    }
}
