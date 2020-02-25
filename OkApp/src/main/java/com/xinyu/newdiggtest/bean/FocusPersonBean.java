package com.xinyu.newdiggtest.bean;

import java.util.List;

/**
 * 关注的人
 */

public class FocusPersonBean {


    private OpBean op;
    private List<PersonBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<PersonBean> getData() {
        return data;
    }

    public void setData(List<PersonBean> data) {
        this.data = data;
    }

    public static class PersonBean {

        String head;
        String user_name;


        String nick_name;
        String user_id;
        String f_follow_user;


        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getF_follow_user() {
            return f_follow_user;
        }

        public void setF_follow_user(String f_follow_user) {
            this.f_follow_user = f_follow_user;
        }


    }
}
