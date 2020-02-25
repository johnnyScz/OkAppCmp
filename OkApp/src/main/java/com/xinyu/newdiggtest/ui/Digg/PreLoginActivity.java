package com.xinyu.newdiggtest.ui.Digg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.xinyu.newdiggtest.R;


import com.xinyu.newdiggtest.app.HomeAppActivity;

import com.xinyu.newdiggtest.bean.MobileBean;

import com.xinyu.newdiggtest.bigq.BindMobileActivity;
import com.xinyu.newdiggtest.h5.H5XshellEvent;
import com.xinyu.newdiggtest.net.ApiConfig;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.SSLSocket;
import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.ui.BaseActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.AccountLoginActivity;

import com.xinyu.newdiggtest.ui.Digg.fragment.CompanyListActivity;

import com.xinyu.newdiggtest.utils.AppContacts;

import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;
import com.xinyu.newdiggtest.wxapi.WeixinUtil;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import java.net.URL;
import java.net.URLEncoder;

import java.util.HashMap;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import butterknife.OnClick;
import io.socket.client.IO;
import io.socket.client.Socket;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 预登录 微信登录页面
 */
public class PreLoginActivity extends BaseActivity {

    ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    protected int getLayoutResouce() {
        return R.layout.pre_login;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra("Enter_type")) {

            try {
                IO.Options opts = new IO.Options();
                //如果服务端使用的是https 加以下两句代码,文章尾部提供SSLSocket类
                opts.sslContext = SSLSocket.genSSLSocketFactory();
                opts.hostnameVerifier = new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                };
                Socket mSocket = IO.socket(ApiConfig.SOCKET_IP, opts);
                App.setSocket(mSocket);

            } catch (Exception e) {


            }

        }


    }

    @OnClick(R.id.login_wx)
    public void WxLogin() {

        if (!MyTextUtil.isEmpty(PreferenceUtil.getInstance(mContext).getUserId())) {
            startActivity(new Intent(mContext, HomeAppActivity.class));

        } else {
            WeixinUtil.getInstance().initWeixinApi(this);
            WeixinUtil.getInstance().weixinSendReq();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onXshellEvent(H5XshellEvent event) {

        if (event.what == 0x11) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    doGetWxSession();
                }
            });
        }
    }

    private void doGetWxSession() {
        String urlPath = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + ApiConfig.WXAPP_ID + "&secret="
                + ApiConfig.WXAPP_APPSecret + "&code=" + PreferenceUtil.getInstance(this).getWxCode() + "&grant_type=authorization_code";
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
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                doXinyuWxLogin(obj);
            }
        });
    }


    //TODO 需要芯与login
    private void doXinyuWxLogin(JSONObject obj) {
        String openid = "", nickname = "", unionid = "", headUrl = "";
        try {
            openid = obj.getString("openid");
            nickname = obj.getString("nickname");
            PreferenceUtil.getInstance(this).setNickName(nickname);
            unionid = obj.getString("unionid");
            headUrl = obj.getString("headimgurl");
            PreferenceUtil.getInstance(this).setHeadUrl(headUrl);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance(1);
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "user.p_wxAppOAuthLogin");
        map.put("unionid", unionid);
        map.put("openid", openid);
        map.put("headimgurl", headUrl);

        try {
            map.put("nickname", URLEncoder.encode(nickname, "UTF-8"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


//        url.getWxInfo(map).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<WxBean>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e("amtf", "onError" + e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(WxBean msg) {
//
//
//                        if (msg.getOp() == null) {
//                            ToastUtils.getInstanc(mContext).showToast("服务异常");
//                            return;
//                        }
//                        if (msg.getOp().getCode().equals("Y")) {
//                            ToastUtils.getInstanc(mContext).showToast("登录成功！");
//                            String uid = msg.getData().getUserid();
//                            String sessionid = msg.getData().getSessionid();
//
//                            PreferenceUtil.getInstance(mContext).setUserId(uid);
//                            PreferenceUtil.getInstance(mContext).setWxSession(sessionid);
//
//                            getMobile(uid, sessionid);
//
//
//                        } else {
//                            ToastUtils.getInstanc(mContext).showToast(mContext.getResources().getString(R.string.login_error));
//                        }
//
//                    }
//                });
    }


    /**
     * 获取用户的mobile
     *
     * @param userId
     * @param sessionid
     */
    public void getMobile(final String userId, final String sessionid) {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("sid", sessionid);
        map.put("user_id", userId);
        map.put("flag", "Y");

        url.getWxMobile(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MobileBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(MobileBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            String name = msg.getUser().getNickname();
                            PreferenceUtil.getInstance(mContext).setNickName(name);

                            String mobile = msg.getUser().getMobile();

                            if (MyTextUtil.isEmpty(mobile)) {

                                Intent intent = new Intent(mContext, BindMobileActivity.class);

                                intent.putExtra("data", msg.getUser());

                                startActivity(intent);

                            } else {
                                getCompanyList(userId, sessionid, mobile);
                            }


                        } else {
                            ToastUtils.getInstanc().showToast("服务异常");
                        }

                    }
                });
    }


    @OnClick(R.id.login_account)
    public void accountLogin() {

        startActivity(new Intent(mContext, AccountLoginActivity.class));
    }


    /**
     * 获取公司列表
     *
     * @param userId
     * @param sessionid
     * @param mobile
     */

    public void getCompanyList(String userId, String sessionid, String mobile) {

        AppContacts.myCompanyList.clear();

        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("sid", sessionid);
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
                        loadingDailog.dismiss();
                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(Object msg) {
                        loadingDailog.dismiss();

                        String jsonStr = new Gson().toJson(msg);

                        Map<String, Object> data = json2map(jsonStr);

                        Map<String, Object> child = (Map<String, Object>) data.get("data");


                        if (child != null) {

                            if (child.size() == 1) {

                                for (Map.Entry<String, Object> entry : child.entrySet()) {

                                    Object myData = entry.getValue();
                                    String ttChild = new Gson().toJson(myData);
                                    try {
                                        JSONObject mapChild = new JSONObject(ttChild);
                                        JSONArray array = mapChild.getJSONArray("userinfo");

                                        if (array == null || array.length() < 1) {
                                            return;
                                        }

                                        if (array.length() == 1) {
                                            String userId = array.getJSONObject(0).getString("user_id");
//                                            PreferenceUtil.getInstance(mContext).setUser3Id(userId);

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }


                                startActivity(new Intent(mContext, HomeAppActivity.class));

                            } else {
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

                                if (AppContacts.myCompanyList.size() > 0) {

                                    startActivity(new Intent(mContext, CompanyListActivity.class));
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


    @Override
    public void onBackPressed() {
        return;
    }
}
