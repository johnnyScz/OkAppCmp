package com.xinyu.newdiggtest.bean;

import java.util.List;

public class LeaveInfoBean {


    private String count;
    private OpBean op;

    private List<FormKVBean> form;

    private List<LeaveBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<LeaveBean> getData() {
        return data;
    }

    public void setData(List<LeaveBean> data) {
        this.data = data;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }


    public List<FormKVBean> getForm() {
        return form;
    }

    public void setForm(List<FormKVBean> form) {
        this.form = form;
    }

    public static class LeaveBean {

        private String f_id;
        private String f_procedure_id;
        private String f_executor;
        private String f_comment = "";
        private String f_confirm_date;
        private String f_state = "";
        private String f_create_time;
        private String f_create_by;
        private String f_update_time;
        private String f_update_by;
        private String f_todo_id;
        private String f_form_id;
        private String f_plugin_id;
        private String f_company_id;
        private String f_process_id;
        private String f_del_flag;
        private String f_start_time;
        private String f_end_time;


        private String process_state;

        private String monthFlag = "";

        private String f_settlement_month = "";


        private FFileBean f_file;
        private FormBean f_form;

        int step = 0;


        private CommonUser executorinfo;
        private ProcedureBean procedure;//手续

        public String getF_id() {
            return f_id;
        }

        public void setF_id(String f_id) {
            this.f_id = f_id;
        }

        public String getF_procedure_id() {
            return f_procedure_id;
        }

        public void setF_procedure_id(String f_procedure_id) {
            this.f_procedure_id = f_procedure_id;
        }

        public String getF_settlement_month() {
            return f_settlement_month;
        }

        public void setF_settlement_month(String f_settlement_month) {
            this.f_settlement_month = f_settlement_month;
        }

        public String getF_executor() {
            return f_executor;
        }

        public void setF_executor(String f_executor) {
            this.f_executor = f_executor;
        }


        public String getMonthFlag() {
            return monthFlag;
        }

        public void setMonthFlag(String monthFlag) {
            this.monthFlag = monthFlag;
        }

        public int getStep() {
            return step;
        }

        public void setStep(int step) {
            this.step = step;
        }

        public String getF_comment() {
            return f_comment;
        }

        public void setF_comment(String f_comment) {
            this.f_comment = f_comment;
        }

        public FormBean getF_form() {
            return f_form;
        }

        public void setF_form(FormBean f_form) {
            this.f_form = f_form;
        }


        public String getF_confirm_date() {
            return f_confirm_date;
        }

        public void setF_confirm_date(String f_confirm_date) {
            this.f_confirm_date = f_confirm_date;
        }

        public String getF_state() {
            return f_state;
        }

        public void setF_state(String f_state) {
            this.f_state = f_state;
        }

        public String getF_create_time() {
            return f_create_time;
        }

        public void setF_create_time(String f_create_time) {
            this.f_create_time = f_create_time;
        }

        public String getF_create_by() {
            return f_create_by;
        }

        public void setF_create_by(String f_create_by) {
            this.f_create_by = f_create_by;
        }

        public String getF_update_time() {
            return f_update_time;
        }

        public void setF_update_time(String f_update_time) {
            this.f_update_time = f_update_time;
        }

        public String getF_update_by() {
            return f_update_by;
        }

        public void setF_update_by(String f_update_by) {
            this.f_update_by = f_update_by;
        }

        public String getF_todo_id() {
            return f_todo_id;
        }

        public void setF_todo_id(String f_todo_id) {
            this.f_todo_id = f_todo_id;
        }

        public String getF_form_id() {
            return f_form_id;
        }

        public void setF_form_id(String f_form_id) {
            this.f_form_id = f_form_id;
        }

        public String getF_plugin_id() {
            return f_plugin_id;
        }

        public void setF_plugin_id(String f_plugin_id) {
            this.f_plugin_id = f_plugin_id;
        }

        public String getF_company_id() {
            return f_company_id;
        }

        public void setF_company_id(String f_company_id) {
            this.f_company_id = f_company_id;
        }

        public String getF_process_id() {
            return f_process_id;
        }

        public void setF_process_id(String f_process_id) {
            this.f_process_id = f_process_id;
        }

        public String getF_del_flag() {
            return f_del_flag;
        }

        public void setF_del_flag(String f_del_flag) {
            this.f_del_flag = f_del_flag;
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

        public String getProcess_state() {
            return process_state;
        }

        public void setProcess_state(String process_state) {
            this.process_state = process_state;
        }


        public FFileBean getF_file() {
            return f_file;
        }

        public void setF_file(FFileBean f_file) {
            this.f_file = f_file;
        }

        public CommonUser getExecutorinfo() {
            return executorinfo;
        }

        public void setExecutorinfo(CommonUser executorinfo) {
            this.executorinfo = executorinfo;
        }

        public ProcedureBean getProcedure() {
            return procedure;
        }

        public void setProcedure(ProcedureBean procedure) {
            this.procedure = procedure;
        }


        public static class FFileBean {

            private String f_id;
            private String f_execute_id;
            private String f_company_id;
            private String f_del_flag;
            private String f_create_time;
            private String f_create_by;
            private String f_update_time;
            private String f_update_by;
            private List<FUrlBean> f_url;

            public String getF_id() {
                return f_id;
            }

            public void setF_id(String f_id) {
                this.f_id = f_id;
            }

            public String getF_execute_id() {
                return f_execute_id;
            }

            public void setF_execute_id(String f_execute_id) {
                this.f_execute_id = f_execute_id;
            }

            public String getF_company_id() {
                return f_company_id;
            }

            public void setF_company_id(String f_company_id) {
                this.f_company_id = f_company_id;
            }

            public String getF_del_flag() {
                return f_del_flag;
            }

            public void setF_del_flag(String f_del_flag) {
                this.f_del_flag = f_del_flag;
            }

            public String getF_create_time() {
                return f_create_time;
            }

            public void setF_create_time(String f_create_time) {
                this.f_create_time = f_create_time;
            }

            public String getF_create_by() {
                return f_create_by;
            }

            public void setF_create_by(String f_create_by) {
                this.f_create_by = f_create_by;
            }

            public String getF_update_time() {
                return f_update_time;
            }

            public void setF_update_time(String f_update_time) {
                this.f_update_time = f_update_time;
            }

            public String getF_update_by() {
                return f_update_by;
            }

            public void setF_update_by(String f_update_by) {
                this.f_update_by = f_update_by;
            }

            public List<FUrlBean> getF_url() {
                return f_url;
            }

            public void setF_url(List<FUrlBean> f_url) {
                this.f_url = f_url;
            }


        }


        public static class ProcedureBean {

            private String f_id;
            private String f_company_id;
            private String f_plugin_id;
            private String f_state;
            private String f_desc;
            private String f_duration;
            private String f_duration_type;
            private String f_button_left;
            private String f_button_right;
            private String f_start_time;
            private String f_end_time;
            private String f_upload_file = "";
            private String f_del_flag;

            private String f_create_time;
            private String f_create_by;
            private String f_update_time;
            private String f_update_by;
            private String f_role_id;

            public String getF_id() {
                return f_id;
            }

            public void setF_id(String f_id) {
                this.f_id = f_id;
            }

            public String getF_company_id() {
                return f_company_id;
            }

            public void setF_company_id(String f_company_id) {
                this.f_company_id = f_company_id;
            }

            public String getF_plugin_id() {
                return f_plugin_id;
            }

            public void setF_plugin_id(String f_plugin_id) {
                this.f_plugin_id = f_plugin_id;
            }

            public String getF_state() {
                return f_state;
            }

            public void setF_state(String f_state) {
                this.f_state = f_state;
            }

            public String getF_desc() {
                return f_desc;
            }

            public void setF_desc(String f_desc) {
                this.f_desc = f_desc;
            }

            public String getF_duration() {
                return f_duration;
            }

            public void setF_duration(String f_duration) {
                this.f_duration = f_duration;
            }

            public String getF_duration_type() {
                return f_duration_type;
            }

            public void setF_duration_type(String f_duration_type) {
                this.f_duration_type = f_duration_type;
            }

            public String getF_button_left() {
                return f_button_left;
            }

            public void setF_button_left(String f_button_left) {
                this.f_button_left = f_button_left;
            }

            public String getF_button_right() {
                return f_button_right;
            }

            public void setF_button_right(String f_button_right) {
                this.f_button_right = f_button_right;
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

            public String getF_upload_file() {
                return f_upload_file;
            }

            public void setF_upload_file(String f_upload_file) {
                this.f_upload_file = f_upload_file;
            }

            public String getF_del_flag() {
                return f_del_flag;
            }

            public void setF_del_flag(String f_del_flag) {
                this.f_del_flag = f_del_flag;
            }

            public String getF_create_time() {
                return f_create_time;
            }

            public void setF_create_time(String f_create_time) {
                this.f_create_time = f_create_time;
            }

            public String getF_create_by() {
                return f_create_by;
            }

            public void setF_create_by(String f_create_by) {
                this.f_create_by = f_create_by;
            }

            public String getF_update_time() {
                return f_update_time;
            }

            public void setF_update_time(String f_update_time) {
                this.f_update_time = f_update_time;
            }

            public String getF_update_by() {
                return f_update_by;
            }

            public void setF_update_by(String f_update_by) {
                this.f_update_by = f_update_by;
            }

            public String getF_role_id() {
                return f_role_id;
            }

            public void setF_role_id(String f_role_id) {
                this.f_role_id = f_role_id;
            }
        }
    }


    public static class FormKVBean {

        String name;

        String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


}
