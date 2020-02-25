package com.xinyu.newdiggtest.bean;

import java.util.List;

public class FileChildRetBean {


    private OpBean op;
    private List<FileChildBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<FileChildBean> getData() {
        return data;
    }

    public void setData(List<FileChildBean> data) {
        this.data = data;
    }


    public static class FileChildBean {


        private String f_id;
        private String f_title;
        private String f_create_by;
        private String f_create_date;
        private String f_update_by;
        private String f_update_date;
        private String attachments;
        private String f_object_id;
        private String f_count;
        private String relation;
        private String relation_by;
        private String likecount;
        private String commentcount;
        private String likes;
        private String like;

        private CommonUserBean f_create_by_info;

        public CommonUserBean getF_create_by_info() {
            return f_create_by_info;
        }

        public String getF_id() {
            return f_id;
        }

        public void setF_id(String f_id) {
            this.f_id = f_id;
        }

        public String getF_title() {
            return f_title;
        }

        public void setF_title(String f_title) {
            this.f_title = f_title;
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

        public String getAttachments() {
            return attachments;
        }

        public void setAttachments(String attachments) {
            this.attachments = attachments;
        }

        public String getF_object_id() {
            return f_object_id;
        }

        public void setF_object_id(String f_object_id) {
            this.f_object_id = f_object_id;
        }

        public String getF_count() {
            return f_count;
        }

        public void setF_count(String f_count) {
            this.f_count = f_count;
        }

        public String getRelation() {
            return relation;
        }

        public void setRelation(String relation) {
            this.relation = relation;
        }

        public String getRelation_by() {
            return relation_by;
        }

        public void setRelation_by(String relation_by) {
            this.relation_by = relation_by;
        }

        public String getLikecount() {
            return likecount;
        }

        public void setLikecount(String likecount) {
            this.likecount = likecount;
        }

        public String getCommentcount() {
            return commentcount;
        }

        public void setCommentcount(String commentcount) {
            this.commentcount = commentcount;
        }

        public String getLikes() {
            return likes;
        }

        public void setLikes(String likes) {
            this.likes = likes;
        }

        public String getLike() {
            return like;
        }

        public void setLike(String like) {
            this.like = like;
        }


    }
}
