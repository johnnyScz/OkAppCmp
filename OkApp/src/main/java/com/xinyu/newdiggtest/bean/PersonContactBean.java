package com.xinyu.newdiggtest.bean;


import java.util.List;

/**
 * 个人通讯录
 */
public class PersonContactBean {


    private OpBean op;
    private List<FriendBean> friend;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<FriendBean> getFriend() {
        return friend;
    }

    public void setFriend(List<FriendBean> friend) {
        this.friend = friend;
    }


    public static class FriendBean {

        private String user_id;
        private String name;
        private String nickname;
        private String head;
        private String mobile;
        private String email;
        private String status;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
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

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
