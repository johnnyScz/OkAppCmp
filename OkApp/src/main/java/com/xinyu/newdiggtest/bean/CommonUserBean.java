package com.xinyu.newdiggtest.bean;

public class CommonUserBean {

    private String user_id;
    private String head;
    private String custom_head;
    private String province;
    private String sex;


    private String wechat;

    private String mobile;
    private String name;
    private String nickname = "";
    private String is_online;
    private String type;
    private String register_time;

    private String company_id;
    private String city;
    private String id_card;
    private String contract_expiration_date;
    private String birth_day;
    private String household_register;
    private String email;

    private String status;
    private String f_mobile;
    private String f_type;

    public String getStatus() {
        return status;
    }

    public String getF_mobile() {
        return f_mobile;
    }

    public String getF_type() {
        return f_type;
    }


    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getContract_expiration_date() {
        return contract_expiration_date;
    }

    public void setContract_expiration_date(String contract_expiration_date) {
        this.contract_expiration_date = contract_expiration_date;
    }

    public String getBirth_day() {
        return birth_day;
    }

    public void setBirth_day(String birth_day) {
        this.birth_day = birth_day;
    }

    public String getHousehold_register() {
        return household_register;
    }

    public void setHousehold_register(String household_register) {
        this.household_register = household_register;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public CommonUserBean(String head, String nickname) {
        this.head = head;
        this.nickname = nickname;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getRegister_time() {
        return register_time;
    }

    public void setRegister_time(String register_time) {
        this.register_time = register_time;
    }

    public String getCustom_head() {
        return custom_head;
    }

    public void setCustom_head(String custom_head) {
        this.custom_head = custom_head;
    }

}
