package com.xinyu.newdiggtest.bean;

import java.util.List;

public class CheckFinshReturnBean {

    private OpBean op;
    private String countmoney;
    private List<CheckFinshListBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public String getCountmoney() {
        return countmoney;
    }

    public void setCountmoney(String countmoney) {
        this.countmoney = countmoney;
    }

    public List<CheckFinshListBean> getData() {
        return data;
    }

    public void setData(List<CheckFinshListBean> data) {
        this.data = data;
    }


    public static class CheckFinshListBean {


        private String f_aim_id;
        private String f_create_time;
        private String f_flag;
        private String f_id;
        private String f_money;
        private String f_refund;
        private String f_score;
        private String f_status;
        private String f_type;
        private String f_user_id;
        private String head;
        private String nickname;
        private String username;

        public String getF_aim_id() {
            return f_aim_id;
        }

        public void setF_aim_id(String f_aim_id) {
            this.f_aim_id = f_aim_id;
        }

        public String getF_create_time() {
            return f_create_time;
        }

        public void setF_create_time(String f_create_time) {
            this.f_create_time = f_create_time;
        }

        public String getF_flag() {
            return f_flag;
        }

        public void setF_flag(String f_flag) {
            this.f_flag = f_flag;
        }

        public String getF_id() {
            return f_id;
        }

        public void setF_id(String f_id) {
            this.f_id = f_id;
        }

        public String getF_money() {
            return f_money;
        }

        public void setF_money(String f_money) {
            this.f_money = f_money;
        }

        public String getF_refund() {
            return f_refund;
        }

        public void setF_refund(String f_refund) {
            this.f_refund = f_refund;
        }

        public String getF_score() {
            return f_score;
        }

        public void setF_score(String f_score) {
            this.f_score = f_score;
        }

        public String getF_status() {
            return f_status;
        }

        public void setF_status(String f_status) {
            this.f_status = f_status;
        }

        public String getF_type() {
            return f_type;
        }

        public void setF_type(String f_type) {
            this.f_type = f_type;
        }

        public String getF_user_id() {
            return f_user_id;
        }

        public void setF_user_id(String f_user_id) {
            this.f_user_id = f_user_id;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
