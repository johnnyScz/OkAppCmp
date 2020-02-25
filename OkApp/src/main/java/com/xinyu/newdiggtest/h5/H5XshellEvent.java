package com.xinyu.newdiggtest.h5;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by DELL on 2018/3/6.
 */

public class H5XshellEvent {
    public int what;

    public String msg;

    public String extra;


    public JSONArray params;

    public JSONObject object;


    public H5XshellEvent(int tag) {
        this.what = tag;
    }
}
