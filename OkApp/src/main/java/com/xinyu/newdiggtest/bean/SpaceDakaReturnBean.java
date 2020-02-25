package com.xinyu.newdiggtest.bean;


import java.util.List;

/**
 * 个人空间打卡return bean
 */
public class SpaceDakaReturnBean {


    private OpBean op;
    private DataBean data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }


    public static class DataBean {


        private String head;
        private String user_id;
        private String nick_name;
        private String become_vip_date;


        private List<FollowListBean> targetexecute;

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public List<FollowListBean> getTargetexecute() {
            return targetexecute;
        }

        public String getBecome_vip_date() {
            return become_vip_date;
        }

        public void setBecome_vip_date(String become_vip_date) {
            this.become_vip_date = become_vip_date;
        }


    }
}
