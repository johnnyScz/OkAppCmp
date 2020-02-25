package com.xinyu.newdiggtest.bean;

public class MsgNewBean {


    private String topic;
    private String count;
    private LatestmsgBean latestmsg;

    private String latesttime;


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

    public LatestmsgBean getLatestmsg() {
        return latestmsg;
    }

    public void setLatestmsg(LatestmsgBean latestmsg) {
        this.latestmsg = latestmsg;
    }


    public String getLatesttime() {
        return latesttime;
    }

    public void setLatesttime(String latesttime) {
        this.latesttime = latesttime;
    }

    public static class LatestmsgBean {

        private String user_id;
        private CommonUserBean user;
        private String room_id;
        private String room_type;
        private Object content;
        private String msg_type;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public CommonUserBean getUser() {
            return user;
        }


        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getRoom_type() {
            return room_type;
        }

        public void setRoom_type(String room_type) {
            this.room_type = room_type;
        }

        public Object getContent() {
            return content;
        }

        public String getMsg_type() {
            return msg_type;
        }

        public void setMsg_type(String msg_type) {
            this.msg_type = msg_type;
        }


    }
}
