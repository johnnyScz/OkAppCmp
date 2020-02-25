package com.xinyu.newdiggtest.bean;

public class CardFilterBean {

    private OpBean op;
    private CardBean data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public CardBean getData() {
        return data;
    }

    public void setData(CardBean data) {
        this.data = data;
    }


    /**
     * 名片属性
     */
    public static class CardBean {

        private String f_plugin_id;
        private String f_plugin_type_id;
        private String f_company_id;
        private String f_name="";
        private String f_organization_id;
        private String f_organization_name="";
        private String f_gender;
        private String f_mobile="";
        private String f_email="";
        private String f_addredss="";
        private String f_homepage="";
        private String f_position="";
        private String f_remark;
        private String f_origin_pic_front;
        private String f_origin_pic_back;
        private String f_create_by;
        private String f_create_date;
        private String f_update_by;
        private String f_update_date;
        private String f_del_flag;
        private String f_confirm;
        private String count;
        private String duplicate;

        public String getF_plugin_id() {
            return f_plugin_id;
        }

        public void setF_plugin_id(String f_plugin_id) {
            this.f_plugin_id = f_plugin_id;
        }

        public String getF_plugin_type_id() {
            return f_plugin_type_id;
        }

        public void setF_plugin_type_id(String f_plugin_type_id) {
            this.f_plugin_type_id = f_plugin_type_id;
        }

        public String getF_company_id() {
            return f_company_id;
        }

        public void setF_company_id(String f_company_id) {
            this.f_company_id = f_company_id;
        }

        public String getF_name() {
            return f_name;
        }

        public void setF_name(String f_name) {
            this.f_name = f_name;
        }

        public String getF_organization_id() {
            return f_organization_id;
        }

        public void setF_organization_id(String f_organization_id) {
            this.f_organization_id = f_organization_id;
        }

        public String getF_organization_name() {
            return f_organization_name;
        }

        public void setF_organization_name(String f_organization_name) {
            this.f_organization_name = f_organization_name;
        }

        public String getF_gender() {
            return f_gender;
        }

        public void setF_gender(String f_gender) {
            this.f_gender = f_gender;
        }

        public String getF_mobile() {
            return f_mobile;
        }

        public void setF_mobile(String f_mobile) {
            this.f_mobile = f_mobile;
        }

        public String getF_email() {
            return f_email;
        }

        public void setF_email(String f_email) {
            this.f_email = f_email;
        }

        public String getF_addredss() {
            return f_addredss;
        }

        public void setF_addredss(String f_addredss) {
            this.f_addredss = f_addredss;
        }

        public String getF_homepage() {
            return f_homepage;
        }

        public void setF_homepage(String f_homepage) {
            this.f_homepage = f_homepage;
        }

        public String getF_position() {
            return f_position;
        }

        public void setF_position(String f_position) {
            this.f_position = f_position;
        }

        public String getF_remark() {
            return f_remark;
        }

        public void setF_remark(String f_remark) {
            this.f_remark = f_remark;
        }

        public String getF_origin_pic_front() {
            return f_origin_pic_front;
        }

        public void setF_origin_pic_front(String f_origin_pic_front) {
            this.f_origin_pic_front = f_origin_pic_front;
        }

        public String getF_origin_pic_back() {
            return f_origin_pic_back;
        }

        public void setF_origin_pic_back(String f_origin_pic_back) {
            this.f_origin_pic_back = f_origin_pic_back;
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

        public String getF_confirm() {
            return f_confirm;
        }

        public void setF_confirm(String f_confirm) {
            this.f_confirm = f_confirm;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getDuplicate() {
            return duplicate;
        }

        public void setDuplicate(String duplicate) {
            this.duplicate = duplicate;
        }
    }
}
