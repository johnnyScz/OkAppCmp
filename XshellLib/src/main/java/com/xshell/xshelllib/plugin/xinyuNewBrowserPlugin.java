package com.xshell.xshelllib.plugin;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.xshell.xshelllib.application.AppConstants;
import com.xshell.xshelllib.utils.Log2FileUtil;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wn on 2016/5/8.
 */
public class xinyuNewBrowserPlugin extends CordovaPlugin {

    /**
     * 当打开一个页面时，需要调用回调函数名字
     */
    private String newBroserCallBcak;
    private boolean isFlay = true;
    private Handler handler = new Handler();

    @Override
    public boolean execute(String action, CordovaArgs args,
                           CallbackContext callbackContext) throws JSONException {
        try {
            if ("xinyuNewBrowser".endsWith(action)) { // 开启一个新的Activity
                if (isFlay) {
                    isFlay = false;
                    JSONObject jos = args.getJSONObject(0);
                    if (jos.has("callbackName")) {
                        newBroserCallBcak = jos.getString("callbackName");
                    }
                    String url = jos.getString("url");
                    Log2FileUtil.getInstance().saveCrashInfo2File("开启了一个新的Activity");

                    Intent intent1 = new Intent();
                    intent1.setAction(AppConstants.ACTION_NEW_BROSER);
                    intent1.putExtra("url", url);
                    LocalBroadcastManager.getInstance(cordova.getActivity()).sendBroadcast(intent1);
                    Log.e("amtf", "xinyuNewBrowser:开启一个新的Activity");

//                    MessageEvent event = new MessageEvent(XshellConsts.OPEN_NEW_BROWSER);
//                    event.msg = url;
//                    EventBus.getDefault().post(event);
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            isFlay = true;
//                        }
//                    }, 1000);
                }
                return true;
            } else if ("closeNewBrowser".equals(action)) { // 关闭Activity
//                EventBus.getDefault().post(new MessageEvent(XshellConsts.CLOSE_NEW_BROWSER));
                return true;
            }
            return true;
        } catch (Exception e) {
            Log.e("amtf", "打开app页面异常：" + e.getMessage());
            return false;
        }
    }


    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
        if (newBroserCallBcak != null) {
            cordova.getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    webView.loadUrl("javascript:" + newBroserCallBcak + "()");
                    newBroserCallBcak = null;
                }
            });
        }

    }
}
