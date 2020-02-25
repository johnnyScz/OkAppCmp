package com.xinyu.newdiggtest.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class DingdingBean implements MultiItemEntity {


    private FollowListBean focusBean;//关注的动态
    RoomPersonListBean sxBean;
    private String from_name;
    private int itemType = 1;//视图type

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getFrom_name() {
        return from_name;
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }

    public FollowListBean getFocusBean() {
        return focusBean;
    }

    public void setFocusBean(FollowListBean focusBean) {
        this.focusBean = focusBean;
    }

    public RoomPersonListBean getSxBean() {
        return sxBean;
    }

    public void setSxBean(RoomPersonListBean sxBean) {
        this.sxBean = sxBean;
    }


    @Override
    public int getItemType() {
        return itemType;
    }
}
