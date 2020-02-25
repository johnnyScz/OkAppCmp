package com.xinyu.newdiggtest.bean;

import java.util.List;

public class UploadUrlBean {

    private int errno;
    private List<ImImgBean> data;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public List<ImImgBean> getData() {
        return data;
    }

    public void setData(List<ImImgBean> data) {
        this.data = data;
    }


}
