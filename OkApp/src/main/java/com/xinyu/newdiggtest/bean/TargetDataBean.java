package com.xinyu.newdiggtest.bean;

import java.util.List;

public class TargetDataBean {


    private TargetplanBean targetplan;
    private List<DakaBottowItem> targetplancomment;
    private List<DakaBottowItem> targetplanlikes;

    public TargetplanBean getTargetplan() {
        return targetplan;
    }

    public void setTargetplan(TargetplanBean targetplan) {
        this.targetplan = targetplan;
    }

    public List<DakaBottowItem> getTargetplancomment() {
        return targetplancomment;
    }

    public void setTargetplancomment(List<DakaBottowItem> targetplancomment) {
        this.targetplancomment = targetplancomment;
    }

    public List<DakaBottowItem> getTargetplanlikes() {
        return targetplanlikes;
    }


    public void setTargetplanlikes(List<DakaBottowItem> targetplanlikes) {
        this.targetplanlikes = targetplanlikes;
    }

}
