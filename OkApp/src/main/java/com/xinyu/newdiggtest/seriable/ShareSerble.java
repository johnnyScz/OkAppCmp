package com.xinyu.newdiggtest.seriable;

import java.io.Serializable;

public class ShareSerble implements Serializable {

    private static final long serialVersionUID = -122034431L;

    private String type = "";
    private String name = "";
    private String state = "";
    private String content = "";
    private String uuid = "";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String string) {
        this.state = string;
    }


}
