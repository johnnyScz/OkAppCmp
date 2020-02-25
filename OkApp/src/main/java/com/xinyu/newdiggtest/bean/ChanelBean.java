package com.xinyu.newdiggtest.bean;

import java.util.List;

public class ChanelBean {


    private OpBean op;
    private List<ChanelChilcBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<ChanelChilcBean> getData() {
        return data;
    }

    public void setData(List<ChanelChilcBean> data) {
        this.data = data;
    }


    public static class ChanelChilcBean {

        //判断红点：channeid count
        private String channelid;


        private int msgCount = 0;//未读频道消息

        private String companyid;
        private String groupid;
        private String formid;
        private String channelname;
        private String img;

        private String formcontentid = "";//;评论列表用到


        private String description;//上面描述
        private String createdtime;//截取日期
        private String createduserid;//创建者的id
        private String groupname;//群名名字


        private String addtopicpermission;
        private String publicpermission;

        private TodoUserBean create_user_info;


        private String channeltopictodaycount;
        private String channeltopictotalcount;

        private String heat = "0";//热度

        public String getHeat() {
            return heat;
        }

        public void setHeat(String heat) {
            this.heat = heat;
        }

        public String getGroupname() {
            return groupname;
        }

        public void setGroupname(String groupname) {
            this.groupname = groupname;
        }

        public TodoUserBean getCreate_user_info() {
            return create_user_info;
        }

        public String getChannelid() {
            return channelid;
        }

        public void setChannelid(String channelid) {
            this.channelid = channelid;
        }

        public int getMsgCount() {
            return msgCount;
        }

        public void setMsgCount(int msgCount) {
            this.msgCount = msgCount;
        }


        public String getCompanyid() {
            return companyid;
        }

        public void setCompanyid(String companyid) {
            this.companyid = companyid;
        }

        public String getGroupid() {
            return groupid;
        }

        public void setGroupid(String groupid) {
            this.groupid = groupid;
        }

        public String getFormcontentid() {
            return formcontentid;
        }

        public String getFormid() {
            return formid;
        }

        public void setFormid(String formid) {
            this.formid = formid;
        }

        public String getChanneltopictodaycount() {
            return channeltopictodaycount;
        }

        public String getChanneltopictotalcount() {
            return channeltopictotalcount;
        }

        public String getChannelname() {
            return channelname;
        }

        public void setChannelname(String channelname) {
            this.channelname = channelname;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCreatedtime() {
            return createdtime;
        }

        public void setCreatedtime(String createdtime) {
            this.createdtime = createdtime;
        }

        public String getCreateduserid() {
            return createduserid;
        }

        public void setCreateduserid(String createduserid) {
            this.createduserid = createduserid;
        }

        public String getAddtopicpermission() {
            return addtopicpermission;
        }

        public void setAddtopicpermission(String addtopicpermission) {
            this.addtopicpermission = addtopicpermission;
        }

        public String getPublicpermission() {
            return publicpermission;
        }

        public void setPublicpermission(String publicpermission) {
            this.publicpermission = publicpermission;
        }
    }
}
