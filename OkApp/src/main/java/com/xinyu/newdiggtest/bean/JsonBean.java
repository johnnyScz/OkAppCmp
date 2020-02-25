package com.xinyu.newdiggtest.bean;

public class JsonBean {


    private String uid;
    private String sid;
    private String fromName;
    private String id;

    @Override
    public String toString() {
        return "{" +
                "uid='" + uid + '\'' +
                ", sid='" + sid + '\'' +
                ", fromName='" + fromName + '\'' +
                ", id='" + id + '\'' +
                ", owner='" + owner + '\'' +
                ", pluginType='" + pluginType + '\'' +
                '}';
    }

    private String owner;
    private String pluginType;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPluginType() {
        return pluginType;
    }

    public void setPluginType(String pluginType) {
        this.pluginType = pluginType;
    }
}
