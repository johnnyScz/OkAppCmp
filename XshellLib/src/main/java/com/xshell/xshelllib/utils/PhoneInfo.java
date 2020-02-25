package com.xshell.xshelllib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;

/**
 * 获取设备ID ，AndroidID
 *
 * @author ZhangSong
 */
public class PhoneInfo {

    private Context context;
    private String android_id;
    private static PhoneInfo id;
    private static final String TAG = "AndroidID";

    private PhoneInfo(Context context) {
        android_id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        this.context = context;
    }

    public static PhoneInfo getInstance(Context context) {
        if (id == null) {
            synchronized (TAG) {
                if (id == null) {
                    id = new PhoneInfo(context);
                }
            }
        }
        return id;
    }

    public String getDeviceID() {
        return android_id;
    }


    //获得手机型号
    public String getModel() {
        return android.os.Build.MODEL;

    }

    //获得Android版本
    public String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    public String getPixels() {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels + "*" + dm.heightPixels;
    }

    public int getPixels_w() {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public int getPixels_h() {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取APP当前版本号
     * @return 版本号
     */
    @SuppressWarnings("unused")
    public int getVersionCode() {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            // 当前应用的版本名称
            String versionName = info.versionName;
            // 当前版本的版本号
            int versionCode = info.versionCode;
            // 当前版本的包名
            String packageNames = info.packageName;
            return versionCode;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 获取APP当前版本名称
     *
     * @return 当前版本名称
     */
    @SuppressWarnings("unused")
    public String getVersionName() {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            // 当前应用的版本名称
            String versionName = info.versionName;
            // 当前版本的包名
           // String packageNames = info.packageName;
            // 当前版本的版本号
           // int versionCode = info.versionCode;
            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    /**
     * 获取APP当前版本名称
     *
     * @return 当前版本名称
     */
    public String getPackageName() {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            // 当前应用的版本名称
           // String versionName = info.versionName;
            // 当前版本的包名
           String packageNames = info.packageName;
            // 当前版本的版本号
            // int versionCode = info.versionCode;
            return packageNames;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取APP的名称
     * @return App的名称
     */
    @SuppressWarnings("unused")
    public String getAppName() {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String appName = info.applicationInfo.loadLabel(manager).toString();
            return appName;
        } catch (Exception e) {
            return null;
        }
    }


}
