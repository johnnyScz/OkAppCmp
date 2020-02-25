package com.xshell.xshelllib.plugin;

import android.app.Activity;


import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONException;


public class CustomSetPlugin extends CordovaPlugin {
    private Activity context;


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        context = cordova.getActivity();
    }

    /**
     * 2.拍照权限 3.读写读写外部存储设备权限
     *
     * @return
     * @throws JSONException
     */

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        String flag = args.getString(0);
        if ("authorizeIsOpen".equals(action)) {

//        } else if ("openaccount".equals(action)) {
//            EventBus.getDefault().post(new XshellEvent(XshellConsts.OPEN_ACCT));
//        } else if ("cchelper".equals(action)) {
//            EventBus.getDefault().post(new XshellEvent(XshellConsts.OPEN_CcHelper));
//        }
        }
        return true;
    }

}
