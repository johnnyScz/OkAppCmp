package com.xinyu.newdiggtest.ui.Digg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.tu.loadingdialog.LoadingDailog;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.MsgBean;
import com.xinyu.newdiggtest.bean.UserInfoBean;
import com.xinyu.newdiggtest.net.ApiConfig;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;

import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.utils.AppUtils;
import com.xinyu.newdiggtest.utils.DialogUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;
import com.xinyu.newdiggtest.wxapi.WeixinUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends Activity {

    @BindView(R.id.edt_phone)
    public EditText inputPhone;

    @BindView(R.id.edt_password)
    public EditText inputPwd;

    @BindView(R.id.btn_login)
    public Button comBtn;

    @BindView(R.id.tv_error_phone)
    public TextView errorPhone;


    @BindView(R.id.counttextview)
    public TextView llSms;

    ExecutorService executorService = Executors.newCachedThreadPool();

    LoadingDailog dialog;

    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.digg_login1);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();

    }

    private void initView() {
        context = this;
        dialog = new DialogUtil(this).getInstance();
        inputPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                comBtn.setEnabled(true);
                llSms.setEnabled(true);
                errorPhone.setVisibility(View.INVISIBLE);
            }
        });

        this.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    @OnClick(R.id.btn_login)
    public void goCommit() {

        if (TextUtils.isEmpty(inputPhone.getText())) {
            ToastUtils.getInstanc(this).showToast("请输入手机号码！");
            return;
        }

        if (!AppUtils.isCellphone(inputPhone.getText().toString())) {
            comBtn.setEnabled(false);
            errorPhone.setVisibility(View.VISIBLE);
            return;
        }

        if (TextUtils.isEmpty(inputPwd.getText())) {
            ToastUtils.getInstanc(this).showToast("请输入验证码！");
            return;
        }
        login(inputPwd.getText().toString());


    }


    @OnClick(R.id.iv_wx)
    public void wxLogin() {
        WeixinUtil.getInstance().initWeixinApi(this);
        WeixinUtil.getInstance().weixinSendReq();
    }


    @OnClick(R.id.counttextview)
    public void sendSms() {
        if (TextUtils.isEmpty(inputPhone.getText())) {
            ToastUtils.getInstanc(this).showToast("请输入手机号码");
            return;
        }

        if (!AppUtils.isCellphone(inputPhone.getText().toString())) {
            llSms.setEnabled(false);
            errorPhone.setVisibility(View.VISIBLE);
            return;
        }

        AppUtils.setOnMsgCode(new AppUtils.OnMsgCode() {
            @Override
            public void sendMsg() {
                llSms.setEnabled(false);
                sendPhoneMsg();
            }
        });
        AppUtils.rxCountCode(60, llSms);

    }

    /**
     * 发送短信验证码
     */
    private void sendPhoneMsg() {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "user.SendSmsVerificationCodeBase");
        map.put("mobile", inputPhone.getText().toString().trim());

        url.sendPhoneMsg(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MsgBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("amtf", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(MsgBean msg) {
                        if (msg.getOp() == null) {
                            ToastUtils.getInstanc(LoginActivity.this).showToast("服务异常");
                            return;
                        }
                        if (msg.getOp().getCode().equals("Y")) {
                            ToastUtils.getInstanc(LoginActivity.this).showToast("短息发送成功");

                        } else {
                            ToastUtils.getInstanc(LoginActivity.this).showToast("服务异常");
                        }

                    }
                });


    }


    /**
     * 验证码登录
     */
    private void login(String code) {
        dialog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "user.p_MobileSmsLogin");
        map.put("mobile", inputPhone.getText().toString().trim());
        map.put("code", code);

        url.sendPhoneMsg(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MsgBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        ToastUtils.getInstanc(LoginActivity.this).showToast("网络或服务异常!");
                        Log.e("amtf", e.getMessage());
                    }

                    @Override
                    public void onNext(MsgBean msg) {
                        dialog.dismiss();
                        if (msg.getOp() == null) {
                            ToastUtils.getInstanc(LoginActivity.this).showToast("验证码错误");
                            return;
                        }
                        if (msg.getOp().getCode().equals("Y")) {
                            if (msg.getData() != null) {
                                PreferenceUtil.getInstance(LoginActivity.this).setUserId(msg.getData().getUserid());
                                PreferenceUtil.getInstance(LoginActivity.this).setWxSession(msg.getData().getSessionid());
                                requestUserInfo(msg.getData().getUserid());
                            }


                        } else if (msg.getOp().getCode().equals("N")) {
                            ToastUtils.getInstanc(LoginActivity.this).showToast(context.getResources().getString(R.string.login_code_no));
                            return;
                        } else {
                            ToastUtils.getInstanc(LoginActivity.this).showToast("服务异常");
                        }

                    }
                });
    }


    /**
     * 手机号登陆后查询用户名称
     */
    public void requestUserInfo(final String userId) {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("command", "user.getUserInfo");
        requsMap.put("user_id", userId);
        requsMap.put("flag", "Y");
        url.getUserInfo(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserInfoBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        ToastUtils.getInstanc(context).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(UserInfoBean msg) {
                        if (msg.getOp().getCode().equals("Y")) {

                            ToastUtils.getInstanc(LoginActivity.this).showToast("登录成功！");

                            PreferenceUtil.getInstance(LoginActivity.this).setNickName(msg.getUser().getNickname());
                            PreferenceUtil.getInstance(LoginActivity.this).setMobilePhone(msg.getUser().getMobile());
                            startActivity(new Intent(context, HomeDiggActivity.class));
                            finish();


                        } else {
                            ToastUtils.getInstanc(context).showToast("服务异常");
                        }
                    }
                });
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onXshellEvent(XshellEvent event) {
        if (event.what == 0x11) {
            getWxInfo();
        }
    }

    private void getWxInfo() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                doGetWxSession();
            }
        });
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

    private void doXinyuWxLogin(JSONObject obj) {
        String openid = "", nickname = "", unionid = "", headUrl = "", city = "";
        try {
            openid = obj.getString("openid");
            nickname = obj.getString("nickname");
            PreferenceUtil.getInstance(this).setNickName(nickname);
            unionid = obj.getString("unionid");
            headUrl = obj.getString("headimgurl");
            city = obj.getString("city");
            PreferenceUtil.getInstance(this).setHeadUrl(headUrl);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "user.p_wxAppOAuthLogin");
        map.put("openid", openid);
        map.put("city", city);
        map.put("headimgurl", headUrl);

        try {
            map.put("nickname", URLEncoder.encode(nickname, "UTF-8"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        map.put("unionid", unionid);

        url.sendPhoneMsg(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MsgBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("amtf", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(MsgBean msg) {
                        if (msg.getOp() == null) {
                            ToastUtils.getInstanc(LoginActivity.this).showToast("服务异常");
                            return;
                        }
                        if (msg.getOp().getCode().equals("Y")) {
                            ToastUtils.getInstanc(LoginActivity.this).showToast("登录成功！");
                            String uid = msg.getData().getUserid();
                            String sessionid = msg.getData().getSessionid();
                            PreferenceUtil.getInstance(LoginActivity.this).setUserId(uid);
                            PreferenceUtil.getInstance(LoginActivity.this).setWxSession(sessionid);
                            startActivity(new Intent(LoginActivity.this, HomeDiggActivity.class));
                            finish();
                        } else {
                            ToastUtils.getInstanc(LoginActivity.this).showToast(context.getResources().getString(R.string.login_error));
                        }

                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
