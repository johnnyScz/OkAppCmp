package com.xinyu.newdiggtest.bean;

public class LoginBean {


    private LoginbeanBean loginbean;
    private OpBean op;

    public LoginbeanBean getLoginbean() {
        return loginbean;
    }

    public void setLoginbean(LoginbeanBean loginbean) {
        this.loginbean = loginbean;
    }

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public static class LoginbeanBean {

        private String account;
        private String accounttype;
        private String sessionid;
        private String status;
        private UserLgBean user;
        private String userid;
        private String usertype;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getAccounttype() {
            return accounttype;
        }

        public void setAccounttype(String accounttype) {
            this.accounttype = accounttype;
        }

        public String getSessionid() {
            return sessionid;
        }

        public void setSessionid(String sessionid) {
            this.sessionid = sessionid;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public UserLgBean getUser() {
            return user;
        }



        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUsertype() {
            return usertype;
        }

        public void setUsertype(String usertype) {
            this.usertype = usertype;
        }


    }


}
