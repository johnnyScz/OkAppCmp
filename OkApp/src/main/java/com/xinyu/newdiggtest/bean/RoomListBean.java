package com.xinyu.newdiggtest.bean;

public class RoomListBean {

    private String room_id;
    private String room_name;
    private String room_type = "";//G 群聊 S 私聊
    private String room_property;
    private String room_level;
    private String room_head;//room的图标
    private String union_id;
    private String room_member_number;
    private String main_object = "";//群主编号(谁创建的群)
    private String create_time;
    private BaseCommonUser user;
    private DetailBean detail;


    private String msg_time;
    private String latstMsg = "";

    private String headUrl;
    private String msgCount = "";

    public String getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(String msgCount) {
        this.msgCount = msgCount;
    }

    public String getLatstMsg() {
        return latstMsg;
    }

    public void setLatstMsg(String latstMsg) {
        this.latstMsg = latstMsg;
    }


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

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public DetailBean getDetail() {
        return detail;
    }

//    public String getLast_msg() {
//        return last_msg;
//    }

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

    public String getMsg_time() {
        return msg_time;
    }

    public void setMsg_time(String msg_time) {
        this.msg_time = msg_time;
    }

    public static class UserBean {
        private String user_id;
        private String head;
        private String province;
        private String sex;
        private String wechat;
        private String name;
        private String nickname;
        private String is_online;
        private String type;
        private String register_time;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getWechat() {
            return wechat;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getIs_online() {
            return is_online;
        }

        public void setIs_online(String is_online) {
            this.is_online = is_online;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getRegister_time() {
            return register_time;
        }

        public void setRegister_time(String register_time) {
            this.register_time = register_time;
        }
    }

    public static class DetailBean {
        /**
         * topic : chat60
         * count : 0
         */

        private String topic;
        private String count;


        private Object latestmsg;

        public Object getLatestmsg() {
            return latestmsg;
        }

        public void setLatestmsg(String latestmsg) {
            this.latestmsg = latestmsg;
        }

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
