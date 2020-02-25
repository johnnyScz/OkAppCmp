package com.xshell.xshelllib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.xshell.xshelllib.application.AppContext;

public class PreferenceXshellUtil {
    private static final String PREF_NAME = "dhqq_pref";
    private static PreferenceXshellUtil instance;
    private final SharedPreferences pref;


    /**
     * 文件列表上次更新时间
     */
    private static final String FILE_UPDATE_TIME = "file_update_time";
    /**
     * 设备id
     */
    private static final String ANDROID_ID = "android_id";
    /**
     * 下次打开是否应该提示安装
     */
    private static final String APP_THIS_CODE = "app_this_code";

    /**
     * 下次打开是否应该提示安装
     */
    private static final String NEXT_TO_INSTALL_APP = "next_to_install_app";
    /**
     * app列表最近更新时间
     */
    private static final String APP_UPDATE_TIME = "app_update_time";

    /**
     * 用户头像的url
     */
    private static final String USER_LOGO_URL = "USER_LOGO_URL";
    /**
     * 下载APP保存目录
     */
    private static final String DOWN_APP_DIR = "down_app_dir";



    /**
     * 是否有后台检查更新html5
     */
    private static final String BACKGROUND_UPDATE_FILE = "BACKGROUND_UPDATE_FILE";

    private static final String ISFIRSTRUN = "FIRST_RUN";
    private static final String HTML_UPDATE = "HTML_UPDATE";

    private static final String ISSHOWGUIDEPAGE = "ISSHOWGUIDEPAGE";

    private static final String APP_HOMEACTIVITY_PATH = "app_homeactivity_path";


    private static final String RECORDER_SAVETIME = "recorder_savetime";


    public boolean isNeedInstall() {
        return pref.getBoolean(NEXT_TO_INSTALL_APP, false);
    }


    public static PreferenceXshellUtil getInstance() {
        if (instance == null) {
            synchronized (PREF_NAME) {
                if (instance == null) {
                    instance = new PreferenceXshellUtil();
                }
            }
        }
        return instance;
    }

    private PreferenceXshellUtil() {
        pref = AppContext.CONTEXT.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
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

    public void setRecorderSavetime(int day) {
        Editor appEditor = pref.edit();
        appEditor.putInt(RECORDER_SAVETIME, day);
        appEditor.apply();
    }

    /**
     * 获取当前版本的CODE
     */
    public int getRecorderSavetime() {
        return pref.getInt(RECORDER_SAVETIME, 0);
    }


    /**
     * 获取程序是否为首次安装
     */
    public boolean hadFirstRun() {
        return pref.getBoolean(ISFIRSTRUN, true);
    }

    public void setFirstRun(boolean install) {
        Editor appEditor = pref.edit();
        appEditor.putBoolean(ISFIRSTRUN, install);
        appEditor.apply();
    }


    /**
     * 是否需要显示引导页
     */
    public boolean isShowGuidePage() {
        return pref.getBoolean(ISSHOWGUIDEPAGE, true);
    }

    /**
     * 设置是否显示引导页
     *
     * @param show 是否显示
     */
    public void setShowGuidePage(boolean show) {
        Editor appEditor = pref.edit();
        appEditor.putBoolean(ISSHOWGUIDEPAGE, show);
        appEditor.apply();
    }


    /**
     * 获取homeActivity的path
     */
    public String getHomeActivityPath() {
        return pref.getString(APP_HOMEACTIVITY_PATH, null);
    }

    /**
     * 设置homeActivity的path
     */
    public void setHomeActivityPath(String path) {
        Editor appEditor = pref.edit();
        appEditor.putString(APP_HOMEACTIVITY_PATH, path);
        appEditor.apply();
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
        return pref.getString(FILE_UPDATE_TIME, "0");
    }

    public void setAndroidID(String id) {
        Editor appEditor = pref.edit();
        appEditor.putString(ANDROID_ID, id);
        appEditor.apply();
    }


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
        return pref.getString(APP_UPDATE_TIME, null);
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



    /**
     * 设置是否有后台检查更新html5
     *
     * @param down 是否有后台检查更新html5
     */
    public void setBackgroundUpdateFile(boolean down) {
        Editor appEditor = pref.edit();
        appEditor.putBoolean(BACKGROUND_UPDATE_FILE, down);
        appEditor.apply();
    }

    /**
     * 设置当前版本的CODE
     */
    public void setAppThisCode(int code) {
        Editor appEditor = pref.edit();
        appEditor.putInt(APP_THIS_CODE, code);
        appEditor.apply();
    }

    /**
     * 获取当前版本的CODE
     */
    public int getAppThisCode() {
        return pref.getInt(APP_THIS_CODE, 0);
    }


    /**
     * 设置应当升级
     */
    public void setNextToInstall(boolean install) {
        Editor appEditor = pref.edit();
        appEditor.putBoolean(NEXT_TO_INSTALL_APP, install);
        appEditor.apply();
    }


    /**
     * 获取手势密码的url
     */
    public String getUserLogoUrl() {
        return pref.getString(USER_LOGO_URL, null);
    }

    /**
     * 设置手势密码用户头像的url
     *
     * @param url url
     */
    public void setUserLogoUrl(String url) {
        Editor edit = pref.edit();
        edit.putString(USER_LOGO_URL, url);
        edit.apply();
    }

}
