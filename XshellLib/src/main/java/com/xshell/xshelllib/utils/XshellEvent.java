package com.xshell.xshelllib.utils;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by DELL on 2018/3/6.
 */

public class XshellEvent {
    public int what;

    public String msg;

    public String extra;

    public CallbackContext object;

    public JSONArray params;

    public JSONObject argObj;


    public XshellEvent(int tag) {
        this.what = tag;
    }
}
