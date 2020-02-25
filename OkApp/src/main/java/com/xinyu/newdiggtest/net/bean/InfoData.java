package com.xinyu.newdiggtest.net.bean;

import com.xinyu.newdiggtest.bean.OpBean;

public class InfoData {

    private OpBean op;
    private String data;
    private String url;
    private String token;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
