package com.xshell.xshelllib.plugin;

import android.util.Log;

import com.xshell.xshelllib.tools.socketutil.OnPushTextMessage;
import com.xshell.xshelllib.tools.socketutil.OnResultMessage;
import com.xshell.xshelllib.tools.socketutil.OnSocketListener;
import com.xshell.xshelllib.tools.socketutil.PushUtil;
import com.xshell.xshelllib.tools.socketutil.SocketUtil;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;

/**
 * Created by zzy on 2016/8/7.
 * <p>
 * WebSocket插件
 */
public class WebSocketPlugin extends CordovaPlugin implements OnSocketListener, OnPushTextMessage {
    private CallbackContext callbackContext;
    private CordovaArgs args;
    private String pushCallbackName;

    @Override
    public boolean execute(String action, CordovaArgs args, final CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        this.args = args;
        if ("openWebSocket".equals(action)) {  //开启一个websoket
            String url = args.getString(0);
            if (url == null) {
                callbackContext.error("url is null!");
                return false;
            }
            SocketUtil.connect(url, this);
            return true;
        } else if ("sendMessage".equals(action)) {  //发送websocket信息
            String content = args.getString(0);
            if (content == null) {
                callbackContext.error("content is null!");
                return false;
            }
            SocketUtil.sendPushMessage(content, new OnResultMessage() {
                @Override
                public void resultMessage(String result) {
                    Log.i("zzy", "result:" + result);
                    callbackContext.success(result);
                }
            });
            return true;
        } else if ("registerPush".equals(action)) {  //注册推送
            String content = args.getString(0);
            PushUtil.registerPush(content, callbackContext);
            return true;
        } else if ("subscribePush".equals(action)) {  //订阅推送
            String content = args.getString(0);
            pushCallbackName = args.getString(1);
            PushUtil.setOnPushTextMessages(content, this);
            PushUtil.subscriptionPush(content, callbackContext);
            return true;
        }
        return false;
    }

    @Override
    public void onSocketOpen() {
        Log.i("zzy", "0000000000000:");
        callbackContext.success("openSuccess!");
    }

    @Override
    public void onPushTextMessage(String result) {
        Log.i("zzy", "-----------111-------:" + result);
        webView.loadUrl("javascript:" + pushCallbackName + "('" + result + "')");

    }
}
