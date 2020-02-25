package com.xinyu.newdiggtest.bean;

public class HomeMsgCountBean {

    private XhintBean xhint;
    private TodoMsgBean todo;
    private GroupBeanHome group;
    private String checkcount;//目标数量
    private OpBean op;

    public XhintBean getXhint() {
        return xhint;
    }

    public void setXhint(XhintBean xhint) {
        this.xhint = xhint;
    }

    public TodoMsgBean getTodo() {
        return todo;
    }

    public void setTodo(TodoMsgBean todo) {
        this.todo = todo;
    }

    public GroupBeanHome getGroup() {
        return group;
    }

    public void setGroup(GroupBeanHome group) {
        this.group = group;
    }

    public String getCheckcount() {
        return checkcount;
    }

    public void setCheckcount(String checkcount) {
        this.checkcount = checkcount;
    }

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public static class XhintBean {


        private String topic;
        private String count = "0";
        private LatestmsgBean latestmsg;
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

        public LatestmsgBean getLatestmsg() {
            return latestmsg;
        }

        public void setLatestmsg(LatestmsgBean latestmsg) {
            this.latestmsg = latestmsg;
        }

        public String getLatesttime() {
            return latesttime;
        }

        public void setLatesttime(String latesttime) {
            this.latesttime = latesttime;
        }

        public static class LatestmsgBean {
            /**
             * user_id : 635
             * user : {"user_id":"635","recharge_vip_date":"20190917","unionid":"oS_yHuJQ-R4zGM1VK5GMc9_PFPts","company_id":"1","city":"虹口区","download_app_flag":"Y","openid":"oIlCz0vhRiEc8EFU61ohOqI2wt_c","sex":"","wechat":"oS_yHuJQ-R4zGM1VK5GMc9_PFPts","mobile":"13818049085","custom_head":"http://121.40.177.191:8880/api2e/upload/183838c3c17eef35b0ba42715c03b43f.jpg","user_company_type":"1","personal_desc":"111222","register_time":"2019-07-26 10:22:08","birth_day":"19901011","head":"http://121.40.177.191:8880/api2e/upload/183838c3c17eef35b0ba42715c03b43f.jpg","province":"上海市","name":"南艳花","nickname":"南艳花","become_vip_date":"20190917","is_online":"true","relation_friend_count":"1","email":"nanyanhua@xinyusoft.com"}
             * group : {"user_id":"479","company_id":"1","city":"长治市","sex":"","mobile":"18816545586","custom_head":"http://121.40.177.191:8880/api2e/upload/5747b2b02d673dc720d18ae2a94f63ce.blob","user_company_type":"1","personal_desc":"123","register_time":"2019-05-27 10:45:37","relation_friended_count":"1","head":"http://121.40.177.191:8880/api2e/upload/5747b2b02d673dc720d18ae2a94f63ce.blob","province":"山西省","nickname":"小陈","name":"小陈","is_online":"true","email":"xxx@xinuyusoft.com"}
             * room_id : 1477
             * room_type : S
             * content : {"thumbnail":"http://testok.xinyusoft.com/ok/upload/thumbnail/0990390cc2e58a365c32bbcf4eab8ee6.jpeg","original":"http://testok.xinyusoft.com/ok/upload/0990390cc2e58a365c32bbcf4eab8ee6.jpeg","type":"1","height":"149","width":"200"}
             * msg_type : 1
             */

            private String user_id;
            private UserBean user;
            private GroupBean group;
            private String room_id;
            private String room_type;
//            private ContentBean content;//ContentBean
            private String msg_type;

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public UserBean getUser() {
                return user;
            }

            public void setUser(UserBean user) {
                this.user = user;
            }

            public GroupBean getGroup() {
                return group;
            }

            public void setGroup(GroupBean group) {
                this.group = group;
            }

            public String getRoom_id() {
                return room_id;
            }

            public void setRoom_id(String room_id) {
                this.room_id = room_id;
            }

            public String getRoom_type() {
                return room_type;
            }

            public void setRoom_type(String room_type) {
                this.room_type = room_type;
            }

//            public ContentBean getContent() {
//                return content;
//            }
//
//            public void setContent(ContentBean content) {
//                this.content = content;
//            }

            public String getMsg_type() {
                return msg_type;
            }

            public void setMsg_type(String msg_type) {
                this.msg_type = msg_type;
            }

            public static class UserBean {
                /**
                 * user_id : 635
                 * recharge_vip_date : 20190917
                 * unionid : oS_yHuJQ-R4zGM1VK5GMc9_PFPts
                 * company_id : 1
                 * city : 虹口区
                 * download_app_flag : Y
                 * openid : oIlCz0vhRiEc8EFU61ohOqI2wt_c
                 * sex :
                 * wechat : oS_yHuJQ-R4zGM1VK5GMc9_PFPts
                 * mobile : 13818049085
                 * custom_head : http://121.40.177.191:8880/api2e/upload/183838c3c17eef35b0ba42715c03b43f.jpg
                 * user_company_type : 1
                 * personal_desc : 111222
                 * register_time : 2019-07-26 10:22:08
                 * birth_day : 19901011
                 * head : http://121.40.177.191:8880/api2e/upload/183838c3c17eef35b0ba42715c03b43f.jpg
                 * province : 上海市
                 * name : 南艳花
                 * nickname : 南艳花
                 * become_vip_date : 20190917
                 * is_online : true
                 * relation_friend_count : 1
                 * email : nanyanhua@xinyusoft.com
                 */

                private String user_id;
                private String recharge_vip_date;
                private String unionid;
                private String company_id;
                private String city;
                private String download_app_flag;
                private String openid;
                private String sex;
                private String wechat;
                private String mobile;
                private String custom_head;
                private String user_company_type;
                private String personal_desc;
                private String register_time;
                private String birth_day;
                private String head;
                private String province;
                private String name;
                private String nickname;
                private String become_vip_date;
                private String is_online;
                private String relation_friend_count;
                private String email;

                public String getUser_id() {
                    return user_id;
                }

                public void setUser_id(String user_id) {
                    this.user_id = user_id;
                }

                public String getRecharge_vip_date() {
                    return recharge_vip_date;
                }

                public void setRecharge_vip_date(String recharge_vip_date) {
                    this.recharge_vip_date = recharge_vip_date;
                }

                public String getUnionid() {
                    return unionid;
                }

                public void setUnionid(String unionid) {
                    this.unionid = unionid;
                }

                public String getCompany_id() {
                    return company_id;
                }

                public void setCompany_id(String company_id) {
                    this.company_id = company_id;
                }

                public String getCity() {
                    return city;
                }

                public void setCity(String city) {
                    this.city = city;
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

                public String getSex() {
                    return sex;
                }

                public void setSex(String sex) {
                    this.sex = sex;
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

                public String getRegister_time() {
                    return register_time;
                }

                public void setRegister_time(String register_time) {
                    this.register_time = register_time;
                }

                public String getBirth_day() {
                    return birth_day;
                }

                public void setBirth_day(String birth_day) {
                    this.birth_day = birth_day;
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

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getNickname() {
                    return nickname;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }

                public String getBecome_vip_date() {
                    return become_vip_date;
                }

                public void setBecome_vip_date(String become_vip_date) {
                    this.become_vip_date = become_vip_date;
                }

                public String getIs_online() {
                    return is_online;
                }

                public void setIs_online(String is_online) {
                    this.is_online = is_online;
                }

                public String getRelation_friend_count() {
                    return relation_friend_count;
                }

                public void setRelation_friend_count(String relation_friend_count) {
                    this.relation_friend_count = relation_friend_count;
                }

                public String getEmail() {
                    return email;
                }

                public void setEmail(String email) {
                    this.email = email;
                }
            }

            public static class GroupBean {
                /**
                 * user_id : 479
                 * company_id : 1
                 * city : 长治市
                 * sex :
                 * mobile : 18816545586
                 * custom_head : http://121.40.177.191:8880/api2e/upload/5747b2b02d673dc720d18ae2a94f63ce.blob
                 * user_company_type : 1
                 * personal_desc : 123
                 * register_time : 2019-05-27 10:45:37
                 * relation_friended_count : 1
                 * head : http://121.40.177.191:8880/api2e/upload/5747b2b02d673dc720d18ae2a94f63ce.blob
                 * province : 山西省
                 * nickname : 小陈
                 * name : 小陈
                 * is_online : true
                 * email : xxx@xinuyusoft.com
                 */

                private String user_id;
                private String company_id;
                private String city;
                private String sex;
                private String mobile;
                private String custom_head;
                private String user_company_type;
                private String personal_desc;
                private String register_time;
                private String relation_friended_count;
                private String head;
                private String province;
                private String nickname;
                private String name;
                private String is_online;
                private String email;

                public String getUser_id() {
                    return user_id;
                }

                public void setUser_id(String user_id) {
                    this.user_id = user_id;
                }

                public String getCompany_id() {
                    return company_id;
                }

                public void setCompany_id(String company_id) {
                    this.company_id = company_id;
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

                public String getMobile() {
                    return mobile;
                }

                public void setMobile(String mobile) {
                    this.mobile = mobile;
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

                public String getRegister_time() {
                    return register_time;
                }

                public void setRegister_time(String register_time) {
                    this.register_time = register_time;
                }

                public String getRelation_friended_count() {
                    return relation_friended_count;
                }

                public void setRelation_friended_count(String relation_friended_count) {
                    this.relation_friended_count = relation_friended_count;
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

                public String getIs_online() {
                    return is_online;
                }

                public void setIs_online(String is_online) {
                    this.is_online = is_online;
                }

                public String getEmail() {
                    return email;
                }

                public void setEmail(String email) {
                    this.email = email;
                }
            }

//            public static class ContentBean {
//
//
//                private String thumbnail;
//                private String original;
//                private String type;
//                private String height;
//                private String width;
//
//                public String getThumbnail() {
//                    return thumbnail;
//                }
//
//                public void setThumbnail(String thumbnail) {
//                    this.thumbnail = thumbnail;
//                }
//
//                public String getOriginal() {
//                    return original;
//                }
//
//                public void setOriginal(String original) {
//                    this.original = original;
//                }
//
//                public String getType() {
//                    return type;
//                }
//
//                public void setType(String type) {
//                    this.type = type;
//                }
//
//                public String getHeight() {
//                    return height;
//                }
//
//                public void setHeight(String height) {
//                    this.height = height;
//                }
//
//                public String getWidth() {
//                    return width;
//                }
//
//                public void setWidth(String width) {
//                    this.width = width;
//                }
//            }
        }
    }

    public static class TodoMsgBean {


        private String f_id;
        private String f_type;
        private String f_type_id;
        private String f_title;
        private String f_owner;
        private String f_create_by;
        private String f_update_by;
        private String f_state;
        private String f_msg;
        private Object f_remark;
        private String f_create_date;
        private String f_update_date;
        private String f_create_date_timestamp;
        private BaseCommonUser userinfo;
        private String todocount = "0";

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

        public String getF_type_id() {
            return f_type_id;
        }

        public void setF_type_id(String f_type_id) {
            this.f_type_id = f_type_id;
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

        public String getF_create_by() {
            return f_create_by;
        }

        public void setF_create_by(String f_create_by) {
            this.f_create_by = f_create_by;
        }

        public String getF_update_by() {
            return f_update_by;
        }

        public void setF_update_by(String f_update_by) {
            this.f_update_by = f_update_by;
        }

        public String getF_state() {
            return f_state;
        }

        public void setF_state(String f_state) {
            this.f_state = f_state;
        }

        public String getF_msg() {
            return f_msg;
        }

        public void setF_msg(String f_msg) {
            this.f_msg = f_msg;
        }

        public Object getF_remark() {
            return f_remark;
        }

        public void setF_remark(String f_remark) {
            this.f_remark = f_remark;
        }

        public String getF_create_date() {
            return f_create_date;
        }

        public void setF_create_date(String f_create_date) {
            this.f_create_date = f_create_date;
        }

        public String getF_update_date() {
            return f_update_date;
        }

        public void setF_update_date(String f_update_date) {
            this.f_update_date = f_update_date;
        }

        public String getF_create_date_timestamp() {
            return f_create_date_timestamp;
        }

        public void setF_create_date_timestamp(String f_create_date_timestamp) {
            this.f_create_date_timestamp = f_create_date_timestamp;
        }

        public BaseCommonUser getUserinfo() {
            return userinfo;
        }

        public void setUserinfo(BaseCommonUser userinfo) {
            this.userinfo = userinfo;
        }

        public String getTodocount() {
            return todocount;
        }

        public void setTodocount(String todocount) {
            this.todocount = todocount;
        }


    }

    public static class GroupBeanHome {

        private String id;
        private String companyid;
        private String groupid;
        private String msgtype;
        private String msgcontent;
        private String createduserid;
        private String createdtime;
        private String groupcount = "0";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCompanyid() {
            return companyid;
        }

        public void setCompanyid(String companyid) {
            this.companyid = companyid;
        }

        public String getGroupid() {
            return groupid;
        }

        public void setGroupid(String groupid) {
            this.groupid = groupid;
        }

        public String getMsgtype() {
            return msgtype;
        }

        public void setMsgtype(String msgtype) {
            this.msgtype = msgtype;
        }

        public String getMsgcontent() {
            return msgcontent;
        }

        public void setMsgcontent(String msgcontent) {
            this.msgcontent = msgcontent;
        }

        public String getCreateduserid() {
            return createduserid;
        }

        public void setCreateduserid(String createduserid) {
            this.createduserid = createduserid;
        }

        public String getCreatedtime() {
            return createdtime;
        }

        public void setCreatedtime(String createdtime) {
            this.createdtime = createdtime;
        }

        public String getGroupcount() {
            return groupcount;
        }

        public void setGroupcount(String groupcount) {
            this.groupcount = groupcount;
        }
    }


}
