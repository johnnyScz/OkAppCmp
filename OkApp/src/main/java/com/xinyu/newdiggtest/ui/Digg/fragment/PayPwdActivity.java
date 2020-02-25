package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.MsgBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.ui.BaseActivity;
import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.utils.AppMD5Util;
import com.xinyu.newdiggtest.utils.AppUtils;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PayPwdActivity extends BaseActivity {


    @BindView(R.id.edt_code)
    public EditText inputMsg;


    @BindView(R.id.tv_sentcode)
    public TextView sendCode;

    //-----------------不同页面不同显示------------------------

    @BindView(R.id.titlt__tv)
    public TextView title;//标题

    @BindView(R.id.left3)
    public TextView left3;//密码

    @BindView(R.id.edt_phone)
    public EditText inputPhone;//输入手机号

    @BindView(R.id.edt_pwd)
    public EditText inputPwd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        if (getIntent().hasExtra(IntentParams.FirstBangPhone)) {
            title.setText("设置支付密码");
            left3.setText("密码");
            inputPwd.setHint("请设置密码");
            inputPhone.setHint("请输入手机号码");
            inputPhone.setEnabled(true);
        } else {
            inputPhone.setText(PreferenceUtil.getInstance(mContext).getPhone());
            inputPhone.setEnabled(false);
            title.setText("忘记密码");
            left3.setText("新密码");
            inputPwd.setHint("请设置新密码");
        }


    }


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_paypwd;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onXshellEvent(XshellEvent event) {

    }

    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


    @OnClick(R.id.btn_commit)
    public void goCommit() {
        if (TextUtils.isEmpty(inputPhone.getText())) {
            ToastUtils.getInstanc(mContext).showToast("请输入手机号");
            return;
        }
        if (!AppUtils.isCellphone(inputPhone.getText().toString())) {
            ToastUtils.getInstanc(mContext).showToast("手机号码非法,请重新输入");
            return;
        }

        if (TextUtils.isEmpty(inputMsg.getText())) {
            ToastUtils.getInstanc(mContext).showToast("请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(inputPwd.getText())) {
            ToastUtils.getInstanc(mContext).showToast("请输入密码");
            return;
        }
        int len = inputPwd.getText().length();
        if (len != 6) {
            ToastUtils.getInstanc(mContext).showToast("密码长度为6位");
            return;
        }
        checkCode();
    }


    @OnClick(R.id.tv_sentcode)
    public void sendMsg() {
        if (TextUtils.isEmpty(inputPhone.getText())) {
            ToastUtils.getInstanc(this).showToast("请输入手机号码");
            return;
        }

        if (!AppUtils.isCellphone(inputPhone.getText().toString())) {
            ToastUtils.getInstanc(mContext).showToast("请输入合法的手机号码");
            return;
        }

        AppUtils.setOnMsgCode(new AppUtils.OnMsgCode() {
            @Override
            public void sendMsg() {
                sendPhoneMsg();
            }
        });
        AppUtils.rxCountCode(60, sendCode, mContext);

    }

    /**
     * 发送短信验证码
     */
    private void sendPhoneMsg() {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
//        map.put("command", "user.SendSmsVerificationCode");
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
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                            return;
                        }
                        if (msg.getOp().getCode().equals("Y")) {
                            ToastUtils.getInstanc(mContext).showToast("短息发送成功");

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }

                    }
                });


    }


    private void commitSetPwd() {

        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);

        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        requsMap.put("phone", inputPhone.getText().toString());
        requsMap.put("pwd", AppMD5Util.MD5(inputPwd.getText().toString()));
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        url.setPayPwd(requsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Info>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                loadingDailog.dismiss();
                ToastUtils.getInstanc(mContext).showToast(e.getMessage());
            }

            @Override
            public void onNext(Info msg) {
                loadingDailog.dismiss();
                if (msg.getOp().getCode().equals("Y")) {
                    if (MyTextUtil.isEmpty(PreferenceUtil.getInstance(mContext).getPhone())) {
                        PreferenceUtil.getInstance(mContext).setMobilePhone(inputPhone.getText().toString().trim());
                    }
                    finish();
                } else {
                    ToastUtils.getInstanc(mContext).showToast("服务异常");
                }

            }
        });
    }


    private void checkCode() {
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "user.CheckSmsCodeAction");
        map.put("mobile", inputPhone.getText().toString().trim());
        map.put("code", inputMsg.getText().toString().trim());
        url.checkCode(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Info>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(Info msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp() == null) {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                            return;
                        }
                        if (msg.getOp().getCode().equals("Y")) {
                            commitSetPwd();
                        } else {
                            ToastUtils.getInstanc(mContext).showToast("验证码错误，请重新验证");
                            return;
                        }
                    }
                });
    }


}




