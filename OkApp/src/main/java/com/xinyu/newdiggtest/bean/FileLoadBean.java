package com.xinyu.newdiggtest.bean;

import java.util.List;

public class FileLoadBean {

    private String errno;
    private List<String> data;

    public String getErrno() {
        return errno;
    }

    public void setErrno(String errno) {
        this.errno = errno;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
