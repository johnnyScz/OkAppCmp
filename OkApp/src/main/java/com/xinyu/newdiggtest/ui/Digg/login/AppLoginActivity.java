package com.xinyu.newdiggtest.ui.Digg.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.xinyu.newdiggtest.APPConstans;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.app.HomeAppActivity;
import com.xinyu.newdiggtest.bean.CommonUser;
import com.xinyu.newdiggtest.bean.LoginBean;
import com.xinyu.newdiggtest.bean.UserLgBean;
import com.xinyu.newdiggtest.bean.WxBean;
import com.xinyu.newdiggtest.h5.H5XshellEvent;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.ui.BaseActivity;

import com.xinyu.newdiggtest.utils.AppUtils;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;
import com.xinyu.newdiggtest.utils.WechatRequsetUtil;
import com.xinyu.newdiggtest.wxapi.WeixinUtil;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AppLoginActivity extends BaseActivity {

    @BindView(R.id.edt_phone)
    EditText edt_phone;


    @BindView(R.id.edt_code)
    EditText edt_code;

    @BindView(R.id.counttextview)
    TextView counttextview;

    @BindView(R.id.error_code)
    TextView error_code;//验证码错误


    @BindView(R.id.line2)
    View line2;

    @BindView(R.id.line1)
    View line1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    private void initView() {


        Intent intent = getIntent();

        if (intent.hasExtra("phone")) {
            edt_phone.setText(intent.getStringExtra("phone"));
        }

        edt_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                resetView();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() > 0) {
                    line1.setBackgroundColor(mContext.getResources().getColor(R.color.button_vip));
                } else {
                    line1.setBackgroundColor(mContext.getResources().getColor(R.color.bar_grey));
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        edt_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                resetView();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() > 0) {
                    line2.setBackgroundColor(mContext.getResources().getColor(R.color.button_vip));
                } else {
                    line2.setBackgroundColor(mContext.getResources().getColor(R.color.bar_grey));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void resetView() {
        line1.setBackgroundColor(mContext.getResources().getColor(R.color.bar_grey));
        line2.setBackgroundColor(mContext.getResources().getColor(R.color.bar_grey));
    }

    @Override
    protected int getLayoutResouce() {
        return R.layout.digg_register;
    }


    @OnClick(R.id.go_register)
    public void goRegister() {

        startActivity(new Intent(mContext, AppRegisterActivity.class));
    }


    @OnClick(R.id.wechat)
    public void goChatInfo() {
        WeixinUtil.getInstance().initWeixinApi(this);
        WeixinUtil.getInstance().weixinSendReq();

        APPConstans.FromLoginPage = 1;
    }


    @OnClick(R.id.btn_login)
    public void goCommit() {

        if (MyTextUtil.isEmpty(edt_phone.getText().toString())) {
            ToastUtils.getInstanc().showToast("请输入手机号码");
            return;
        }


        if (!AppUtils.isCellphone(edt_phone.getText().toString())) {
            ToastUtils.getInstanc().showToast("手机号码不合法,请重新输入");
            return;
        }


        if (MyTextUtil.isEmpty(edt_code.getText().toString())) {
            ToastUtils.getInstanc().showToast("请输入验证码");
            return;
        }

        goLogin();
    }


    @OnClick(R.id.counttextview)
    public void sendCode() {
        if (MyTextUtil.isEmpty(edt_phone.getText().toString())) {
            ToastUtils.getInstanc().showToast("请输入手机号码!");
            return;
        }

        if (!AppUtils.isCellphone(edt_phone.getText().toString())) {
            ToastUtils.getInstanc().showToast("请输入正确的手机号码");
            return;
        }

        AppUtils.setOnMsgCode(new AppUtils.OnMsgCode() {
            @Override
            public void sendMsg() {
                goSendCode();
            }
        });

        AppUtils.rxCountCode(60, counttextview, mContext);

    }


    public void goSendCode() {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", edt_phone.getText().toString());
        url.sendPhoneCode(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Info>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.getInstanc().showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(Info msg) {

                        if (msg.getOp().getCode().equals("Y")) {
                            ToastUtils.getInstanc().showToast("验证码发送成功");
                        }
                    }
                });


    }


    public void goLogin() {

        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        final AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("phonenum", edt_phone.getText().toString());
        map.put("code", edt_code.getText().toString());

        url.loginNew(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(LoginBean msg) {
                        loadingDailog.dismiss();

                        if (msg.getOp().getCode().equals("Y")) {

                            String info = msg.getOp().getInfo();

                            if (MyTextUtil.isEmpty(info)) {
                                error_code.setVisibility(View.GONE);
                                if (msg.getLoginbean() != null) {
                                    LoginBean.LoginbeanBean data = msg.getLoginbean();
                                    UserLgBean user = data.getUser();
                                    if (user != null && !MyTextUtil.isEmpty(user.getUser_id())) {

                                        String sid = data.getSessionid();

                                        PreferenceUtil.getInstance(mContext).setWxSession(sid);
                                        PreferenceUtil.getInstance(mContext).setNickName(user.getName());
                                        PreferenceUtil.getInstance(mContext).setUserId(user.getUser_id());
                                        PreferenceUtil.getInstance(mContext).setMobilePhone(user.getMobile());

                                        getCompanyList(user.getUser_id(), sid, user.getMobile());

                                    }
                                }


                            } else {
                                if (info.equals("请重新获取验证码")) {
                                    ToastUtils.getInstanc().showToast(info);
                                    error_code.setVisibility(View.GONE);
                                } else if (info.equals("账号不存在")) {
                                    ToastUtils.getInstanc().showToast(info);
                                    error_code.setVisibility(View.GONE);
                                } else if (info.equals("验证码不正确")) {
                                    error_code.setVisibility(View.VISIBLE);

                                }
                            }

                        } else {
                            ToastUtils.getInstanc().showToast("服务异常");
                        }

                    }
                });
    }


    /**
     * 获取公司列表
     *
     * @param userId
     * @param sessionid
     * @param mobile
     */

    public void getCompanyList(String userId, String sessionid, String mobile) {


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

                        JSONArray array = new JSONArray();


                        if (child != null) {

                            for (Map.Entry<String, Object> entry : child.entrySet()) {
                                Object myData = entry.getValue();
                                String ttChild = new Gson().toJson(myData);
                                try {

                                    JSONObject mapChild = new JSONObject(ttChild);

                                    array.put(mapChild);


                                    if (array.length() == 1) {

                                        String company = mapChild.getString("id");
                                        String sid = mapChild.getJSONArray("userinfo").getJSONObject(0).getString("session_id");
                                        PreferenceUtil.getInstance(mContext).setWxSession(sid);
                                        PreferenceUtil.getInstance(mContext).setCompanyId(company);

                                        JSONObject object = mapChild.getJSONArray("userinfo").getJSONObject(0).getJSONObject("user");
                                        saveInfo(object);

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            PreferenceUtil.getInstance(mContext).setCompanyInfo(array.toString());

                            startActivity(new Intent(mContext, HomeAppActivity.class));

                            finish();
                        }
                    }
                });
    }

    /**
     * 公司信息返回的信息替换
     *
     * @param object
     */
    private void saveInfo(JSONObject object) {

        try {
            String userId = object.getString("user_id");
            String nikeName = object.getString("name");
            String head = object.getString("head");


            PreferenceUtil.getInstance(mContext).setUserId(userId);
            PreferenceUtil.getInstance(mContext).setNickName(nikeName);
            PreferenceUtil.getInstance(mContext).setHeadUrl(head);

            PreferenceUtil.getInstance(mContext).setMobilePhone(object.getString("mobile"));

            if (object.toString().contains("province") && object.toString().contains("city")) {

                String province = object.getString("province");

                String city = object.getString("city");

                PreferenceUtil.getInstance(mContext).setAdress(province + " " + city);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


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

    WechatRequsetUtil util;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onXshellEvent(H5XshellEvent event) {

        if (event.what == EventConts.WeChat_net_Info) {

            doXinyuWxLogin(event.object);

        }

        if (event.what == 0x11) {
            if (APPConstans.FromLoginPage == 1) {
                util = new WechatRequsetUtil(mContext);
                util.getWxInfo();
            }
        }

    }


    //TODO 授权后微信登录
    private void doXinyuWxLogin(JSONObject obj) {

        String openid = "", nickname = "", unionid = "", headUrl = "";
        try {
            openid = obj.getString("openid");
            nickname = obj.getString("nickname");

            unionid = obj.getString("unionid");
            headUrl = obj.getString("headimgurl");

            PreferenceUtil.getInstance(mContext).setNickName(nickname);
            PreferenceUtil.getInstance(mContext).setHeadUrl(headUrl);


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

        // WxBean
        url.getWxInfo(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WxBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("amtf", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(WxBean msg) {       //WxBean


                        if (msg.getOp() == null) {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                            return;
                        }
                        if (msg.getOp().getCode().equals("Y")) {
                            ToastUtils.getInstanc(mContext).showToast("登录成功！");
                            String uid = msg.getData().getUserid();
                            String sessionid = msg.getData().getSessionid();

                            PreferenceUtil.getInstance(mContext).setUserId(uid);
                            PreferenceUtil.getInstance(mContext).setWxSession(sessionid);

                            if (msg.getData() != null && msg.getData().getUser() != null) {

                                CommonUser user = msg.getData().getUser();

                                if (MyTextUtil.isEmpty(user.getMobile())) {

                                    Intent intent = new Intent(mContext, BangMobileActivity.class);

                                    intent.putExtra("userId", uid);
                                    intent.putExtra("sid", sessionid);

                                    startActivity(intent);

                                } else {
                                    if (!MyTextUtil.isEmpty(PreferenceUtil.getInstance(mContext).getCompanyId())) {
                                        startActivity(new Intent(mContext, HomeAppActivity.class));
                                    } else {
                                        util.getCompanyList(uid, user.getMobile());
                                    }
                                }
                            }


                        } else {
                            ToastUtils.getInstanc(mContext).showToast(mContext.getResources().getString(R.string.login_error));
                        }

                    }
                });
    }


    @Override
    public void onBackPressed() {
        return;
    }
}
