package com.xinyu.newdiggtest.bean;

import java.io.Serializable;
import java.util.List;

public class OrderReturnBean {

    private OpBean op;
    private List<OrderlistBean> datalist;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<OrderlistBean> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<OrderlistBean> datalist) {
        this.datalist = datalist;
    }


    public static class OrderlistBean implements Serializable {
        private String f_relevant_type;
        private String f_relevant_uuid;
        private String f_pay_type = "0";
        private String f_createtime;
        private String f_payer_user;
        private String f_type;
        private String f_desc;
        private String f_payee_user;
        private String f_createuser;
        private String f_content;
        private String f_user_clientip;
        private String f_forum_state;//平台处理状态（2 待确认 3 确认打款 4打款成功）
        private String f_inorout_type = "0";//1 收入，2支出 3 提现(TODO 支付宝提现是type 3)





        private String f_id;
        private String f_money;
        private String f_order_id;
        private String f_prod_name;
        private String f_pay_order;
        private String f_success_date;
        private String f_pay_state = "0";//0 1 支付失败  2 支付成功

        public String getF_relevant_type() {
            return f_relevant_type;
        }

        public void setF_relevant_type(String f_relevant_type) {
            this.f_relevant_type = f_relevant_type;
        }

        public String getF_relevant_uuid() {
            return f_relevant_uuid;
        }

        public void setF_relevant_uuid(String f_relevant_uuid) {
            this.f_relevant_uuid = f_relevant_uuid;
        }

        public String getF_pay_type() {
            return f_pay_type;
        }

        public void setF_pay_type(String f_pay_type) {
            this.f_pay_type = f_pay_type;
        }

        public String getF_createtime() {
            return f_createtime;
        }

        public void setF_createtime(String f_createtime) {
            this.f_createtime = f_createtime;
        }

        public String getF_payer_user() {
            return f_payer_user;
        }

        public void setF_payer_user(String f_payer_user) {
            this.f_payer_user = f_payer_user;
        }

        public String getF_type() {
            return f_type;
        }

        public void setF_type(String f_type) {
            this.f_type = f_type;
        }

        public String getF_desc() {
            return f_desc;
        }

        public void setF_desc(String f_desc) {
            this.f_desc = f_desc;
        }

        public String getF_payee_user() {
            return f_payee_user;
        }

        public void setF_payee_user(String f_payee_user) {
            this.f_payee_user = f_payee_user;
        }

        public String getF_createuser() {
            return f_createuser;
        }

        public void setF_createuser(String f_createuser) {
            this.f_createuser = f_createuser;
        }

        public String getF_content() {
            return f_content;
        }

        public void setF_content(String f_content) {
            this.f_content = f_content;
        }

        public String getF_user_clientip() {
            return f_user_clientip;
        }

        public void setF_user_clientip(String f_user_clientip) {
            this.f_user_clientip = f_user_clientip;
        }

        public String getF_forum_state() {
            return f_forum_state;
        }

        public void setF_forum_state(String f_forum_state) {
            this.f_forum_state = f_forum_state;
        }

        public String getF_inorout_type() {
            return f_inorout_type;
        }

        public void setF_inorout_type(String f_inorout_type) {
            this.f_inorout_type = f_inorout_type;
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

        public String getF_order_id() {
            return f_order_id;
        }

        public void setF_order_id(String f_order_id) {
            this.f_order_id = f_order_id;
        }

        public String getF_prod_name() {
            return f_prod_name;
        }

        public void setF_prod_name(String f_prod_name) {
            this.f_prod_name = f_prod_name;
        }

        public String getF_pay_order() {
            return f_pay_order;
        }

        public void setF_pay_order(String f_pay_order) {
            this.f_pay_order = f_pay_order;
        }

        public String getF_success_date() {
            return f_success_date;
        }

        public void setF_success_date(String f_success_date) {
            this.f_success_date = f_success_date;
        }

        public String getF_pay_state() {
            return f_pay_state;
        }

        public void setF_pay_state(String f_pay_state) {
            this.f_pay_state = f_pay_state;
        }
    }
}
