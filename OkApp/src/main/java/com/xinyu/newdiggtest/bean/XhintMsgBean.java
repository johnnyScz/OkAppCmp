package com.xinyu.newdiggtest.bean;

import java.util.List;

public class XhintMsgBean {

    private OpBean op;

    private List<MsgDataBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }


    public List<MsgDataBean> getData() {
        return data;
    }

    public void setData(List<MsgDataBean> data) {
        this.data = data;
    }


    public static class MsgDataBean {

        private String topic;
        private String count;
        private LatestMsgBean latestmsg;
        private String latesttime = "0";

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

        public LatestMsgBean getLatestmsg() {
            return latestmsg;
        }

        public void setLatestmsg(LatestMsgBean latestmsg) {
            this.latestmsg = latestmsg;
        }

        public String getLatesttime() {
            return latesttime;
        }

        public void setLatesttime(String latesttime) {
            this.latesttime = latesttime;
        }

        public static class LatestMsgBean {

            private String user_id;
            private CommonUserBean user;
            private CommonGroupBean group;
            private String room_id;
            private String room_type;

           


            private Object content;//CommonMutiCotentBean
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

            public void setUser(CommonUserBean user) {
                this.user = user;
            }

            public CommonGroupBean getGroup() {
                return group;
            }

            public void setGroup(CommonGroupBean group) {
                this.group = group;
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

            public void setContent(CommonMutiCotentBean content) {
                this.content = content;
            }

            public String getMsg_type() {
                return msg_type;
            }

            public void setMsg_type(String msg_type) {
                this.msg_type = msg_type;
            }


        }
    }
}
