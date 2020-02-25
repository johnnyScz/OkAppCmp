package com.xshell.xshelllib.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.WindowManager;

import com.xshell.xshelllib.R;


/**
 * Created by zzy on 2016/4/8.
 * 沉浸式状态栏的工具类
 *
 */
public class FulStatusBarUtil {
    /**
     * 设置沉浸式状态栏
     */
    public static void setcolorfulStatusBar(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setTintColor(activity.getResources().getColor(R.color.colorful_status_bar));
        // SystemBarConfig config = tintManager.getConfig();
        // listViewDrawer.setPadding(0, config.getPixelInsetTop(true), 0,
        // config.getPixelInsetBottom());
    }

    /**
     * 设置沉浸式状态栏
     */
    public static void setcolorfulStatusBar(Activity activity,int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            // setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setTintColor(color);
        tintManager.setTintColor(activity.getResources().getColor(R.color.actionsheet_blue));
        // SystemBarConfig config = tintManager.getConfig();
        // listViewDrawer.setPadding(0, config.getPixelInsetTop(true), 0,
        // config.getPixelInsetBottom());
    }

    /**
     * 设置沉浸式状态栏
     */
    public static void setcolorfulStatusBar(Activity activity,String color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            // setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor(color));
        // SystemBarConfig config = tintManager.getConfig();
        // listViewDrawer.setPadding(0, config.getPixelInsetTop(true), 0,
        // config.getPixelInsetBottom());
    }

}
