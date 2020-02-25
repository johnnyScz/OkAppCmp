package com.xinyu.newdiggtest.net.bean;

import com.xinyu.newdiggtest.bean.OpBean;

public class ShenHeLoginBean {

    private String user_id;
    private String session_id;
    private String type;
    private UserShenHeBean user;
    private OpBean op;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserShenHeBean getUser() {
        return user;
    }

    public void setUser(UserShenHeBean user) {
        this.user = user;
    }

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public static class UserShenHeBean {


        private String user_id;
        private String head;
        private String sex;
        private String nickname;
        private String name;
        private String custom_head;
        private String type;
        private String register_time;
        private String account;

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

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCustom_head() {
            return custom_head;
        }

        public void setCustom_head(String custom_head) {
            this.custom_head = custom_head;
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

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }
    }


}
