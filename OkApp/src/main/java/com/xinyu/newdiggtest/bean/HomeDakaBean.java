package com.xinyu.newdiggtest.bean;

import java.util.List;

public class HomeDakaBean {


    private OpBean op;
    private List<DaKaItemBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<DaKaItemBean> getData() {
        return data;
    }

    public void setData(List<DaKaItemBean> data) {
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


}
