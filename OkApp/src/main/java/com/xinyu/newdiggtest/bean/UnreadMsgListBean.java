package com.xinyu.newdiggtest.bean;

import java.util.List;

public class UnreadMsgListBean {


    private OpBean op;
    private List<MsgUnreadBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<MsgUnreadBean> getData() {
        return data;
    }

    public void setData(List<MsgUnreadBean> data) {
        this.data = data;
    }

    public static class MsgUnreadBean {

        private String f_id;
        private String f_type;
        private String f_type_id;
        private String f_title;
        private String f_owner = "";
        private String f_create_by;
        private String f_update_by;
        private String f_state;
        private String f_msg;

//        private RemarkBean f_remark;

        private Object f_remark;


        private String f_create_date;
        private String f_update_date;
        private String f_create_date_timestamp;
        private CommonUserBean f_create_by_info;


//               "f_remark": {
//            "f_scope": "other",
//                    "f_owner": "635",
//                    "f_plugin_type_id": "172",
//                    "f_details_url": "https://ok2etest.xinyusoft.com/api2e/bd/login.html#/detail?typeId=172"
//        },


        public String getF_id() {
            return f_id;
        }

        public void setF_id(String f_id) {
            this.f_id = f_id;
        }

        public String getF_type() {
            return f_type;
        }

        public void setF_type(String f_type) {
            this.f_type = f_type;
        }

        public String getF_type_id() {
            return f_type_id;
        }

        public void setF_type_id(String f_type_id) {
            this.f_type_id = f_type_id;
        }

        public String getF_title() {
            return f_title;
        }


        public Object getF_remark() {
            return f_remark;
        }

        public void setF_title(String f_title) {
            this.f_title = f_title;
        }

        public String getF_owner() {
            return f_owner;
        }

        public void setF_owner(String f_owner) {
            this.f_owner = f_owner;
        }

        public String getF_create_by() {
            return f_create_by;
        }

        public void setF_create_by(String f_create_by) {
            this.f_create_by = f_create_by;
        }

        public String getF_update_by() {
            return f_update_by;
        }

        public void setF_update_by(String f_update_by) {
            this.f_update_by = f_update_by;
        }

        public String getF_state() {
            return f_state;
        }

        public void setF_state(String f_state) {
            this.f_state = f_state;
        }

        public String getF_msg() {
            return f_msg;
        }

        public void setF_msg(String f_msg) {
            this.f_msg = f_msg;
        }


        public String getF_create_date() {
            return f_create_date;
        }

        public void setF_create_date(String f_create_date) {
            this.f_create_date = f_create_date;
        }

        public String getF_update_date() {
            return f_update_date;
        }

        public void setF_update_date(String f_update_date) {
            this.f_update_date = f_update_date;
        }

        public String getF_create_date_timestamp() {
            return f_create_date_timestamp;
        }

        public void setF_create_date_timestamp(String f_create_date_timestamp) {
            this.f_create_date_timestamp = f_create_date_timestamp;
        }

        public CommonUserBean getF_create_by_info() {
            return f_create_by_info;
        }

        public void setF_create_by_info(CommonUserBean f_create_by_info) {
            this.f_create_by_info = f_create_by_info;
        }


    }




}
