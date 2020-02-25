package com.xinyu.newdiggtest.bean;

import java.util.List;

public class DakaSelectBean {

    private OpBean op;
    private List<ShowBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<ShowBean> getData() {
        return data;
    }

    public void setData(List<ShowBean> data) {
        this.data = data;
    }

    public static class OpBean {
        /**
         * code : Y
         * info : 执行成功
         */

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

    public static class ShowBean {
        private String target_name;
        private String target_uuid;


        public String getTarget_name() {
            return target_name;
        }

        public void setTarget_name(String target_name) {
            this.target_name = target_name;
        }

        public String getTarget_uuid() {
            return target_uuid;
        }

        public void setTarget_uuid(String target_uuid) {
            this.target_uuid = target_uuid;
        }


    }
}
