package com.xinyu.newdiggtest.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class RetListBean implements MultiItemEntity {


//    private MsgTodoBean msg;//viewType 为3(待办消息)


    private List<InvitesBean> invites;
    private List<InvitesBean> ccs;
    private List<DocumentBean> attachment;

    private int processType = 0;//流程待办类型(1. 请假 2.设备3.资金 4推广费 5.资金方月度结算)


    private int VoteType = 1;//1.投票 2.报名(报名投票 相关 )


    private List<InvitesBean> finishes;//待办已经接受的列表

    private List<InvitesBean> notes;//交办回复的列表


    int itemType = 0;


    int isYaoban;//是否是邀办


    private String f_id;
    private String f_rid;


    private String f_pid;

    private String f_type = "";
    private String f_source_id;


    private String f_process_id = "";//流程待办

    private String f_title;

    private String f_assign;
    private String f_cc;

    private String f_owner;
    private String f_create_by;
    private String f_create_date;
    private String f_update_by;
    private String f_update_date;


    private String f_finish_date = "";//


    private String f_state = "0";//0表示未check  1表示check了
    private String f_start_date;


    private String f_end_date = "";


    private String f_todo_date;//编辑或修改后的日期

    private String f_del_flag;

    private String f_url;

    private String f_create_time;

    private TodoUserBean create_name;

    private LeaverBean leaveuser;

    public String getF_owner() {
        return f_owner;
    }


    public String getF_todo_date() {
        return f_todo_date;
    }

    public void setF_todo_date(String f_todo_date) {
        this.f_todo_date = f_todo_date;
    }

    public int getProcessType() {
        return processType;
    }

    public void setProcessType(int processType) {
        this.processType = processType;
    }

    public String getF_assign() {
        return f_assign;
    }

    public void setF_assign(String f_assign) {
        this.f_assign = f_assign;
    }

    public String getF_process_id() {
        return f_process_id;
    }

    public void setF_process_id(String f_process_id) {
        this.f_process_id = f_process_id;
    }

    public LeaverBean getLeaveuser() {
        return leaveuser;
    }

    public String getF_finish_date() {
        return f_finish_date;
    }

    public String getF_cc() {
        return f_cc;
    }

    public void setF_cc(String f_cc) {
        this.f_cc = f_cc;
    }

    public String getF_id() {
        return f_id;
    }

    public void setF_id(String f_id) {
        this.f_id = f_id;
    }

    public String getF_rid() {
        return f_rid;
    }

    public void setF_rid(String f_rid) {
        this.f_rid = f_rid;
    }


//    public MsgTodoBean getMsg() {
//        return msg;
//    }
//
//    public void setMsg(MsgTodoBean msg) {
//        this.msg = msg;
//    }


    public int getVoteType() {
        return VoteType;
    }

    public void setVoteType(int voteType) {
        VoteType = voteType;
    }

    public int getIsYaoban() {
        return isYaoban;
    }

    public void setIsYaoban(int isYaoban) {
        this.isYaoban = isYaoban;
    }

    public String getF_state() {
        return f_state;
    }

    public void setF_state(String f_state) {
        this.f_state = f_state;
    }


    public String getF_pid() {
        return f_pid;
    }

    public void setF_pid(String f_pid) {
        this.f_pid = f_pid;
    }

    public TodoUserBean getCreate_name() {
        return create_name;
    }

    public void setCreate_name(TodoUserBean create_name) {
        this.create_name = create_name;
    }

    public List<InvitesBean> getInvites() {
        return invites;
    }

    public void setInvites(List<InvitesBean> invites) {
        this.invites = invites;
    }

    public List<InvitesBean> getNotes() {
        return notes;
    }

    public void setNotes(List<InvitesBean> notes) {
        this.notes = notes;
    }


    public List<InvitesBean> getCcs() {
        return ccs;
    }

    public void setCcs(List<InvitesBean> ccs) {
        this.ccs = ccs;
    }

    public String getF_end_date() {
        return f_end_date;
    }

    public void setF_end_date(String f_end_date) {
        this.f_end_date = f_end_date;
    }

    public String getF_type() {
        return f_type;
    }

    public void setF_type(String f_type) {
        this.f_type = f_type;
    }


    public String getF_title() {
        return f_title;
    }

    public void setF_title(String f_title) {
        this.f_title = f_title;
    }

    public List<DocumentBean> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<DocumentBean> attachment) {
        this.attachment = attachment;
    }


    public String getF_start_date() {
        return f_start_date;
    }

    public void setF_start_date(String f_start_date) {
        this.f_start_date = f_start_date;
    }

    public String getF_create_by() {
        return f_create_by;
    }

    public void setF_create_by(String f_create_by) {
        this.f_create_by = f_create_by;
    }

    public String getF_create_date() {
        return f_create_date;
    }

    public void setF_create_date(String f_create_date) {
        this.f_create_date = f_create_date;
    }

    public List<InvitesBean> getFinishes() {
        return finishes;
    }

    public void setFinishes(List<InvitesBean> finishes) {
        this.finishes = finishes;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }


    public static class InvitesBean {

        private String f_todo_id;
        private String f_id;
        private String f_owner;
        private String f_state = "0";//0表示未check 1 已check
        private String f_todo_date;


        private String f_title;

        private String f_type;


        private String f_confirm_date;
        private String f_create_by = "";
        private String f_create_date;
        private String f_update_by;
        private String f_update_date;
        private String f_del_flag;
        private TodoUserBean ownermap;

        public String getF_todo_id() {
            return f_todo_id;
        }

        public void setF_todo_id(String f_todo_id) {
            this.f_todo_id = f_todo_id;
        }

        public String getF_id() {
            return f_id;
        }

        public void setF_id(String f_id) {
            this.f_id = f_id;
        }

        public String getF_type() {
            return f_type;
        }

        public void setF_type(String f_type) {
            this.f_type = f_type;
        }

        public String getF_title() {
            return f_title;
        }

        public void setF_title(String f_title) {
            this.f_title = f_title;
        }

        public String getF_owner() {
            return f_owner;
        }

        public void setF_owner(String f_owner) {
            this.f_owner = f_owner;
        }

        public String getF_state() {
            return f_state;
        }

        public void setF_state(String f_state) {
            this.f_state = f_state;
        }

        public String getF_todo_date() {
            return f_todo_date;
        }

        public void setF_todo_date(String f_todo_date) {
            this.f_todo_date = f_todo_date;
        }

        public String getF_confirm_date() {
            return f_confirm_date;
        }

        public void setF_confirm_date(String f_confirm_date) {
            this.f_confirm_date = f_confirm_date;
        }

        public String getF_create_by() {
            return f_create_by;
        }

        public void setF_create_by(String f_create_by) {
            this.f_create_by = f_create_by;
        }

        public String getF_create_date() {
            return f_create_date;
        }

        public void setF_create_date(String f_create_date) {
            this.f_create_date = f_create_date;
        }

        public String getF_update_by() {
            return f_update_by;
        }

        public void setF_update_by(String f_update_by) {
            this.f_update_by = f_update_by;
        }

        public String getF_update_date() {
            return f_update_date;
        }

        public void setF_update_date(String f_update_date) {
            this.f_update_date = f_update_date;
        }

        public String getF_del_flag() {
            return f_del_flag;
        }

        public void setF_del_flag(String f_del_flag) {
            this.f_del_flag = f_del_flag;
        }

        public TodoUserBean getOwnermap() {
            return ownermap;
        }

        public void setOwnermap(TodoUserBean ownermap) {
            this.ownermap = ownermap;
        }


    }


    public static class DocumentBean {

        private String f_todo_id;
        private String f_id;
        private String f_title;
        private String f_path;
        private String f_type;

        private String f_create_by;
        private String f_create_date;
        private String f_update_by;
        private String f_update_date;
        private String f_del_flag;

        public String getF_todo_id() {
            return f_todo_id;
        }

        public void setF_todo_id(String f_todo_id) {
            this.f_todo_id = f_todo_id;
        }

        public String getF_id() {
            return f_id;
        }

        public void setF_id(String f_id) {
            this.f_id = f_id;
        }

        public String getF_title() {
            return f_title;
        }

        public void setF_title(String f_title) {
            this.f_title = f_title;
        }

        public String getF_path() {
            return f_path;
        }

        public void setF_path(String f_path) {
            this.f_path = f_path;
        }

        public String getF_type() {
            return f_type;
        }

        public void setF_type(String f_type) {
            this.f_type = f_type;
        }


        public String getF_create_by() {
            return f_create_by;
        }

        public void setF_create_by(String f_create_by) {
            this.f_create_by = f_create_by;
        }

        public String getF_create_date() {
            return f_create_date;
        }

        public void setF_create_date(String f_create_date) {
            this.f_create_date = f_create_date;
        }

        public String getF_update_by() {
            return f_update_by;
        }

        public void setF_update_by(String f_update_by) {
            this.f_update_by = f_update_by;
        }

        public String getF_update_date() {
            return f_update_date;
        }

        public void setF_update_date(String f_update_date) {
            this.f_update_date = f_update_date;
        }

        public String getF_del_flag() {
            return f_del_flag;
        }

        public void setF_del_flag(String f_del_flag) {
            this.f_del_flag = f_del_flag;
        }
    }


}
