package com.xinyu.newdiggtest.ui;

import org.json.JSONObject;

/**
 * Created by DELL on 2018/5/8
 */

public class XshellEvent {
    public int what;

    public int type = -1;//-1 全量更新 0.待办点赞或评论  1.关注点赞或者评论

    public String msg;

    public String obj1;

    public JSONObject object;

    public void setType(int mtype) {
        this.type = mtype;
    }

    public void setJson(JSONObject obj) {
        object = obj;
    }

    public XshellEvent(int tag) {
        this.what = tag;
    }
}
