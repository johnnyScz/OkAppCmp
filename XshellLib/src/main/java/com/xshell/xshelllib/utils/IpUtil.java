package com.xshell.xshelllib.utils;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by zzy on 2016/10/14.
 * 请求Xversion接口获得ip
 */
public class IpUtil {

    private static String ip = "127.0.0.1";

    public static void requestIp() {
        //直接请求网址，获取ip所在的地区
        OkHttpUtils.get().url("http://pv.sohu.com/cityjson?ie=utf-8").build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                call.cancel();
            }

            @Override
            public void onResponse(String result, int i) {
                try {
                    int index = result.indexOf("{");
                    String substring = result.substring(index, result.length() - 1);
                    JSONObject object = new JSONObject(substring);
                    ip = object.getString("cip");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public static String getIp() {
        return ip;
    }

}
