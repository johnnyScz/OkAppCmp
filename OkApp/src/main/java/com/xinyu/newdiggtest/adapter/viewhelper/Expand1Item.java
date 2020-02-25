package com.xinyu.newdiggtest.adapter.viewhelper;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.xinyu.newdiggtest.bean.ChildExpandBean;

import java.util.List;

public class Expand1Item extends AbstractExpandableItem<Expand2Item> implements MultiItemEntity {

    private String name;

    boolean hasChild = false;

    private String f_id;
    private String userId;
    private String headUrl;
    private List<ChildExpandBean> child;

    private String empnum = "0";
    private int requestChildCount = 0;

    public String getEmpnum() {
        return empnum;
    }

    public void setEmpnum(String empnum) {
        this.empnum = empnum;
    }

    public int getRequestChildCount() {
        return requestChildCount;
    }

    public void setRequestChildCount(int requestChildCount) {
        this.requestChildCount = requestChildCount;
    }

    public String getF_id() {
        return f_id;
    }

    public void setF_id(String f_id) {
        this.f_id = f_id;
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

    public List<ChildExpandBean> getChild() {
        return child;
    }

    public void setChild(List<ChildExpandBean> child) {
        this.child = child;
    }


    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }


    public Expand1Item(String name, String uerId, String headUrl) {
        this.userId = uerId;
        this.headUrl = headUrl;
        this.name = name;
    }

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public int getItemType() {
        return QuickExpandableAdapter.TYPE_LEVEL_1;
    }

    public String getTitle() {
        return name;
    }

    public void setTitle(String title) {
        this.name = title;
    }
}
