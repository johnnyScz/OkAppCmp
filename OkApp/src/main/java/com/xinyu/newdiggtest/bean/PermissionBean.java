package com.xinyu.newdiggtest.bean;

import java.util.List;

public class PermissionBean {

    private OpBean op;
    private List<FormPremissionBean> form;
    private List<String> plugin;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<FormPremissionBean> getForm() {
        return form;
    }

    public List<String> getPlugin() {
        return plugin;
    }

    public void setPlugin(List<String> plugin) {
        this.plugin = plugin;
    }


    public static class FormPremissionBean {

        private String plugin_type_id;
        private String add_permission;
        private String look_permission;

        public String getPlugin_type_id() {
            return plugin_type_id;
        }

        public void setPlugin_type_id(String plugin_type_id) {
            this.plugin_type_id = plugin_type_id;
        }

        public String getAdd_permission() {
            return add_permission;
        }

        public void setAdd_permission(String add_permission) {
            this.add_permission = add_permission;
        }

        public String getLook_permission() {
            return look_permission;
        }

        public void setLook_permission(String look_permission) {
            this.look_permission = look_permission;
        }
    }
}
