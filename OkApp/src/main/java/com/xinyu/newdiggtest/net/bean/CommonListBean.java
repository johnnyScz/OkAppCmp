package com.xinyu.newdiggtest.net.bean;

import java.util.List;

public class CommonListBean {
    private String f_target_uuid;
    private String f_plan_date;
    private String f_executor;
    private String f_target_end_date;
    private String f_target_start_date;
    private String f_state;
    private String f_content;
    private String f_plan_id;
    private String f_describe;
    private String f_target_name;

    private List<TargetcommentBean> targetcomment;

    public String getF_target_uuid() {
        return f_target_uuid;
    }

    public void setF_target_uuid(String f_target_uuid) {
        this.f_target_uuid = f_target_uuid;
    }

    public String getF_plan_date() {
        return f_plan_date;
    }

    public void setF_plan_date(String f_plan_date) {
        this.f_plan_date = f_plan_date;
    }

    public String getF_executor() {
        return f_executor;
    }

    public void setF_executor(String f_executor) {
        this.f_executor = f_executor;
    }

    public String getF_target_end_date() {
        return f_target_end_date;
    }

    public void setF_target_end_date(String f_target_end_date) {
        this.f_target_end_date = f_target_end_date;
    }

    public String getF_target_start_date() {
        return f_target_start_date;
    }

    public void setF_target_start_date(String f_target_start_date) {
        this.f_target_start_date = f_target_start_date;
    }

    public String getF_state() {
        return f_state;
    }

    public void setF_state(String f_state) {
        this.f_state = f_state;
    }

    public String getF_content() {
        return f_content;
    }

    public void setF_content(String f_content) {
        this.f_content = f_content;
    }

    public String getF_plan_id() {
        return f_plan_id;
    }

    public void setF_plan_id(String f_plan_id) {
        this.f_plan_id = f_plan_id;
    }

    public String getF_describe() {
        return f_describe;
    }

    public void setF_describe(String f_describe) {
        this.f_describe = f_describe;
    }

    public String getF_target_name() {
        return f_target_name;
    }

    public void setF_target_name(String f_target_name) {
        this.f_target_name = f_target_name;
    }



    public List<TargetcommentBean> getTargetcomment() {
        return targetcomment;
    }

    public void setTargetcomment(List<TargetcommentBean> targetcomment) {
        this.targetcomment = targetcomment;
    }

    public static class TargetcommentBean {
        private long f_createtime;
        private int f_id;
        private String f_uuid;
        private String f_plan_id;
        private String f_type;
        private String f_comment_user;
        private String f_nick_name;
        private String f_comment;

        public long getF_createtime() {
            return f_createtime;
        }

        public void setF_createtime(long f_createtime) {
            this.f_createtime = f_createtime;
        }

        public int getF_id() {
            return f_id;
        }

        public void setF_id(int f_id) {
            this.f_id = f_id;
        }

        public String getF_uuid() {
            return f_uuid;
        }

        public void setF_uuid(String f_uuid) {
            this.f_uuid = f_uuid;
        }

        public String getF_plan_id() {
            return f_plan_id;
        }

        public void setF_plan_id(String f_plan_id) {
            this.f_plan_id = f_plan_id;
        }

        public String getF_type() {
            return f_type;
        }

        public void setF_type(String f_type) {
            this.f_type = f_type;
        }

        public String getF_comment_user() {
            return f_comment_user;
        }

        public void setF_comment_user(String f_comment_user) {
            this.f_comment_user = f_comment_user;
        }

        public String getF_nick_name() {
            return f_nick_name;
        }

        public void setF_nick_name(String f_nick_name) {
            this.f_nick_name = f_nick_name;
        }

        public String getF_comment() {
            return f_comment;
        }

        public void setF_comment(String f_comment) {
            this.f_comment = f_comment;
        }
    }
}
