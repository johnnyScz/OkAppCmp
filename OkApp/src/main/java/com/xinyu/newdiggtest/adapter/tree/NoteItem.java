package com.xinyu.newdiggtest.adapter.tree;

import com.ahao.basetreeview.model.NodeId;

public class NoteItem implements NodeId {

    private String id;
    private String parentId;
    private String name;


    private String OrgId;
    private String userId;
    private String headUrl;
    boolean hasChild;//是否需要请求子集(是否有子集)
    boolean isEmpty = false;//是否是占位符号元素


    public String getOrgId() {
        return OrgId;
    }

    public void setOrgId(String orgId) {
        OrgId = orgId;
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

    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public NoteItem(String name, String id, String parentId) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getPId() {
        return parentId;
    }


    @Override
    public String toString() {
        return "NoteItem{" +
                "id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
