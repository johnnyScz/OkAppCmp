package com.xinyu.newdiggtest.bean;


import java.util.List;

public class FormBean {

    private String f_id;
    private String f_customer_id;
    private String f_start_time;
    private String f_end_time;
    private String f_principal;
    private String f_interest;
    private String f_netting;
    private String f_current_assets;
    private String f_company_id;
    private String f_create_by;
    private String f_create_time;
    private String f_update_by;
    private String f_update_time;
    private String f_del_flag;


    private String f_settlement_month = "";


    private String f_days;
    private String f_cc;
    private String f_request_amount;
    private String f_royalty;
    private CustomermapBean customermap;
    private Object ccmap;

    private List<String> f_request_funds;

    private String f_plugin_id;
    private String f_plugin_type_id;
    private String f_owner;
    private String f_day;
    private String f_type;
    private String f_desc;
    private String f_promotion = "";
    private String f_amount = "";

    public String getF_plugin_id() {
        return f_plugin_id;
    }

    public String getF_plugin_type_id() {
        return f_plugin_type_id;
    }

    public String getF_owner() {
        return f_owner;
    }

    public String getF_day() {
        return f_day;
    }

    public String getF_type() {
        return f_type;
    }

    public String getF_desc() {
        return f_desc;
    }

    public String getF_promotion() {
        return f_promotion;
    }

    public String getF_amount() {
        return f_amount;
    }

    public String getF_settlement_month() {
        return f_settlement_month;
    }

    public void setF_settlement_month(String f_settlement_month) {
        this.f_settlement_month = f_settlement_month;
    }

    public String getF_id() {
        return f_id;
    }

    public void setF_id(String f_id) {
        this.f_id = f_id;
    }

    public String getF_customer_id() {
        return f_customer_id;
    }

    public void setF_customer_id(String f_customer_id) {
        this.f_customer_id = f_customer_id;
    }

    public String getF_start_time() {
        return f_start_time;
    }

    public void setF_start_time(String f_start_time) {
        this.f_start_time = f_start_time;
    }

    public String getF_end_time() {
        return f_end_time;
    }

    public void setF_end_time(String f_end_time) {
        this.f_end_time = f_end_time;
    }

    public String getF_principal() {
        return f_principal;
    }

    public void setF_principal(String f_principal) {
        this.f_principal = f_principal;
    }

    public String getF_interest() {
        return f_interest;
    }

    public void setF_interest(String f_interest) {
        this.f_interest = f_interest;
    }

    public String getF_netting() {
        return f_netting;
    }

    public void setF_netting(String f_netting) {
        this.f_netting = f_netting;
    }

    public String getF_current_assets() {
        return f_current_assets;
    }

    public void setF_current_assets(String f_current_assets) {
        this.f_current_assets = f_current_assets;
    }

    public String getF_company_id() {
        return f_company_id;
    }

    public void setF_company_id(String f_company_id) {
        this.f_company_id = f_company_id;
    }

    public String getF_create_by() {
        return f_create_by;
    }

    public void setF_create_by(String f_create_by) {
        this.f_create_by = f_create_by;
    }

    public String getF_create_time() {
        return f_create_time;
    }

    public void setF_create_time(String f_create_time) {
        this.f_create_time = f_create_time;
    }

    public String getF_update_by() {
        return f_update_by;
    }

    public void setF_update_by(String f_update_by) {
        this.f_update_by = f_update_by;
    }

    public String getF_update_time() {
        return f_update_time;
    }

    public void setF_update_time(String f_update_time) {
        this.f_update_time = f_update_time;
    }

    public String getF_del_flag() {
        return f_del_flag;
    }

    public void setF_del_flag(String f_del_flag) {
        this.f_del_flag = f_del_flag;
    }

    public String getF_days() {
        return f_days;
    }

    public void setF_days(String f_days) {
        this.f_days = f_days;
    }

    public String getF_cc() {
        return f_cc;
    }

    public void setF_cc(String f_cc) {
        this.f_cc = f_cc;
    }

    public String getF_request_amount() {
        return f_request_amount;
    }

    public void setF_request_amount(String f_request_amount) {
        this.f_request_amount = f_request_amount;
    }

    public String getF_royalty() {
        return f_royalty;
    }

    public void setF_royalty(String f_royalty) {
        this.f_royalty = f_royalty;
    }

    public CustomermapBean getCustomermap() {
        return customermap;
    }

    public void setCustomermap(CustomermapBean customermap) {
        this.customermap = customermap;
    }

    public Object getCcmap() {
        return ccmap;
    }

    public void setCcmap(Object ccmap) {
        this.ccmap = ccmap;
    }


    public List<String> getF_request_funds() {
        return f_request_funds;
    }

    public void setF_request_funds(List<String> f_request_funds) {
        this.f_request_funds = f_request_funds;
    }

    public static class CustomermapBean {


        private String f_plugin_id;
        private String f_plugin_type_id;
        private String f_company_id;
        private String f_name;
        private String f_organization_id;
        private String f_organization_name;
        private String f_gender;
        private String f_mobile;
        private String f_email;
        private String f_addredss;
        private String f_homepage;
        private String f_position;
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

    public static class FFileBean {
        /**
         * f_title : QS_0101
         * f_path : http://121.40.177.191:8880/api2e/upload/225b24ce58edba3ddfddca85e98186f4.png
         * f_type : png
         * f_upid : ["917"]
         */

        private String f_title;
        private String f_path;
        private String f_type;
        private List<String> f_upid;

        public String getF_title() {
            return f_title;
        }

        public void setF_title(String f_title) {
            this.f_title = f_title;
        }

        public String getF_path() {
            return f_path;
        }

        public void setF_path(String f_path) {
            this.f_path = f_path;
        }

        public String getF_type() {
            return f_type;
        }

        public void setF_type(String f_type) {
            this.f_type = f_type;
        }

        public List<String> getF_upid() {
            return f_upid;
        }

        public void setF_upid(List<String> f_upid) {
            this.f_upid = f_upid;
        }
    }
}
