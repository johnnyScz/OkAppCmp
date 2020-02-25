package com.xshell.xshelllib.ui;

import android.annotation.TargetApi;
import android.app.Activity;


import android.os.Build;
import android.os.Bundle;


import org.apache.cordova.CordovaActivity;


/**
 * 主界面
 *
 * @author zzy
 */
public abstract class XinyuHomeActivity extends CordovaActivity {

    private static final String TAG = "XinyuHomeActivity";

    private Activity xinyuHomeContext;


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        xinyuHomeContext = XinyuHomeActivity.this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        }
    }
}
