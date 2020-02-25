package com.xinyu.newdiggtest.bean;


import java.util.List;

/**
 * 钱包打赏明细return bean
 */
public class WalletDashangBean {

    private OpBean op;
    private List<WalletMoneyBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<WalletMoneyBean> getData() {
        return data;
    }

    public void setData(List<WalletMoneyBean> data) {
        this.data = data;
    }


}
