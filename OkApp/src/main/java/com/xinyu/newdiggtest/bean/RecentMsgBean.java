package com.xinyu.newdiggtest.bean;

import java.util.List;

public class RecentMsgBean {

    private OpBean op;
    private String event_no;
    private List<EventflowsBean> eventflows;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public String getEvent_no() {
        return event_no;
    }

    public void setEvent_no(String event_no) {
        this.event_no = event_no;
    }

    public List<EventflowsBean> getEventflows() {
        return eventflows;
    }

    public void setEventflows(List<EventflowsBean> eventflows) {
        this.eventflows = eventflows;
    }


    public static class EventflowsBean {

        private ImItemMsgBean event;
        private String eventno;

        public ImItemMsgBean getEvent() {
            return event;
        }

        public void setEvent(ImItemMsgBean event) {
            this.event = event;
        }

        public String getEventno() {
            return eventno;
        }

        public void setEventno(String eventno) {
            this.eventno = eventno;
        }


    }
}
