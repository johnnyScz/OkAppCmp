package com.xshell.xshelllib.tools.socketutil;

import org.apache.cordova.CallbackContext;

/**
 * Created by ZHOUCHAO on 2016/3/21.
 */
public abstract class OnResultMessage {

    private CallbackContext callBackContext;

    public abstract void resultMessage(String result);

    public void setCallBackContext(CallbackContext callBackContext) {
        this.callBackContext = callBackContext;
    }

    public CallbackContext getCallBackContext() {
        return callBackContext;
    }
}
