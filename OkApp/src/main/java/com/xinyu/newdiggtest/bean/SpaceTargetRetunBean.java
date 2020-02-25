package com.xinyu.newdiggtest.bean;

import java.util.List;

/**
 * 个人空间-目标列表
 */
public class SpaceTargetRetunBean {


    private OpBean op;
    private DataBean data;
    private String become_vip_date = "0";


    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getBecome_vip_date() {
        return become_vip_date;
    }

    public void setBecome_vip_date(String become_vip_date) {
        this.become_vip_date = become_vip_date;
    }

    public static class DataBean {

        private String head;
        private String user_id;
        private String user_name;
        private String nick_name;
        private List<TargetBean> targetplan;

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public List<TargetBean> getTargetplan() {
            return targetplan;
        }

        public void setTargetplan(List<TargetBean> targetplan) {
            this.targetplan = targetplan;
        }


    }
}
