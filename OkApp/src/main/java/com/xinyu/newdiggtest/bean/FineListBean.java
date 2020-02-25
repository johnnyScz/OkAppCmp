package com.xinyu.newdiggtest.bean;

public class FineListBean {

    private String f_rate_type;//为 n时表示没有评定，显示unassessed_money（未评定的钱）

    private String unassessed_money;

    private String f_rater_id;
    private int f_score;
    private long f_create_time;
    private float f_sup_money;

    private BaseUser user;
    private float f_refund;
    private String f_sup_id;


    public String getF_rater_id() {
        return f_rater_id;
    }

    public void setF_rater_id(String f_rater_id) {
        this.f_rater_id = f_rater_id;
    }

    public int getF_score() {
        return f_score;
    }

    public void setF_score(int f_score) {
        this.f_score = f_score;
    }

    public long getF_create_time() {
        return f_create_time;
    }

    public void setF_create_time(long f_create_time) {
        this.f_create_time = f_create_time;
    }

    public String getUnassessed_money() {
        return unassessed_money;
    }

    public void setUnassessed_money(String unassessed_money) {
        this.unassessed_money = unassessed_money;
    }


    public float getF_sup_money() {
        return f_sup_money;
    }

    public void setF_sup_money(int f_sup_money) {
        this.f_sup_money = f_sup_money;
    }

    public String getF_rate_type() {
        return f_rate_type;
    }

    public void setF_rate_type(String f_rate_type) {
        this.f_rate_type = f_rate_type;
    }

    public BaseUser getUser() {
        return user;
    }

    public void setUser(BaseUser user) {
        this.user = user;
    }

    public float getF_refund() {
        return f_refund;
    }

    public void setF_refund(int f_refund) {
        this.f_refund = f_refund;
    }

    public String getF_sup_id() {
        return f_sup_id;
    }

    public void setF_sup_id(String f_sup_id) {
        this.f_sup_id = f_sup_id;
    }


}
