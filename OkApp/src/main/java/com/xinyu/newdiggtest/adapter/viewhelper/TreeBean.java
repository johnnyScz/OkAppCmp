package com.xinyu.newdiggtest.adapter.viewhelper;

import com.xinyu.newdiggtest.bean.CommonIdBean;
import com.xinyu.newdiggtest.bean.OpBean;

import java.util.List;

public class TreeBean {


    private OpBean op;
    private List<TreeListBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<TreeListBean> getData() {
        return data;
    }

    public void setData(List<TreeListBean> data) {
        this.data = data;
    }

    public static class TreeListBean {

        private String f_name;
        private String f_id;
        private String rid;
        private String empnum;
        private String f_create_by;
        private List<CommonIdBean> emplist;
        private List<ChildBean> child;


        public String getF_name() {
            return f_name;
        }

        public void setF_name(String f_name) {
            this.f_name = f_name;
        }

        public String getF_id() {
            return f_id;
        }

        public void setF_id(String f_id) {
            this.f_id = f_id;
        }

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        public String getEmpnum() {
            return empnum;
        }

        public void setEmpnum(String empnum) {
            this.empnum = empnum;
        }

        public String getF_create_by() {
            return f_create_by;
        }

        public void setF_create_by(String f_create_by) {
            this.f_create_by = f_create_by;
        }

        public List<CommonIdBean> getEmplist() {
            return emplist;
        }

        public void setEmplist(List<CommonIdBean> emplist) {
            this.emplist = emplist;
        }

        public List<ChildBean> getChild() {
            return child;
        }

        public void setChild(List<ChildBean> child) {
            this.child = child;
        }


        public static class ChildBean {

            private String pid;
            private String f_name;
            private String f_id;
            private String rid;


            private List<CommonIdBean> emplist;
            private List<ChildBean> child;

            private String empnum = "0";


            public String getEmpnum() {
                return empnum;
            }

            public void setEmpnum(String empnum) {
                this.empnum = empnum;
            }

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            public String getF_name() {
                return f_name;
            }

            public void setF_name(String f_name) {
                this.f_name = f_name;
            }

            public String getF_id() {
                return f_id;
            }

            public void setF_id(String f_id) {
                this.f_id = f_id;
            }

            public String getRid() {
                return rid;
            }

            public void setRid(String rid) {
                this.rid = rid;
            }


            public List<CommonIdBean> getEmplist() {
                return emplist;
            }

            public void setEmplist(List<CommonIdBean> emplist) {
                this.emplist = emplist;
            }

            public List<ChildBean> getChild() {
                return child;
            }

            public void setChild(List<ChildBean> child) {
                this.child = child;
            }


        }
    }


    public static class childGroup {

        private String pid;
        private String f_name;
        private String f_id;
        private String rid;
        private List<CommonIdBean> emplist;
        private List<childGroup> child;

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getF_name() {
            return f_name;
        }

        public void setF_name(String f_name) {
            this.f_name = f_name;
        }

        public String getF_id() {
            return f_id;
        }

        public void setF_id(String f_id) {
            this.f_id = f_id;
        }

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        public List<CommonIdBean> getEmplist() {
            return emplist;
        }

        public void setEmplist(List<CommonIdBean> emplist) {
            this.emplist = emplist;
        }

        public List<childGroup> getChild() {
            return child;
        }

        public void setChild(List<childGroup> child) {
            this.child = child;
        }


    }


}
