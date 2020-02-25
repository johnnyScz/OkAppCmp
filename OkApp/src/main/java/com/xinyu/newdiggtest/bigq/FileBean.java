package com.xinyu.newdiggtest.bigq;

import java.io.File;

public class FileBean {

    int type = 0;
    String fname;


    String dex;//后缀

    String fileType = "";
    File mFile;
    String nacketName;//不要后缀名称

    String fileUrl = "";//上传 后的Url

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDex() {
        return dex;
    }

    public void setDex(String dex) {
        this.dex = dex;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public File getPath() {
        return mFile;
    }

    public void setFile(File path) {
        this.mFile = path;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getNacketName() {
        return nacketName;
    }

    public void setNacketName(String nacketName) {
        this.nacketName = nacketName;
    }
}
