package com.xinyu.newdiggtest.bean;

import java.util.List;

public class SectionRetBean {

    private OpBean op;
    private List<OrgSection> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<OrgSection> getData() {
        return data;
    }

    public void setData(List<OrgSection> data) {
        this.data = data;
    }


    public static class OrgSection {
        /**
         * f_name : cj
         * f_org_id : 540
         */

        private String f_name;
        private String f_org_id;

        public String getF_name() {
            return f_name;
        }

        public void setF_name(String f_name) {
            this.f_name = f_name;
        }

        public String getF_org_id() {
            return f_org_id;
        }

        public void setF_org_id(String f_org_id) {
            this.f_org_id = f_org_id;
        }
    }
}
