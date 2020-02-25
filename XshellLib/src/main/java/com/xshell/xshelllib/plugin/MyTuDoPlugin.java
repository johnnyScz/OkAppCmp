package com.xshell.xshelllib.plugin;

import android.content.Context;
import android.util.Log;

import com.xshell.xshelllib.sqlite.DataLocalityHuManager;
import com.xshell.xshelllib.tools.http.MyRequestCallBack;
import com.xshell.xshelllib.utils.EncodeUtil;
import com.xshell.xshelllib.utils.FileUtil;
import com.xshell.xshelllib.utils.LogUtils;
import com.xshell.xshelllib.utils.TimeUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;

import static android.R.attr.value;

/**
 * creat by liuqiang
 * 待办相关
 */

public class MyTuDoPlugin extends CordovaPlugin {

    private Context context;


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        context = cordova.getActivity();

    }

    @Override
    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {

        if ("todo".equals(action)) {

            Log.e("amtf", "我是待办 ");
            return true;

        } else if ("detail".equals(action)) {

            Log.e("amtf", "我是待办详情");


            return true;
        } else if ("assign".equals(action)) {

            Log.e("amtf", "我是交办");


            return true;
        }


        return false;
    }


}
