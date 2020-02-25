package com.xinyu.newdiggtest.bean;

public class AccountBean {
    private OpBean op;
    private Account data;

    public OpBean getOp() {
        return op;
    }


    public Account getData() {
        return data;
    }

    public void setData(Account data) {
        this.data = data;
    }


    public static class Account {
        private String f_uuid;
        private String f_user_id;
        private String f_balance;
        private String f_phone;

        public String getF_uuid() {
            return f_uuid;
        }

        public void setF_uuid(String f_uuid) {
            this.f_uuid = f_uuid;
        }

        public String getF_user_id() {
            return f_user_id;
        }

        public void setF_user_id(String f_user_id) {
            this.f_user_id = f_user_id;
        }

        public String getF_balance() {
            return f_balance;
        }

        public void setF_balance(String f_balance) {
            this.f_balance = f_balance;
        }

        public String getF_phone() {
            return f_phone;
        }

        public void setF_phone(String f_phone) {
            this.f_phone = f_phone;
        }


    }


}
