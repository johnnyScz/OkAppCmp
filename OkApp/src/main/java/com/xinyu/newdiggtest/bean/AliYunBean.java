package com.xinyu.newdiggtest.bean;


import java.util.List;

public class AliYunBean {


    private PluginBean plugin;

    private PugRigester plugin_register;


    private OpBean op;
    private List<YunBean> data;

    public PluginBean getPlugin() {
        return plugin;
    }

    public void setPlugin(PluginBean plugin) {
        this.plugin = plugin;
    }


    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<YunBean> getData() {
        return data;
    }

    public void setData(List<YunBean> data) {
        this.data = data;
    }

    public static class PluginBean {


        private String f_plugin_type_id;
        private String f_group_id;
        private String f_title;
        private String f_company_id;
        private String f_url;
        private String f_logo_url;
        private String f_scope;
        private String f_table_name;
        private String f_is_process;
        private String f_have_process;
        private String check;
        private List<ProperBean> plugin_property;


        public String getF_plugin_type_id() {
            return f_plugin_type_id;
        }

        public void setF_plugin_type_id(String f_plugin_type_id) {
            this.f_plugin_type_id = f_plugin_type_id;
        }

        public String getF_group_id() {
            return f_group_id;
        }

        public void setF_group_id(String f_group_id) {
            this.f_group_id = f_group_id;
        }

        public String getF_title() {
            return f_title;
        }

        public void setF_title(String f_title) {
            this.f_title = f_title;
        }

        public String getF_company_id() {
            return f_company_id;
        }

        public void setF_company_id(String f_company_id) {
            this.f_company_id = f_company_id;
        }

        public String getF_url() {
            return f_url;
        }

        public void setF_url(String f_url) {
            this.f_url = f_url;
        }

        public String getF_logo_url() {
            return f_logo_url;
        }

        public void setF_logo_url(String f_logo_url) {
            this.f_logo_url = f_logo_url;
        }

        public String getF_scope() {
            return f_scope;
        }

        public void setF_scope(String f_scope) {
            this.f_scope = f_scope;
        }

        public String getF_table_name() {
            return f_table_name;
        }

        public void setF_table_name(String f_table_name) {
            this.f_table_name = f_table_name;
        }

        public String getF_is_process() {
            return f_is_process;
        }

        public void setF_is_process(String f_is_process) {
            this.f_is_process = f_is_process;
        }

        public String getF_have_process() {
            return f_have_process;
        }

        public void setF_have_process(String f_have_process) {
            this.f_have_process = f_have_process;
        }

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }

        public List<ProperBean> getPlugin_property() {
            return plugin_property;
        }

        public void setPlugin_property(List<ProperBean> plugin_property) {
            this.plugin_property = plugin_property;
        }


    }

    public PugRigester getPlugin_register() {
        return plugin_register;
    }

    public void setPlugin_register(PugRigester plugin_register) {
        this.plugin_register = plugin_register;
    }

}
