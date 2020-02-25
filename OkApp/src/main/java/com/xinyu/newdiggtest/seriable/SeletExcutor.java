package com.xinyu.newdiggtest.seriable;

import java.io.Serializable;

public class SeletExcutor implements Serializable {

    private static final long serialVersionUID = -122034431L;

    String userId;
    String name;

    public String getName() {
        return name;
    }

    public SeletExcutor(String name, String userId) {
        this.name = name;
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }




}
