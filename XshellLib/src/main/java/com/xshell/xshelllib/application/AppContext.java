package com.xshell.xshelllib.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class AppContext extends Application {
    @SuppressWarnings("unused")
    private static final String TAG = "LCApplication";
    public static Context CONTEXT;
    public static AppContext APPCONTEXT;



    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = this;
        APPCONTEXT = this;

        init();
        Log.e("huanghu", "AppContext初始化几次。。。。。。");
    }




    private void init() {
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }


    // 打印log
    public static void log(String tag, String msg) {
        Log.d(tag, msg);
    }


}
