package com.xinyu.newdiggtest.net.bean;

public class IntentBean {

    public String object_name;
    public String start_time;
    public String end_time;
    public String target_id;
    public String ballanceType;//0打卡打赏，立即结算。1表示奖励金/挑战金 等待结算(结算类型)
    public String type;//0 挑战金 1 打赏 2.奖励金
    public String relevant_type;//0 目标1 打卡
    public String inorout_type;//1 收入 2支出
    public String money;//金额
    public String toUser;//给谁付钱

}
