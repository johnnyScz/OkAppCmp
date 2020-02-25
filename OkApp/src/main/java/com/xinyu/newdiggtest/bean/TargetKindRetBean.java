package com.xinyu.newdiggtest.bean;

import java.util.List;

public class TargetKindRetBean {


    private OpBean op;
    private KindTargetData data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public KindTargetData getData() {
        return data;
    }

    public void setData(KindTargetData data) {
        this.data = data;
    }


    public static class KindTargetData {
        private List<StandardBean> standard;//标准方式
//        private List<TargetKindItemBean> template;//十月怀胎,育儿，老人

        public List<StandardBean> getStandard() {
            return standard;
        }

        public void setStandard(List<StandardBean> standard) {
            this.standard = standard;
        }

//        public List<TargetKindItemBean> getTemplate() {
//            return template;
//        }
//
//        public void setTemplate(List<TargetKindItemBean> template) {
//            this.template = template;
//        }

        public static class StandardBean {

            private String f_update_date;
            private String f_del_flag;
            private String f_title;
            private String f_update_by;
            private String f_create_date;
            private String f_id;
            private String f_create_by;
            private List<TargetKindItemBean> child;

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

            public String getF_title() {
                return f_title;
            }

            public void setF_title(String f_title) {
                this.f_title = f_title;
            }

            public String getF_update_by() {
                return f_update_by;
            }

            public void setF_update_by(String f_update_by) {
                this.f_update_by = f_update_by;
            }

            public String getF_create_date() {
                return f_create_date;
            }

            public void setF_create_date(String f_create_date) {
                this.f_create_date = f_create_date;
            }

            public String getF_id() {
                return f_id;
            }

            public void setF_id(String f_id) {
                this.f_id = f_id;
            }

            public String getF_create_by() {
                return f_create_by;
            }

            public void setF_create_by(String f_create_by) {
                this.f_create_by = f_create_by;
            }

            public List<TargetKindItemBean> getChild() {
                return child;
            }

            public void setChild(List<TargetKindItemBean> child) {
                this.child = child;
            }


        }


    }
}
