package com.xinyu.newdiggtest.bean;

public class MsgRedBean {

    private OpBean op;
    private DataId data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public DataId getData() {
        return data;
    }

    public void setData(DataId data) {
        this.data = data;
    }


    public static class DataId {
        /**
         * id : 504
         */

        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
