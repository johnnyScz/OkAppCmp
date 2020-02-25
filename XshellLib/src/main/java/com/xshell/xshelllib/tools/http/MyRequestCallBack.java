package com.xshell.xshelllib.tools.http;

import com.zhy.http.okhttp.callback.FileCallBack;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;

/**
 * Created by zzy on 2016/8/16.
 * 自定义的callback
 */
public abstract class MyRequestCallBack extends FileCallBack {

    private CallbackContext callbackContext;
    private CordovaArgs args;
    private String key;
    private String callbackName;

    public MyRequestCallBack(String destFileDir, String destFileName) {
        super(destFileDir, destFileName);
    }


    public String getCallbackName() {
        return callbackName;
    }

    public void setCallbackName(String callbackName) {
        this.callbackName = callbackName;
    }

    public CallbackContext getCallbackContext() {
        return callbackContext;
    }

    public void setCallbackContext(CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

    public CordovaArgs getArgs() {
        return args;
    }

    public void setArgs(CordovaArgs args) {
        this.args = args;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
