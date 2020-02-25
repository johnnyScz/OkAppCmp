package com.xinyu.newdiggtest.bean;

import java.util.List;

public class MomBean {


    private OpBean op;
    private List<MomChildBean> data;

    private PugRigester plugin_register;

    public PugRigester getPlugin_register() {
        return plugin_register;
    }

    public void setPlugin_register(PugRigester plugin_register) {
        this.plugin_register = plugin_register;
    }

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<MomChildBean> getData() {
        return data;
    }

    public void setData(List<MomChildBean> data) {
        this.data = data;
    }

    public static class MomChildBean {


        private String f_id;
        private String f_field1;
        private String f_field2;
        private String f_field3;
        private String f_field4;
        private String f_field5;
        private String f_field6;
        private String f_cc;
        private String f_create_by;
        private String f_create_time;
        private String f_update_by;
        private String f_update_time;
        private String f_del_flag;
        private String f_company_id;

        public String getF_id() {
            return f_id;
        }

        public void setF_id(String f_id) {
            this.f_id = f_id;
        }

        public String getF_field1() {
            return f_field1;
        }

        public void setF_field1(String f_field1) {
            this.f_field1 = f_field1;
        }

        public String getF_field2() {
            return f_field2;
        }

        public void setF_field2(String f_field2) {
            this.f_field2 = f_field2;
        }

        public String getF_field3() {
            return f_field3;
        }

        public void setF_field3(String f_field3) {
            this.f_field3 = f_field3;
        }

        public String getF_field4() {
            return f_field4;
        }

        public void setF_field4(String f_field4) {
            this.f_field4 = f_field4;
        }

        public String getF_field5() {
            return f_field5;
        }

        public void setF_field5(String f_field5) {
            this.f_field5 = f_field5;
        }

        public String getF_field6() {
            return f_field6;
        }

        public void setF_field6(String f_field6) {
            this.f_field6 = f_field6;
        }

        public String getF_cc() {
            return f_cc;
        }

        public void setF_cc(String f_cc) {
            this.f_cc = f_cc;
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

        public String getF_company_id() {
            return f_company_id;
        }

        public void setF_company_id(String f_company_id) {
            this.f_company_id = f_company_id;
        }
    }
}
