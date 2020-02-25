package com.xinyu.newdiggtest.bean;


import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.xinyu.newdiggtest.net.bean.CardBean;

import org.json.JSONObject;

public class FocusTodoBean implements MultiItemEntity {


    int ViewType = 0;//0 正常 1公司列表

    private JSONObject cmpBean;//公司列表的数据


    CardBean cardBean;


    private String user_id;
    private String head;
    private String province;
    private String company_id;
    private String city;
    private String sex;
    private String nickname;
    private String name;
    private String wechat;
    private String mobile;
    private String is_online;
    private String register_time;
    private String email;
    private LatestTodoBean latest_todo;
    private String custom_head;
    private String user_company_type;
    private String personal_desc;
    private String relation_friended_count;
    private String unionid;
    private String download_app_flag;
    private String openid;
    private String type;
    private String birth_day;


    private String become_vip_date;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }


    public CardBean getCardBean() {
        return cardBean;
    }

    public void setCardBean(CardBean cardBean) {
        this.cardBean = cardBean;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIs_online() {
        return is_online;
    }

    public void setIs_online(String is_online) {
        this.is_online = is_online;
    }

    public String getRegister_time() {
        return register_time;
    }

    public void setRegister_time(String register_time) {
        this.register_time = register_time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LatestTodoBean getLatest_todo() {
        return latest_todo;
    }

    public void setLatest_todo(LatestTodoBean latest_todo) {
        this.latest_todo = latest_todo;
    }

    public String getCustom_head() {
        return custom_head;
    }

    public void setCustom_head(String custom_head) {
        this.custom_head = custom_head;
    }

    public String getUser_company_type() {
        return user_company_type;
    }

    public void setUser_company_type(String user_company_type) {
        this.user_company_type = user_company_type;
    }

    public String getPersonal_desc() {
        return personal_desc;
    }

    public void setPersonal_desc(String personal_desc) {
        this.personal_desc = personal_desc;
    }

    public String getRelation_friended_count() {
        return relation_friended_count;
    }

    public void setRelation_friended_count(String relation_friended_count) {
        this.relation_friended_count = relation_friended_count;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getDownload_app_flag() {
        return download_app_flag;
    }

    public void setDownload_app_flag(String download_app_flag) {
        this.download_app_flag = download_app_flag;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBirth_day() {
        return birth_day;
    }

    public void setBirth_day(String birth_day) {
        this.birth_day = birth_day;
    }


    public JSONObject getCmpBean() {
        return cmpBean;
    }

    public void setCmpBean(JSONObject cmpBean) {
        this.cmpBean = cmpBean;
    }


    public String getBecome_vip_date() {
        return become_vip_date;
    }

    public void setBecome_vip_date(String become_vip_date) {
        this.become_vip_date = become_vip_date;
    }

    @Override
    public int getItemType() {
        return ViewType;
    }


    public void setItemType(int viewType) {
        ViewType = viewType;
    }

    public static class LatestTodoBean {

        private String f_todo_id;
        private String f_id;
        private String f_state;
        private String f_owner;
        private String f_todo_date;
        private String f_check_date;
        private String f_create_by;
        private String f_create_date;
        private String f_update_by;
        private String f_update_date = "";
        private String f_del_flag;
        private String title = "";
        private String f_todo_time;
        private String f_check_time;
        private String f_latest_msg;


        private String f_desc = "";
        private String createtimestamp;

        private String updatetimestamp;

        public String getUpdatetimestamp() {
            return updatetimestamp;
        }

        public void setUpdatetimestamp(String updatetimestamp) {
            this.updatetimestamp = updatetimestamp;
        }


        public String getF_desc() {
            return f_desc;
        }

        public void setF_desc(String f_desc) {
            this.f_desc = f_desc;
        }

        public String getF_latest_msg() {
            return f_latest_msg;
        }

        public void setF_latest_msg(String f_latest_msg) {
            this.f_latest_msg = f_latest_msg;
        }


        public String getCreatetimestamp() {
            return createtimestamp;
        }


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

        public String getF_state() {
            return f_state;
        }

        public void setF_state(String f_state) {
            this.f_state = f_state;
        }

        public String getF_owner() {
            return f_owner;
        }

        public void setF_owner(String f_owner) {
            this.f_owner = f_owner;
        }

        public String getF_todo_date() {
            return f_todo_date;
        }

        public void setF_todo_date(String f_todo_date) {
            this.f_todo_date = f_todo_date;
        }

        public String getF_check_date() {
            return f_check_date;
        }

        public void setF_check_date(String f_check_date) {
            this.f_check_date = f_check_date;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getF_todo_time() {
            return f_todo_time;
        }

        public void setF_todo_time(String f_todo_time) {
            this.f_todo_time = f_todo_time;
        }

        public String getF_check_time() {
            return f_check_time;
        }

        public void setF_check_time(String f_check_time) {
            this.f_check_time = f_check_time;
        }


    }


}
