package com.xinyu.newdiggtest.bean;

import java.util.List;

public class PersonChatBean {

    private PageBean page;
    private OpBean op;
    private List<RoomPersonListBean> room_list;

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<RoomPersonListBean> getRoom_list() {
        return room_list;
    }

    public void setRoom_list(List<RoomPersonListBean> room_list) {
        this.room_list = room_list;
    }

    public static class PageBean {
        /**
         * totalcount : 7
         * totalpage : 1
         */

        private String totalcount;
        private String totalpage;

        public String getTotalcount() {
            return totalcount;
        }

        public void setTotalcount(String totalcount) {
            this.totalcount = totalcount;
        }

        public String getTotalpage() {
            return totalpage;
        }

        public void setTotalpage(String totalpage) {
            this.totalpage = totalpage;
        }
    }



}
