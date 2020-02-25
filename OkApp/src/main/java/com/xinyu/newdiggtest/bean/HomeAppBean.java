package com.xinyu.newdiggtest.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.xinyu.newdiggtest.bigq.FileBean;

import java.util.List;

public class HomeAppBean implements MultiItemEntity {

    private int itemType = 1;//视图type

    String title;


    List<FileBean> documentList;

    List<RetListBean.InvitesBean> invitList;//添加待办

    List<RetListBean.InvitesBean> chognsongList;//添加抄送

    List<RetListBean.InvitesBean> notes;//回复列表


    public List<RetListBean.InvitesBean> getInvitList() {
        return invitList;
    }

    public void setInvitList(List<RetListBean.InvitesBean> invitList) {
        this.invitList = invitList;
    }

    public List<RetListBean.InvitesBean> getChognsongList() {
        return chognsongList;
    }

    public void setChognsongList(List<RetListBean.InvitesBean> chognsongList) {
        this.chognsongList = chognsongList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public List<FileBean> getDocumentList() {
        return documentList;
    }

    public void setDocumentList(List<FileBean> documentBeanList) {
        this.documentList = documentBeanList;
    }

    public List<RetListBean.InvitesBean> getNotes() {
        return notes;
    }

    public void setNotes(List<RetListBean.InvitesBean> notes) {
        this.notes = notes;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {

        return itemType;

    }


}
