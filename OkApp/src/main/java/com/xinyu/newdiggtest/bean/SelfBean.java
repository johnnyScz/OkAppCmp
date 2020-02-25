package com.xinyu.newdiggtest.bean;

public class SelfBean {

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

        private String followuser = "0";
        private String followtarget = "0";
        private String money = "0";

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }


        public String getFollowuser() {
            return followuser;
        }

        public void setFollowuser(String followuser) {
            this.followuser = followuser;
        }

        public String getFollowtarget() {
            return followtarget;
        }

        public void setFollowtarget(String followtarget) {
            this.followtarget = followtarget;
        }
    }
}
