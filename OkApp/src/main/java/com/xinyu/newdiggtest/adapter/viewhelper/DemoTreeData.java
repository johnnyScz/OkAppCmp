package com.xinyu.newdiggtest.adapter.viewhelper;

import com.withwings.wt.treelist.bean.TreeData;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 * 创建：WithWings 时间 19/1/2
 * Email:wangtong1175@sina.com
 */
public class DemoTreeData extends TreeData {

    private int level;

    String userId;
    String name;
    String head;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }


    public DemoTreeData(int levl) {
        this.level = levl;
    }

    @Override
    public List<TreeData> getChildren() {
        if (mData != null) {
            List<TreeData> treeData = new ArrayList<>();

            treeData.addAll(mData);
            return treeData;
        } else {
            return null;
        }
    }

    private List<DemoTreeData> mData;

    public void setData(List<DemoTreeData> data) {
        mData = data;
    }
}
