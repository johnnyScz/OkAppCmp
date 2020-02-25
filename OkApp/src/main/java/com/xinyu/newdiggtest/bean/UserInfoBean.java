package com.xinyu.newdiggtest.bean;

public class UserInfoBean {

    private UserBean user;
    private OpBean op;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public static class UserBean {

        private String user_id;
        private String head;
        private String province = "";
        private String sex;
        private String wechat;
        private String nickname;
        private String is_online;
        private String type;


        private String city;


        private String email;


        private String birth_day;//生日


        private String mobile = "";


        private String subsidiary_expand;//推荐人
        private String become_vip_date = "0";//成为VIP时间
        private String recharge_vip_date;//VIP付费时间
        private String download_app_flag;//app是否已经下载(Y 已下载)


        private String reco_bonus = "";


        public String getSubsidiary_expand() {
            return subsidiary_expand;
        }

        public void setSubsidiary_expand(String subsidiary_expand) {
            this.subsidiary_expand = subsidiary_expand;
        }

        public String getBecome_vip_date() {
            return become_vip_date;
        }

        public void setBecome_vip_date(String become_vip_date) {
            this.become_vip_date = become_vip_date;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getBirth_day() {
            return birth_day;
        }

        public void setBirth_day(String birth_day) {
            this.birth_day = birth_day;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRecharge_vip_date() {
            return recharge_vip_date;
        }

        public void setRecharge_vip_date(String recharge_vip_date) {
            this.recharge_vip_date = recharge_vip_date;
        }

        public String getDownload_app_flag() {
            return download_app_flag;
        }

        public void setDownload_app_flag(String download_app_flag) {
            this.download_app_flag = download_app_flag;
        }

        public String getReco_bonus() {
            return reco_bonus;
        }

        public void setReco_bonus(String reco_bonus) {
            this.reco_bonus = reco_bonus;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }


        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
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

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getIs_online() {
            return is_online;
        }

        public void setIs_online(String is_online) {
            this.is_online = is_online;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }


    }


}
