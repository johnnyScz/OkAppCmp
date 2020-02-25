package com.xinyu.newdiggtest.net.bean;

import com.xinyu.newdiggtest.bean.OpBean;

public class InfoStr {

    private OpBean op;
    private String content;
    private String msg_id;


    private Object data;

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
