package com.xinyu.newdiggtest.bean;

public class CreatFeedBackBean {


    private OpBean op;
    private FeedbackId data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public FeedbackId getData() {
        return data;
    }

    public void setData(FeedbackId data) {
        this.data = data;
    }


    public static class FeedbackId {

        private int f_feedback_id;

        public int getF_feedback_id() {
            return f_feedback_id;
        }

        public void setF_feedback_id(int f_feedback_id) {
            this.f_feedback_id = f_feedback_id;
        }
    }
}
