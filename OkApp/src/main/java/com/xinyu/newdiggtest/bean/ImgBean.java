package com.xinyu.newdiggtest.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class ImgBean implements MultiItemEntity {


    public static int Item = 0;
    public static int Item_Add = 1;
    public static int Item_Reduce = 2;

    String userId;
    String headUrl;
    String name;
    int type = 0;//0 item 1 增加 2 删除

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int getItemType() {
        return type;
    }
}
