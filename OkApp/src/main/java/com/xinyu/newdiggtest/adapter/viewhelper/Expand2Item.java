package com.xinyu.newdiggtest.adapter.viewhelper;

import com.chad.library.adapter.base.entity.MultiItemEntity;


public class Expand2Item implements MultiItemEntity {

    private String title;

    boolean hasChild = false;

    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public Expand2Item(String title) {
        this.title = title;
    }

    @Override
    public int getItemType() {
        return QuickExpandableAdapter.TYPE_LEVEL_2;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}





