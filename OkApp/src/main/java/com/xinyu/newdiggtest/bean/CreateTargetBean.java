package com.xinyu.newdiggtest.bean;

public class CreateTargetBean {


    private OpBean op;
    private DataBean data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }


    public static class DataBean {
        private String f_target_id;

        public String getF_target_id() {
            return f_target_id;
        }

        public void setF_target_id(String f_target_id) {
            this.f_target_id = f_target_id;
        }
    }
}
