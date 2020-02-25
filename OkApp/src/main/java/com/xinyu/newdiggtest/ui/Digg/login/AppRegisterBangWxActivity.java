package com.xinyu.newdiggtest.ui.Digg.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.app.HomeAppActivity;
import com.xinyu.newdiggtest.bean.CommonUser;
import com.xinyu.newdiggtest.h5.H5XshellEvent;
import com.xinyu.newdiggtest.net.ApiConfig;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.BingBean;
import com.xinyu.newdiggtest.ui.BaseActivity;
import com.xinyu.newdiggtest.utils.AppContacts;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;
import com.xinyu.newdiggtest.wxapi.WeixinUtil;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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

import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 注册完成绑定微信页面
 */
public class AppRegisterBangWxActivity extends BaseActivity {


    @Override
    protected int getLayoutResouce() {
        return R.layout.digg_register_wx;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {


    }


    @OnClick(R.id.back)
    public void goBack() {
        finish();
    }


    @OnClick(R.id.btn_register)
    public void goWx() {
        WeixinUtil.getInstance().initWeixinApi(this);
        WeixinUtil.getInstance().weixinSendReq();

    }


    ExecutorService executorService = Executors.newCachedThreadPool();

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
                bangWx(obj);
            }
        });
    }


    //绑定微信
    private void bangWx(JSONObject obj) {
        String nickname = "", unionid = "", headUrl = "";
        try {
            nickname = obj.getString("nickname");

            unionid = obj.getString("unionid");
            headUrl = obj.getString("headimgurl");

            //头像名称
            PreferenceUtil.getInstance(this).setHeadUrl(headUrl);
            PreferenceUtil.getInstance(this).setNickName(nickname);

            bingWx(getIntent().getStringExtra("user_id"), unionid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * 手机号绑定微信
     *
     * @param uid
     * @param unionId
     */

    public void bingWx(final String uid, String unionId) {
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        final NetApi url = manager.create(NetApi.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("user_id", uid);
        map.put("account", unionId);

        url.phonebindingWx(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BingBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(BingBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            if (msg.getOp().getInfo().equals("该手机号已绑定过账号")) {
                                ToastUtils.getInstanc().showToast("该手机号已经绑定过了！");

                            } else {

                                CommonUser user = msg.getUser();

                                if (user != null) {

                                    String userId = user.getUser_id();
                                    String mobile = user.getMobile();
                                    PreferenceUtil.getInstance(mContext).setUserId(userId);
                                    PreferenceUtil.getInstance(mContext).setMobilePhone(mobile);

                                    getCompanyList(userId, mobile);
                                }


                            }

                        } else {
                            Log.e("amtf", "服务绑定异常");
                        }
                    }
                });
    }


    /**
     * 绑定成功之后
     *
     * @param userId
     */

    public void getCompanyList(String userId, String mobile) {

        AppContacts.myCompanyList.clear();

        loadingDailog.show();
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
                                    PreferenceUtil.getInstance(mContext).setCompanyId(cmpanyId);
                                    PreferenceUtil.getInstance(mContext).setCompanyType(cmpanyType);
                                    Intent intent = new Intent(mContext, HomeAppActivity.class);
                                    startActivity(intent);
                                    finish();


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
