package com.xinyu.newdiggtest.bean;

import java.util.List;

public class CommentReturnBean {


    private OpBean op;
    private String total;
    private List<CommentBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<CommentBean> getData() {
        return data;
    }

    public void setData(List<CommentBean> data) {
        this.data = data;
    }


    public static class CommentBean {


        private String f_id;
        private String f_type;
        private String f_object_id;
        private String f_comment = "";
        private String f_create_by;
        private String f_create_date;
        private String f_update_by;
        private String f_update_date;
        private String f_del_flag;
        private String count;


        private CommonUser userinfo;


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

        public String getF_object_id() {
            return f_object_id;
        }

        public void setF_object_id(String f_object_id) {
            this.f_object_id = f_object_id;
        }

        public String getF_comment() {
            return f_comment;
        }

        public void setF_comment(String f_comment) {
            this.f_comment = f_comment;
        }

        public String getF_create_by() {
            return f_create_by;
        }

        public void setF_create_by(String f_create_by) {
            this.f_create_by = f_create_by;
        }

        public String getF_create_date() {
            return f_create_date;
        }

        public void setF_create_date(String f_create_date) {
            this.f_create_date = f_create_date;
        }

        public String getF_update_by() {
            return f_update_by;
        }

        public void setF_update_by(String f_update_by) {
            this.f_update_by = f_update_by;
        }

        public String getF_update_date() {
            return f_update_date;
        }

        public void setF_update_date(String f_update_date) {
            this.f_update_date = f_update_date;
        }

        public String getF_del_flag() {
            return f_del_flag;
        }

        public void setF_del_flag(String f_del_flag) {
            this.f_del_flag = f_del_flag;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public CommonUser getUserinfo() {
            return userinfo;
        }

        public void setUserinfo(CommonUser userinfo) {
            this.userinfo = userinfo;
        }
    }
}
