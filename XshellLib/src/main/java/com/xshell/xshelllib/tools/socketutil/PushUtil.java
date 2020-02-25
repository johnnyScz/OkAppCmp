package com.xshell.xshelllib.tools.socketutil;

import org.apache.cordova.CallbackContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZHOUCHAO on 2016/4/13.
 * 推送工具类
 */
public class PushUtil {



    /**
     * 推送
     */
    private static Map<String,OnPushTextMessage> onPushTextMessages = new HashMap<String,OnPushTextMessage>();



    /**
     * 注册推送
     * @param url url
     * @param callbackContext callbackContext
     */
    public static void registerPush(String url, final CallbackContext callbackContext){
//        SocketUtil.sendPushMessage(url, new OnResultMessage() {
//            @Override
//            public void resultMessage(String result) {
//                callbackContext.success(result);
//            }
//        });

//        SocketUtil.sendTextMessage("tradepush",url, new OnResultMessage() {
//            @Override
//            public void resultMessage(String result) {
//                  LogUtils.e("shen","注册成交");
//                callbackContext.success(result);
//            }
//        });
    }

    /**
     * 订阅一个推送
     */
//    public static void subscriptionPush(String url, final CallbackContext callbackContext){
    public static void subscriptionPush(String url, final CallbackContext callbackContext){
        SocketUtil.sendsubscriptionMessage(url, new OnResultMessage() {
            @Override
            public void resultMessage(String result) {
                for (OnPushTextMessage onText : onPushTextMessages.values()) {
                    onText.onPushTextMessage(result);
                    callbackContext.success(result);
                }
            }
        });
    }

    //设置订阅回掉
    public static  void setOnPushTextMessages(String key, OnPushTextMessage onPushTextMessage){
        onPushTextMessages.put(key, onPushTextMessage);
    }

    //移除推送回掉
    public static void removeWtpush(String key){
        onPushTextMessages.remove(key);
    }

    //撤回一个订阅
    public static void cancelSubscriptionOrderWTPush(String cmd, String str) {

    }


}
