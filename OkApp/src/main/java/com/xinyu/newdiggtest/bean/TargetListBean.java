package com.xinyu.newdiggtest.bean;

import java.util.List;

public class TargetListBean {

    private OpBean op;
    private List<TargetBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<TargetBean> getData() {
        return data;
    }

    public void setData(List<TargetBean> data) {
        this.data = data;
    }

    public static class OpBean {


        private String code;
        private String info;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }


}
