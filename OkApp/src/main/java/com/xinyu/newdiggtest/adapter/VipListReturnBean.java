package com.xinyu.newdiggtest.adapter;

import com.xinyu.newdiggtest.bean.OpBean;

import java.util.List;

public class VipListReturnBean {

    private OpBean op;
    private DataBean data;
    private List<VipRecomendBean> datalist;

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

    public List<VipRecomendBean> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<VipRecomendBean> datalist) {
        this.datalist = datalist;
    }


    public static class DataBean {

        private String user_id;
        private String recharge_vip_date;

        private String download_app_flag;
        private String sex;
        private String wechat;
        private String type;

        private String register_time;
        private String head;
        private String province;
        private String nickname;

        private String become_vip_date;//成为vip时间
        private String is_online;
        private String second_rec_users = "0";//二级推荐人数
        private String first_rec_users = "0";//一级推荐人数
        private String reco_bonus = "0";//总金额

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getRecharge_vip_date() {
            return recharge_vip_date;
        }

        public void setRecharge_vip_date(String recharge_vip_date) {
            this.recharge_vip_date = recharge_vip_date;
        }

        public String getSecond_rec_users() {
            return second_rec_users;
        }

        public void setSecond_rec_users(String second_rec_users) {
            this.second_rec_users = second_rec_users;
        }


        public String getFirst_rec_users() {
            return first_rec_users;
        }

        public void setFirst_rec_users(String first_rec_users) {
            this.first_rec_users = first_rec_users;
        }

        public String getDownload_app_flag() {
            return download_app_flag;
        }

        public void setDownload_app_flag(String download_app_flag) {
            this.download_app_flag = download_app_flag;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getWechat() {
            return wechat;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getReco_bonus() {
            return reco_bonus;
        }

        public void setReco_bonus(String reco_bonus) {
            this.reco_bonus = reco_bonus;
        }

        public String getRegister_time() {
            return register_time;
        }

        public void setRegister_time(String register_time) {
            this.register_time = register_time;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }


        public String getBecome_vip_date() {
            return become_vip_date;
        }

        public void setBecome_vip_date(String become_vip_date) {
            this.become_vip_date = become_vip_date;
        }

        public String getIs_online() {
            return is_online;
        }

        public void setIs_online(String is_online) {
            this.is_online = is_online;
        }
    }


}
