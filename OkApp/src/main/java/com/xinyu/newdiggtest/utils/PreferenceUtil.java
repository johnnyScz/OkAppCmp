package com.xinyu.newdiggtest.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class PreferenceUtil {
    private static final String PREF_NAME = "ok_pref";
    private static PreferenceUtil instance;
    private final SharedPreferences pref;
    private static final String WXCode = "Wx_Code";

    /**
     * 下载APP保存目录
     */
    private static final String DOWN_APP_DIR = "down_app_dir";


    private static final String Phone = "phone";//绑定的手机号码

    private static final String WX_Token = "Wx_token";

    private static final String App_Fun = "app_fun";//0 个人版 1 企业版

    private static final String CompanyId = "company_id";//选择的公司Id


    private static final String USER_Id = "user_id";

    private static final String USER_3Id = "user_3id";//第三次才拿到真正的id


    private static final String isVip = "isVip";

    private static final String WX_nickName = "nickName";

    private static final String WX_SessionId = "wx_sesionid";

    private static final String headUrl = "head_Url";

    private static final String HasLoadDevToken = "dev_token";

    private static final String App_first_enter = "App_first_enter";//app是否是第一次进入（是否展示引导页）


    /**
     * 文件列表上次更新时间
     */
    private static final String FILE_UPDATE_TIME = "file_update_time";

    private static final String HTML_UPDATE = "HTML_UPDATE";

    private static final String Um_deviceToken = "Umeng_device_token";//友盟注册token


    private static final String HandNote_RoomId = "ssj_roomid";//随手记roodId

    private static final String H5_Lt_Name = "company_name";//h5登录公司名称

    private static final String H5_Lt_Logo = "company_logo";//h5登录公司Logo


    private static final String CompanySaveInfo = "save_info";//接口修改后保存信息(通讯录用到)


    /**
     * app最近更新时间
     */
    private static final String APP_UPDATE_TIME = "app_update_time";


    /**
     * 我的住址
     */
    private static final String Adress = "adress";


    /**
     * 公司类型0 个人 1公司
     */
    private static final String CompanyType = "company_type";


    /**
     * 设置App更新时间（检查文件更新是会提交这个时间）
     *
     * @param time 时间
     */
    public void setAppUpdateTime(String time) {
        Editor appEditor = pref.edit();
        appEditor.putString(APP_UPDATE_TIME, time);
        appEditor.apply();
    }

    /**
     * 获取App更新时间（检查文件时）
     */
    public String getAppUpdateTime() {
        return pref.getString(APP_UPDATE_TIME, "201909201030");
    }


    /**
     * 设置App更新时间（检查文件更新是会提交这个时间）
     *
     * @param adress 时间
     */
    public void setAdress(String adress) {
        Editor appEditor = pref.edit();
        appEditor.putString(Adress, adress);
        appEditor.apply();
    }

    /**
     * 获取App更新时间（检查文件时）
     */
    public String getAdress() {
        return pref.getString(Adress, "上海浦东");
    }


    public static PreferenceUtil getInstance(Context context) {

        if (instance == null) {
            synchronized (PREF_NAME) {
                if (instance == null) {
                    instance = new PreferenceUtil(context);
                }
            }
        }
        return instance;
    }

    private PreferenceUtil(Context context) {

        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }


    /**
     * 获取公司 类型
     */
    public void setCompanyType(String type) {
        Editor appEditor = pref.edit();
        appEditor.putString(CompanyType, type);
        appEditor.apply();
    }

    public String getCompanyType() {
        return pref.getString(CompanyType, "1");
    }


    /**
     * 保存登录时公司列表信息
     */
    public void setCompanyInfo(String info) {
        Editor appEditor = pref.edit();
        appEditor.putString(CompanySaveInfo, info);
        appEditor.apply();
    }

    public String getCompanyInfo() {
        return pref.getString(CompanySaveInfo, "");
    }


    /**
     * 网页是否有更新
     */
    public boolean isHtmlUpdate() {
        return pref.getBoolean(HTML_UPDATE, false);
    }

    public void setHtmlUpdate(boolean isUpdate) {
        Editor appEditor = pref.edit();
        appEditor.putBoolean(HTML_UPDATE, isUpdate);
        appEditor.apply();
    }

    public boolean isAppFirstEnter() {
        return pref.getBoolean(App_first_enter, true);
    }


    public void setAppFirstEnter(boolean enterFirst) {
        Editor appEditor = pref.edit();
        appEditor.putBoolean(App_first_enter, enterFirst);
        appEditor.apply();
    }


    public boolean getIsVip() {
        return pref.getBoolean(isVip, false);
    }

    public void setIsVip(boolean vip) {
        Editor appEditor = pref.edit();
        appEditor.putBoolean(isVip, vip);
        appEditor.apply();
    }


    /**
     * 设置APP下载目录
     *
     * @param dir 目录
     */
    public void setDownAppDir(String dir) {
        Editor appEditor = pref.edit();
        appEditor.putString(DOWN_APP_DIR, dir);
        appEditor.apply();
    }

    /**
     * 获取APP下载目录
     */
    public String getDownAppDir() {
        return pref.getString(DOWN_APP_DIR, "");
    }

    public void setLtName(String name) {
        Editor appEditor = pref.edit();
        appEditor.putString(H5_Lt_Name, name);
        appEditor.apply();
    }


    public String getLtName() {
        return pref.getString(H5_Lt_Name, "");
    }


    public void setLtLogo(String logo) {
        Editor appEditor = pref.edit();
        appEditor.putString(H5_Lt_Logo, logo);
        appEditor.apply();
    }


    public String getLtLogo() {
        return pref.getString(H5_Lt_Logo, "");
    }


    public void setWxCode(String code) {
        Editor appEditor = pref.edit();
        appEditor.putString(WXCode, code);
        appEditor.apply();
    }


    public String getWxCode() {
        return pref.getString(WXCode, "");
    }


    public void setAppFun(String code) {
        Editor appEditor = pref.edit();
        appEditor.putString(App_Fun, code);
        appEditor.apply();
    }


    public String getAppFun() {
        return pref.getString(App_Fun, "-1");
    }


    public void setCompanyId(String compId) {
        Editor appEditor = pref.edit();
        appEditor.putString(CompanyId, compId);
        appEditor.apply();
    }


    public String getCompanyId() {
        return pref.getString(CompanyId, "1");
    }


    public void setHandNoteRoomId(String roomId) {
        Editor appEditor = pref.edit();
        appEditor.putString(HandNote_RoomId, roomId);
        appEditor.apply();
    }

    public String getHandNote_RoomId() {
        return pref.getString(HandNote_RoomId, "");
    }


    public void setMobilePhone(String phone) {
        Editor appEditor = pref.edit();
        appEditor.putString(Phone, phone);
        appEditor.apply();
    }

    public String getPhone() {
        return pref.getString(Phone, "");
    }


    public void setLoadDevToken(boolean load) {
        Editor appEditor = pref.edit();
        appEditor.putBoolean(HasLoadDevToken, load);
        appEditor.apply();
    }

    public boolean getLoadDevToken() {
        return pref.getBoolean(HasLoadDevToken, false);
    }


    public void setUserId(String userId) {
        Editor appEditor = pref.edit();
        appEditor.putString(USER_Id, userId);
        appEditor.apply();
    }

    public String getUserId() {
        return pref.getString(USER_Id, "");
    }


//    public void setUser3Id(String userId) {
//        Editor appEditor = pref.edit();
//        appEditor.putString(USER_3Id, userId);
//        appEditor.apply();
//    }

    /**
     * 获取最终的userid
     *
     * @return
     */
//    public String getUser3Id() {
//        return pref.getString(USER_3Id, "");
//    }
    public void setWxSession(String session) {
        Editor appEditor = pref.edit();
        appEditor.putString(WX_SessionId, session);
        appEditor.apply();
    }

    public String getSessonId() {
        return pref.getString(WX_SessionId, "");
    }

    /**
     * 设置文件更新时间（检查文件更新是会提交这个时间）
     *
     * @param time 时间
     */
    public void setFileUpdateTime(String time) {
        Editor appEditor = pref.edit();
        appEditor.putString(FILE_UPDATE_TIME, time);
        appEditor.apply();
    }

    /**
     * 获取文件更新时间（检查文件时）
     */
    public String getFileUpdateTime() {
        return pref.getString(FILE_UPDATE_TIME, null);
    }


    public void setUmengToken(String token) {
        Editor appEditor = pref.edit();
        appEditor.putString(Um_deviceToken, token);
        appEditor.apply();
    }

    public String getUmengToken() {
        return pref.getString(Um_deviceToken, "");
    }


    public void setWxToken(String token) {
        Editor appEditor = pref.edit();
        appEditor.putString(WX_Token, token);
        appEditor.apply();
    }

    public String getWxToken() {
        return pref.getString(WX_Token, "");
    }

    public void setNickName(String name) {
        Editor appEditor = pref.edit();
        appEditor.putString(WX_nickName, name);
        appEditor.apply();
    }

    public String getNickName() {
        return pref.getString(WX_nickName, "");
    }


    public void setHeadUrl(String url) {
        Editor appEditor = pref.edit();
        appEditor.putString(headUrl, url);
        appEditor.apply();
    }

    public String getHeadUrl() {
        return pref.getString(headUrl, "");
    }


}
