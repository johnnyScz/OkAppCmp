package com.xinyu.newdiggtest.bean;


import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.lzy.ninegrid.ImageInfo;

import java.util.List;

public class FollowTestBean implements MultiItemEntity {

    int viewType = 0;

    List<ImageInfo> imgList;

    int askType = 0;//（0 问 1 应答）
    AnswerBean answerBean;//应答3
    AskTopBean askTopBean;//发起
    FollowListBean focusBean;//关注的动态1


    RoomPersonListBean sxBean;//我的私信2
    MsgNewHomeBean msgBean;//我的消息 4


    int reqestDataType = 0;//(1 待办,2  我的私信,3 我关注的,4 我的消息)


    String titleName = "";

    public AnswerBean getAnswerBean() {
        return answerBean;
    }

    public void setAnswerBean(AnswerBean answerBean) {
        this.answerBean = answerBean;
    }

    public AskTopBean getAskTopBean() {
        return askTopBean;
    }

    public void setAskTopBean(AskTopBean askTopBean) {
        this.askTopBean = askTopBean;
    }

    /**
     * 数据接口类型
     *
     * @return
     */
    public int getReqestDataType() {
        return reqestDataType;
    }

    public void setReqestDataType(int reqestDataType) {
        this.reqestDataType = reqestDataType;
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

    public MsgNewHomeBean getMsgBean() {
        return msgBean;
    }

    public void setMsgBean(MsgNewHomeBean msgBean) {
        this.msgBean = msgBean;
    }


    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public int getAskType() {
        return askType;
    }

    public void setAskType(int askType) {
        this.askType = askType;
    }

    public List<ImageInfo> getImgList() {
        return imgList;
    }

    public void setImgList(List<ImageInfo> imgList) {
        this.imgList = imgList;
    }


    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    @Override
    public int getItemType() {
        return viewType;
    }
}