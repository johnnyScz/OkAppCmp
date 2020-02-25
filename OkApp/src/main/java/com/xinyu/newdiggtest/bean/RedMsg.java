package com.xinyu.newdiggtest.bean;

import java.util.List;

public class RedMsg {

    private OpBean op;
    private String total;
    private NewestmsgBean newestmsg;
    private List<RedMsgItem> detail;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public NewestmsgBean getNewestmsg() {
        return newestmsg;
    }

    public void setNewestmsg(NewestmsgBean newestmsg) {
        this.newestmsg = newestmsg;
    }

    public List<RedMsgItem> getDetail() {
        return detail;
    }

    public void setDetail(List<RedMsgItem> detail) {
        this.detail = detail;
    }


    public static class NewestmsgBean {

        private String head;
        private String msgcontent = "0";
        private String msgtype;
        private String sender;
        private String subsidiaryuser;
        private String chatroomid;
        private String nickname;
        private String msgid;
        private String msgtime;
        private String msgto;
        private String type;


        private String originalurl;
        private String msgstatus;

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getMsgcontent() {
            return msgcontent;
        }

        public void setMsgcontent(String msgcontent) {
            this.msgcontent = msgcontent;
        }

        public String getMsgtype() {
            return msgtype;
        }

        public void setMsgtype(String msgtype) {
            this.msgtype = msgtype;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getSubsidiaryuser() {
            return subsidiaryuser;
        }

        public void setSubsidiaryuser(String subsidiaryuser) {
            this.subsidiaryuser = subsidiaryuser;
        }

        public String getChatroomid() {
            return chatroomid;
        }

        public void setChatroomid(String chatroomid) {
            this.chatroomid = chatroomid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getOriginalurl() {
            return originalurl;
        }

        public void setOriginalurl(String originalurl) {
            this.originalurl = originalurl;
        }

        public String getMsgid() {
            return msgid;
        }

        public void setMsgid(String msgid) {
            this.msgid = msgid;
        }

        public String getMsgtime() {
            return msgtime;
        }

        public void setMsgtime(String msgtime) {
            this.msgtime = msgtime;
        }

        public String getMsgto() {
            return msgto;
        }

        public void setMsgto(String msgto) {
            this.msgto = msgto;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMsgstatus() {
            return msgstatus;
        }

        public void setMsgstatus(String msgstatus) {
            this.msgstatus = msgstatus;
        }
    }


}
