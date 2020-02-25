package com.xshell.xshelllib.plugin;

import android.app.Activity;
import android.util.Log;

import com.xshell.xshelllib.utils.XshellEvent;
import com.xshell.xshelllib.utils.XshellConsts;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by DELL on 2017/11/15.
 */

public class AppNativePlugin extends CordovaPlugin {
    Activity ctx;


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        ctx = cordova.getActivity();
    }


    @Override
    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if ("speechStart".equals(action)) {
            return true;
        } else if ("getUserInfo".equals(action)) {

            XshellEvent event = new XshellEvent(XshellConsts.UserInfo);
            event.object = callbackContext;
            EventBus.getDefault().post(event);

            return true;

        } else if ("backNative".equals(action)) {

            XshellEvent event = new XshellEvent(XshellConsts.BackNative);
            EventBus.getDefault().post(event);
            return true;

        } else if ("getPushInfo".equals(action)) {
            XshellEvent event = new XshellEvent(XshellConsts.PUSH_INFO);
            event.object = callbackContext;
            EventBus.getDefault().post(event);
            return true;

        } else if ("chat".equals(action)) {

            XshellEvent event = new XshellEvent(XshellConsts.IM_Chat);
            event.argObj = args.getJSONObject(0);
            event.object = callbackContext;
            EventBus.getDefault().post(event);
            return true;

        } else if ("browser".equals(action)) {


            JSONObject object = args.getJSONObject(0);
            String param1 = object.getString("url");

            Log.w("amtf", "url:" + param1);

            if (param1.contains("http")) {
                XshellEvent event = new XshellEvent(XshellConsts.New_Browser);
                event.object = callbackContext;
                event.msg = param1;
                EventBus.getDefault().post(event);
                return true;
            }

        } else if ("appStatus".equals(action)) {
            XshellEvent event = new XshellEvent(XshellConsts.APP_Status);
            event.object = callbackContext;
            EventBus.getDefault().post(event);
            return true;
        } else if ("cchelper".equals(action)) {
            XshellEvent event = new XshellEvent(XshellConsts.APP_HelPer);
//            event.argObj = args.getJSONObject(0);

            EventBus.getDefault().post(event);
            return true;
        }
        return false;
    }


}
