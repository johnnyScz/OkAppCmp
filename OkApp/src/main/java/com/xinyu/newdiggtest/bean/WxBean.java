package com.xinyu.newdiggtest.bean;

public class WxBean {


    private WxDataBean data;


    private OpBean op;


    public WxDataBean getData() {
        return data;
    }


    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }


    public static class WxDataBean {

        private String userid;
        private String sessionid;

        private CommonUser user;


        public CommonUser getUser() {
            return user;
        }


        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getSessionid() {
            return sessionid;
        }

        public void setSessionid(String sessionid) {
            this.sessionid = sessionid;
        }
    }


}
