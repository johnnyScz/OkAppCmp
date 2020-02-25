package com.xinyu.newdiggtest.bean;

import java.util.List;

public class ChannelRetunBean {

    private OpBean op;
    private Channel data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public Channel getData() {
        return data;
    }

    public void setData(Channel data) {
        this.data = data;
    }



    public static class Channel {

        private GetchanneldtoBean getchanneldto;
        private List<ChanneltopicBean> getchanneltopicdtolist;

        public GetchanneldtoBean getGetchanneldto() {
            return getchanneldto;
        }

        public void setGetchanneldto(GetchanneldtoBean getchanneldto) {
            this.getchanneldto = getchanneldto;
        }

        public List<ChanneltopicBean> getGetchanneltopicdtolist() {
            return getchanneltopicdtolist;
        }

        public void setGetchanneltopicdtolist(List<ChanneltopicBean> getchanneltopicdtolist) {
            this.getchanneltopicdtolist = getchanneltopicdtolist;
        }

        public static class GetchanneldtoBean {

            private String channelid;
            private String companyid;
            private String groupid;
            private String formid;
            private String channelname;
            private String img;
            private String description;
            private String createdtime;
            private String createduserid;
            private String addtopicpermission;
            private String publicpermission;
            private String channeltopictodaycount;
            private String channeltopictotalcount;
            private String groupname;
            private String heat;

            public String getChannelid() {
                return channelid;
            }

            public void setChannelid(String channelid) {
                this.channelid = channelid;
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

            public String getFormid() {
                return formid;
            }

            public void setFormid(String formid) {
                this.formid = formid;
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

            public String getChanneltopictodaycount() {
                return channeltopictodaycount;
            }

            public void setChanneltopictodaycount(String channeltopictodaycount) {
                this.channeltopictodaycount = channeltopictodaycount;
            }

            public String getChanneltopictotalcount() {
                return channeltopictotalcount;
            }

            public void setChanneltopictotalcount(String channeltopictotalcount) {
                this.channeltopictotalcount = channeltopictotalcount;
            }

            public String getGroupname() {
                return groupname;
            }

            public void setGroupname(String groupname) {
                this.groupname = groupname;
            }

            public String getHeat() {
                return heat;
            }

            public void setHeat(String heat) {
                this.heat = heat;
            }
        }


    }
}
