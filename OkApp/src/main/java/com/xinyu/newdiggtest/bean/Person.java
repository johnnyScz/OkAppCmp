package com.xinyu.newdiggtest.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.xinyu.newdiggtest.adapter.FenZuAdapter;


/**
 * 第二组
 */
public class Person implements MultiItemEntity {
    private String name;
    private String imageUrl;
    private String userId;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getItemType() {
        return FenZuAdapter.TYPE_PERSON;
    }

}
