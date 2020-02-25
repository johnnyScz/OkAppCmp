package com.xinyu.newdiggtest.bean;


/**
 * Im 图片上传返回的url
 */
public class ImImgBean {

    private String original;//原始图片
    private String filename;//文件名称

    private String thumbnail;//缩略图

    private String width;//图片宽度
    private String height;//图片高度

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }


}
