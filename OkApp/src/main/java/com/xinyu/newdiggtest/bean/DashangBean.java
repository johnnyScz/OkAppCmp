package com.xinyu.newdiggtest.bean;

public class DashangBean {


    private OpBean op;
    private MoneyBean date;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public MoneyBean getDate() {
        return date;
    }

    public void setDate(MoneyBean date) {
        this.date = date;
    }


    public static class MoneyBean {

        private int money;
        private int user_id;
        private int type;

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
