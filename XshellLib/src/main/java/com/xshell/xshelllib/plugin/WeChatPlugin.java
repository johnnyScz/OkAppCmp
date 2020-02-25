package com.xshell.xshelllib.plugin;


import android.content.Context;


import com.xshell.xshelllib.utils.XshellEvent;
import com.xshell.xshelllib.utils.XshellConsts;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;


/**
 * Created by huang on 2017/11/16.
 */

public class WeChatPlugin extends CordovaPlugin {


    Context mContext;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        mContext = cordova.getActivity();

    }

    /*********
     * 微信登录 end
     ******************/

    @Override
    public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {

        if ("login".equals(action)) {
            XshellEvent event = new XshellEvent(XshellConsts.WxLogin);
            event.object = callbackContext;
            EventBus.getDefault().post(event);
            return true;
        } else if ("share".equals(action)) {
            XshellEvent event = new XshellEvent(XshellConsts.WXshare);
            event.params = args;
            event.object = callbackContext;
            EventBus.getDefault().post(event);
            return true;
        } else if ("shareToWeChatMin".equals(action)) {
            XshellEvent event = new XshellEvent(XshellConsts.WXLittle);
            event.params = args;
            event.object = callbackContext;
            EventBus.getDefault().post(event);
            return true;
        }

        return false;
    }


}
