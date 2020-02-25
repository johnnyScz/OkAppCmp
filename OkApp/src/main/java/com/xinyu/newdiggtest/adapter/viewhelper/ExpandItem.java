package com.xinyu.newdiggtest.adapter.viewhelper;


import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.xinyu.newdiggtest.bean.ChildExpandBean;

import java.util.List;

public class ExpandItem extends AbstractExpandableItem<Expand1Item> implements MultiItemEntity {

    private String f_name;


    private String empnum = "0";
    private String f_id;
    private String f_createuser;

    private List<ChildExpandBean> child;


    public ExpandItem(String userId, String name, String fid) {
        this.f_id = fid;
        this.f_createuser = userId;
        this.f_name = name;
    }


    private int requestChildCount = 0;

    public String getEmpnum() {
        return empnum;
    }

    public void setEmpnum(String empnum) {
        this.empnum = empnum;
    }

    public String getF_id() {
        return f_id;
    }

    public void setF_id(String f_id) {
        this.f_id = f_id;
    }

    public String getF_createuser() {
        return f_createuser;
    }

    public void setF_createuser(String f_createuser) {
        this.f_createuser = f_createuser;
    }

    public List<ChildExpandBean> getChild() {
        return child;
    }

    public void setChild(List<ChildExpandBean> child) {
        this.child = child;
    }


    public int getRequestChildCount() {
        return requestChildCount;
    }

    public void setRequestChildCount(int requestChildCount) {
        this.requestChildCount = requestChildCount;
    }


    public ExpandItem(String title) {
        this.f_name = title;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return QuickExpandableAdapter.TYPE_LEVEL_0;
    }

    public String getTitle() {
        return f_name;
    }

    public void setTitle(String title) {
        this.f_name = title;
    }


}
