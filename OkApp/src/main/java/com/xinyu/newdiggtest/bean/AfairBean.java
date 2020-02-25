package com.xinyu.newdiggtest.bean;

import java.util.List;

public class AfairBean {

    private OpBean op;
    private List<AfairChildBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<AfairChildBean> getData() {
        return data;
    }

    public void setData(List<AfairChildBean> data) {
        this.data = data;
    }


    public static class AfairChildBean {

        private String f_id;
        private String f_company_id;
        private String f_title;
        private String f_desc;
        private String f_chat_id;
        private String f_create_by;
        private String f_create_date;
        private String f_update_by;
        private String f_update_date;
        private String f_del_flag;
        private String f_system;
        private StatisticBean statistic;
        private Object plugins;


        //OKRS
//        private String okrs;

        //CheckBean
        private Object check;

        //  OkrBean
        private Object okr;
        private String f_start_date = "";
        private String check_id;
        private String f_end_date = "";
        private ChatInfoBean chat_info;

        private PushBean group_operation_record;

        private MsgNewBean msg;
        private Object functions;
        private List<GroupInfoBean> group_info;

        public String getF_id() {
            return f_id;
        }

        public void setF_id(String f_id) {
            this.f_id = f_id;
        }

        public MsgNewBean getMsg() {
            return msg;
        }

        public void setMsg(MsgNewBean msg) {
            this.msg = msg;
        }

        public String getF_company_id() {
            return f_company_id;
        }

        public void setF_company_id(String f_company_id) {
            this.f_company_id = f_company_id;
        }

        public String getF_title() {
            return f_title;
        }

        public void setF_title(String f_title) {
            this.f_title = f_title;
        }

        public String getF_desc() {
            return f_desc;
        }

        public void setF_desc(String f_desc) {
            this.f_desc = f_desc;
        }

        public PushBean getGroup_operation_record() {
            return group_operation_record;
        }

        public void setGroup_operation_record(PushBean group_operation_record) {
            this.group_operation_record = group_operation_record;
        }


        public String getF_chat_id() {
            return f_chat_id;
        }

        public void setF_chat_id(String f_chat_id) {
            this.f_chat_id = f_chat_id;
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

        public String getF_system() {
            return f_system;
        }

        public void setF_system(String f_system) {
            this.f_system = f_system;
        }

        public StatisticBean getStatistic() {
            return statistic;
        }

        public void setStatistic(StatisticBean statistic) {
            this.statistic = statistic;
        }

        public Object getPlugins() {
            return plugins;
        }


//        public String getOkrs() {
//            return okrs;
//        }
//
//        public void setOkrs(String okrs) {
//            this.okrs = okrs;
//        }

        public Object getCheck() {
            return check;
        }

        public Object getOkr() {
            return okr;
        }

        public void setOkr(OkrBean okr) {
            this.okr = okr;
        }

        public String getF_start_date() {
            return f_start_date;
        }

        public void setF_start_date(String f_start_date) {
            this.f_start_date = f_start_date;
        }

        public String getCheck_id() {
            return check_id;
        }

        public void setCheck_id(String check_id) {
            this.check_id = check_id;
        }

        public String getF_end_date() {
            return f_end_date;
        }

        public void setF_end_date(String f_end_date) {
            this.f_end_date = f_end_date;
        }

        public ChatInfoBean getChat_info() {
            return chat_info;
        }

        public void setChat_info(ChatInfoBean chat_info) {
            this.chat_info = chat_info;
        }


        public Object getFunctions() {
            return functions;
        }


        public List<GroupInfoBean> getGroup_info() {
            return group_info;
        }

        public void setGroup_info(List<GroupInfoBean> group_info) {
            this.group_info = group_info;
        }

        public static class StatisticBean {
            private String maptotal;
            private String mapdone;
            private String mapcreatetoday;
            private String plugintotal;
            private String plugintoday;
            private String todototal;
            private String tododone;
            private String todotoday;
            private String attachmenttotal;
            private String attachmenttoday;

            public String getMaptotal() {
                return maptotal;
            }

            public void setMaptotal(String maptotal) {
                this.maptotal = maptotal;
            }

            public String getMapdone() {
                return mapdone;
            }

            public void setMapdone(String mapdone) {
                this.mapdone = mapdone;
            }

            public String getMapcreatetoday() {
                return mapcreatetoday;
            }

            public void setMapcreatetoday(String mapcreatetoday) {
                this.mapcreatetoday = mapcreatetoday;
            }

            public String getPlugintotal() {
                return plugintotal;
            }

            public void setPlugintotal(String plugintotal) {
                this.plugintotal = plugintotal;
            }

            public String getPlugintoday() {
                return plugintoday;
            }

            public void setPlugintoday(String plugintoday) {
                this.plugintoday = plugintoday;
            }

            public String getTodototal() {
                return todototal;
            }

            public void setTodototal(String todototal) {
                this.todototal = todototal;
            }

            public String getTododone() {
                return tododone;
            }

            public void setTododone(String tododone) {
                this.tododone = tododone;
            }

            public String getTodotoday() {
                return todotoday;
            }

            public void setTodotoday(String todotoday) {
                this.todotoday = todotoday;
            }

            public String getAttachmenttotal() {
                return attachmenttotal;
            }

            public void setAttachmenttotal(String attachmenttotal) {
                this.attachmenttotal = attachmenttotal;
            }

            public String getAttachmenttoday() {
                return attachmenttoday;
            }

            public void setAttachmenttoday(String attachmenttoday) {
                this.attachmenttoday = attachmenttoday;
            }
        }

        public static class CheckBean {

            private String f_company_id;
            private String f_id;
            private String f_pid;
            private String f_create_by;
            private String f_create_date;
            private String f_update_by;
            private String f_update_date;
            private String f_owner;
            private String f_owner_update_date;
            private String f_title;
            private String f_desc;
            private String f_start_date;
            private String f_end_date;
            private String f_state;
            private String f_state_update_date;
            private String f_finish_date;
            private String f_del_flag;
            private String f_rid;
            private String f_type_id;
            private String f_refer_id;
            private String f_chat_id;
            private String f_properties;
            private String watching;
            private String childcount;
            private String authorized;
            private String f_group_id;
            private String plugincount;


            public String getF_company_id() {
                return f_company_id;
            }

            public void setF_company_id(String f_company_id) {
                this.f_company_id = f_company_id;
            }

            public String getF_id() {
                return f_id;
            }

            public void setF_id(String f_id) {
                this.f_id = f_id;
            }

            public String getF_pid() {
                return f_pid;
            }

            public void setF_pid(String f_pid) {
                this.f_pid = f_pid;
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

            public String getF_owner() {
                return f_owner;
            }

            public void setF_owner(String f_owner) {
                this.f_owner = f_owner;
            }

            public String getF_owner_update_date() {
                return f_owner_update_date;
            }

            public void setF_owner_update_date(String f_owner_update_date) {
                this.f_owner_update_date = f_owner_update_date;
            }

            public String getF_title() {
                return f_title;
            }

            public void setF_title(String f_title) {
                this.f_title = f_title;
            }

            public String getF_desc() {
                return f_desc;
            }

            public void setF_desc(String f_desc) {
                this.f_desc = f_desc;
            }

            public String getF_start_date() {
                return f_start_date;
            }

            public void setF_start_date(String f_start_date) {
                this.f_start_date = f_start_date;
            }

            public String getF_end_date() {
                return f_end_date;
            }

            public void setF_end_date(String f_end_date) {
                this.f_end_date = f_end_date;
            }

            public String getF_state() {
                return f_state;
            }

            public void setF_state(String f_state) {
                this.f_state = f_state;
            }

            public String getF_state_update_date() {
                return f_state_update_date;
            }

            public void setF_state_update_date(String f_state_update_date) {
                this.f_state_update_date = f_state_update_date;
            }

            public String getF_finish_date() {
                return f_finish_date;
            }

            public void setF_finish_date(String f_finish_date) {
                this.f_finish_date = f_finish_date;
            }

            public String getF_del_flag() {
                return f_del_flag;
            }

            public void setF_del_flag(String f_del_flag) {
                this.f_del_flag = f_del_flag;
            }

            public String getF_rid() {
                return f_rid;
            }

            public void setF_rid(String f_rid) {
                this.f_rid = f_rid;
            }

            public String getF_type_id() {
                return f_type_id;
            }

            public void setF_type_id(String f_type_id) {
                this.f_type_id = f_type_id;
            }

            public String getF_refer_id() {
                return f_refer_id;
            }

            public void setF_refer_id(String f_refer_id) {
                this.f_refer_id = f_refer_id;
            }

            public String getF_chat_id() {
                return f_chat_id;
            }

            public void setF_chat_id(String f_chat_id) {
                this.f_chat_id = f_chat_id;
            }

            public String getF_properties() {
                return f_properties;
            }

            public void setF_properties(String f_properties) {
                this.f_properties = f_properties;
            }

            public String getWatching() {
                return watching;
            }

            public void setWatching(String watching) {
                this.watching = watching;
            }

            public String getChildcount() {
                return childcount;
            }

            public void setChildcount(String childcount) {
                this.childcount = childcount;
            }

            public String getAuthorized() {
                return authorized;
            }

            public void setAuthorized(String authorized) {
                this.authorized = authorized;
            }


            public String getF_group_id() {
                return f_group_id;
            }

            public void setF_group_id(String f_group_id) {
                this.f_group_id = f_group_id;
            }

            public String getPlugincount() {
                return plugincount;
            }

            public void setPlugincount(String plugincount) {
                this.plugincount = plugincount;
            }


        }


        public static class ChatInfoBean {

            private String room_id;
            private String room_name;
            private String room_type;
            private String room_property;
            private String room_head;
            private String union_id;
            private String room_member_number;
            private String main_object = "";
            private String create_time;
            private String msg_time;

            public String getRoom_id() {
                return room_id;
            }

            public void setRoom_id(String room_id) {
                this.room_id = room_id;
            }

            public String getRoom_name() {
                return room_name;
            }

            public void setRoom_name(String room_name) {
                this.room_name = room_name;
            }

            public String getRoom_type() {
                return room_type;
            }

            public void setRoom_type(String room_type) {
                this.room_type = room_type;
            }

            public String getRoom_property() {
                return room_property;
            }

            public void setRoom_property(String room_property) {
                this.room_property = room_property;
            }

            public String getRoom_head() {
                return room_head;
            }

            public void setRoom_head(String room_head) {
                this.room_head = room_head;
            }

            public String getUnion_id() {
                return union_id;
            }

            public void setUnion_id(String union_id) {
                this.union_id = union_id;
            }

            public String getRoom_member_number() {
                return room_member_number;
            }

            public void setRoom_member_number(String room_member_number) {
                this.room_member_number = room_member_number;
            }

            public String getMain_object() {
                return main_object;
            }

            public void setMain_object(String main_object) {
                this.main_object = main_object;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getMsg_time() {
                return msg_time;
            }

            public void setMsg_time(String msg_time) {
                this.msg_time = msg_time;
            }
        }


        public static class FunctionsBean {


            private String f_id;
            private String f_group_id;
            private String f_function_id;
            private String f_create_by;
            private String f_create_date;
            private String f_update_by;
            private String f_update_date;
            private String f_del_flag;

            // List<MembersBean>

            private Object members;

            public String getF_id() {
                return f_id;
            }

            public void setF_id(String f_id) {
                this.f_id = f_id;
            }

            public String getF_group_id() {
                return f_group_id;
            }

            public void setF_group_id(String f_group_id) {
                this.f_group_id = f_group_id;
            }

            public String getF_function_id() {
                return f_function_id;
            }

            public void setF_function_id(String f_function_id) {
                this.f_function_id = f_function_id;
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

            public Object getMembers() {
                return members;
            }


        }


    }
}
