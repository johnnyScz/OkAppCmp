package com.xinyu.newdiggtest.utils;

import com.xinyu.newdiggtest.bean.AskTopBean;
import com.xinyu.newdiggtest.bean.MemberRetBean;
import com.xinyu.newdiggtest.bean.MonitorChildBean;
import com.xinyu.newdiggtest.bean.MonitorSpanChildBean;
import com.xinyu.newdiggtest.bean.RetListBean;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AppContacts {

    public static String SELECT_Tag = "";
    public static int isAppAlive = 0;//前台活的activity

    public static boolean isBackrond = false;//是否是在后台运行

    public static List<MonitorChildBean> SELECT_Monitor;

    public static List<String> SELECT_WEEKS = new ArrayList<>();

    public static List<MonitorChildBean> MonitorList = new ArrayList<>();

    public static List<MonitorSpanChildBean> MonitorList1 = new ArrayList<>();


    public static AskTopBean EditTODO = null;

    public static int WXTag = 1;//1 登录 2 分享

    public static int BackFromH5 = 0;//通知首页是否更新


    public static List<MemberRetBean.MemberOutBean> SelectData = new ArrayList<>();

    public static List<MemberRetBean.MemberOutBean> intentData = new ArrayList<>();//需要带到下一个页面的已选成员


    public static List<JSONObject> myCompanyList = new ArrayList<>();


    public static RetListBean ToDOInfo = null;


}
