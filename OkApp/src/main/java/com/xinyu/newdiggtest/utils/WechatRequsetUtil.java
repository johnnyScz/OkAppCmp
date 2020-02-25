package com.xinyu.newdiggtest.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.xinyu.newdiggtest.app.HomeAppActivity;
import com.xinyu.newdiggtest.h5.H5XshellEvent;
import com.xinyu.newdiggtest.net.ApiConfig;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 微信登录跟获取公司列表相关
 */
public class WechatRequsetUtil {

    ExecutorService executorService = Executors.newCachedThreadPool();

    Context context;

    public WechatRequsetUtil(Context mcx) {

        this.context = mcx;

    }

    public void getWxInfo() {

        //
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                doGetWxSession();
            }
        });

    }

    private void doGetWxSession() {
        String urlPath = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + ApiConfig.WXAPP_ID + "&secret="
                + ApiConfig.WXAPP_APPSecret + "&code=" + PreferenceUtil.getInstance(context).getWxCode() + "&grant_type=authorization_code";
        InputStream is = null;
        try {
            URL url = new URL(urlPath);
            //打开连接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            if (200 == urlConnection.getResponseCode()) {
                //得到输入流
                is = urlConnection.getInputStream();
                StringBuffer out = new StringBuffer();
                byte[] b = new byte[1024];
                for (int n; (n = is.read(b)) != -1; ) {
                    out.append(new String(b, 0, n));
                }
                JSONObject obj = new JSONObject(out.toString());
                String token = obj.getString("access_token");
                String openId = obj.getString("openid");
                getWxNetInfo(token, openId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void getWxNetInfo(String token, String openid) {
        String urlPath = "https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + openid;
        InputStream is = null;
        try {
            URL url = new URL(urlPath);
            //打开连接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (200 == urlConnection.getResponseCode()) {
                //得到输入流
                is = urlConnection.getInputStream();
                StringBuffer out = new StringBuffer();
                byte[] b = new byte[1024];
                for (int n; (n = is.read(b)) != -1; ) {
                    out.append(new String(b, 0, n));
                }
                JSONObject obj = new JSONObject(out.toString());
                xinyuWxlogin(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void xinyuWxlogin(final JSONObject obj) {

        if (obj == null) {
            return;
        }

        String nickname = "", unionid = "", headUrl = "";
        try {
            nickname = obj.getString("nickname");

            unionid = obj.getString("unionid");
            headUrl = obj.getString("headimgurl");

            //头像名称
            PreferenceUtil.getInstance(context).setHeadUrl(headUrl);
            PreferenceUtil.getInstance(context).setNickName(nickname);

            H5XshellEvent event = new H5XshellEvent(EventConts.WeChat_net_Info);
            event.object = obj;

            //TODO 把请求到的数据返回给调用的地方
            EventBus.getDefault().post(event);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void getCompanyList(String userId, String mobile) {

        AppContacts.myCompanyList.clear();

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("mobile", mobile);

        url.getCompanyList(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(Object msg) {


                        String jsonStr = new Gson().toJson(msg);

                        Map<String, Object> data = json2map(jsonStr);

                        Map<String, Object> child = (Map<String, Object>) data.get("data");


                        if (child != null) {

                            for (Map.Entry<String, Object> entry : child.entrySet()) {

                                Object myData = entry.getValue();
                                String ttChild = new Gson().toJson(myData);
                                try {
                                    JSONObject mapChild = new JSONObject(ttChild);

                                    AppContacts.myCompanyList.add(mapChild);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (AppContacts.myCompanyList != null && AppContacts.myCompanyList.size() > 0) {

                                JSONObject object = AppContacts.myCompanyList.get(0);

                                try {

                                    String cmpanyId = object.getString("id");//公司id

                                    String cmpanyType = object.getString("company_type");//公司类型

                                    //保存公司信息
                                    PreferenceUtil.getInstance(context).setCompanyId(cmpanyId);
                                    PreferenceUtil.getInstance(context).setCompanyType(cmpanyType);
                                    Intent intent = new Intent(context, HomeAppActivity.class);
                                    context.startActivity(intent);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                });
    }

    public static Map<String, Object> json2map(String str_json) {
        Map<String, Object> res = null;
        try {
            Gson gson = new Gson();
            res = gson.fromJson(str_json, new TypeToken<Map<String, Object>>() {
            }.getType());
        } catch (JsonSyntaxException e) {

            Log.e("amtf", "格式异常：" + e.getMessage());
        }
        return res;
    }


}
