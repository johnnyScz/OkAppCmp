package com.xinyu.newdiggtest.bean;

import java.util.List;

public class FeedBackBean {

    private OpBean op;
    private String illustration;
    private List<FeedBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public String getIllustration() {
        return illustration;
    }

    public void setIllustration(String illustration) {
        this.illustration = illustration;
    }

    public List<FeedBean> getData() {
        return data;
    }

    public void setData(List<FeedBean> data) {
        this.data = data;
    }


}
