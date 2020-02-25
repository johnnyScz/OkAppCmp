package com.xshell.xshelllib.plugin;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.widget.Toast;

import com.xshell.xshelllib.logutil.LogUtils;
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
 * Created by DELL on 2017/11/15.
 */

public class DevicePlugin extends CordovaPlugin {


    private Activity activity;


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        activity = cordova.getActivity();
    }


    @Override
    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {


        if ("getInfo".equals(action)) { // 获取设备id
            XshellEvent event = new XshellEvent(XshellConsts.DeviceInfo);
            event.object = callbackContext;
            EventBus.getDefault().post(event);
            return true;
        } else if ("touchid".equals(action)) {//指纹识别
            LogUtils.e("huanghu", "指纹识别");
            Toast.makeText(cordova.getActivity(), "指纹识别", Toast.LENGTH_SHORT).show();
            return true;
        } else if ("changeOrientation".equals(action)) {//横竖屏切换
            LogUtils.e("huanghu", "横竖屏切换:" + args.getString(0));
            if ("1".equals(args.getString(0))) {
                cordova.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            } else {
                cordova.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            }
            return true;
        } else if ("location".equals(action)) {
            XshellEvent event = new XshellEvent(XshellConsts.LOCATION_Info);
            event.object = callbackContext;
            EventBus.getDefault().post(event);
            return true;
        } else if ("showMapInfo".equals(action)) {
            XshellEvent event = new XshellEvent(XshellConsts.Show_LOCATION_Map);
            event.object = callbackContext;
            EventBus.getDefault().post(event);
            return true;
        } else if ("camera".equals(action)) {
            XshellEvent event = new XshellEvent(XshellConsts.Card_Camera);
            event.object = callbackContext;
            EventBus.getDefault().post(event);
            return true;
        } else if ("album".equals(action)) {
            XshellEvent event = new XshellEvent(XshellConsts.IMG_album);
            event.object = callbackContext;
            EventBus.getDefault().post(event);
            return true;
        } else if ("map".equals(action)) {
            XshellEvent event = new XshellEvent(XshellConsts.MAP);
            event.object = callbackContext;
            event.extra = args.getString(0);
            EventBus.getDefault().post(event);
            return true;
        } else if ("distance".equals(action)) {
            XshellEvent event = new XshellEvent(XshellConsts.DISTANCE);
            event.object = callbackContext;
            String type = args.getString(0);

            if (type.equals("1")) {
                String begin = args.getString(1);
                String end = args.getString(2);
                event.msg = begin;
                event.extra = end;
            }
            EventBus.getDefault().post(event);
            return true;
        } else if ("scanQRCode".equals(action)) {

            XshellEvent event = new XshellEvent(XshellConsts.SCan);
            event.object = callbackContext;
            EventBus.getDefault().post(event);
            return true;
        } else if ("call".equals(action)) {
            XshellEvent event = new XshellEvent(XshellConsts.Call);
            event.msg = args.getString(0);
            EventBus.getDefault().post(event);
            return true;
        }
        return false;
    }

}
