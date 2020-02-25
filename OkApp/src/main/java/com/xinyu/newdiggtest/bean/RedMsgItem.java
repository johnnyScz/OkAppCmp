package com.xinyu.newdiggtest.bean;

public class RedMsgItem {

    private String topic;
    private String count;
    private LatestMsg latestmsg;
    private String latesttime;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public LatestMsg getLatestmsg() {
        return latestmsg;
    }

    public void setLatestmsg(LatestMsg latestmsg) {
        this.latestmsg = latestmsg;
    }

    public String getLatesttime() {
        return latesttime;
    }

    public void setLatesttime(String latesttime) {
        this.latesttime = latesttime;
    }


}
