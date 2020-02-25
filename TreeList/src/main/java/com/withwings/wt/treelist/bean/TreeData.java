package com.withwings.wt.treelist.bean;

import java.util.List;

/**
 * 嵌套式数据层级列表
 * 创建：WithWings 时间 19/1/2
 * Email:wangtong1175@sina.com
 */
public abstract class TreeData {

    private int mLevel;


    protected boolean hasChild = false;

    public void setTreeLevel(int level) {
        mLevel = level;
    }

    public int getTreeLevel() {
        return mLevel;
    }

    public abstract List<TreeData> getChildren();

    private boolean mTreeOpen;

    public boolean isTreeOpen() {
        return mTreeOpen;
    }

    public void setTreeOpen(boolean treeOpen) {
        mTreeOpen = treeOpen;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }
}
