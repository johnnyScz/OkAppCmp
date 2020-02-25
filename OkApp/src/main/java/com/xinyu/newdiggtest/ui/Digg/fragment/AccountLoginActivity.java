package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.widget.EditText;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.ShenHeLoginBean;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.ui.Digg.HomeDiggActivity;
import com.xinyu.newdiggtest.utils.AppMD5Util;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AccountLoginActivity extends BaseNoEventActivity {


    @BindView(R.id.edt_phone)
    EditText edt_phone;

    @BindView(R.id.edt_password)
    EditText edt_password;


    @Override
    protected int getLayoutResouce() {
        return R.layout.acc_login;
    }


    @OnClick(R.id.back)
    public void goCommit() {
        finish();
    }


    @OnClick(R.id.btn_login)
    public void commitLogin() {

        if (MyTextUtil.isEmpty(edt_phone.getText().toString())) {
            ToastUtils.getInstanc().showToast("请输入账号");
            return;
        }

        if (MyTextUtil.isEmpty(edt_password.getText().toString())) {
            ToastUtils.getInstanc().showToast("请输入账号");
            return;
        }

        login();
    }


    private void login() {

        loadingDailog.show();

        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "user.p_AccountLogin");
        map.put("account", edt_phone.getText().toString());
        map.put("password", AppMD5Util.MD5(edt_password.getText().toString()));
        map.put("account_type", "pwd");


        url.accountLogin(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ShenHeLoginBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();

                    }

                    @Override
                    public void onNext(ShenHeLoginBean msg) {
                        loadingDailog.dismiss();

                        if (msg.getOp().getCode().equals("Y") && msg.getUser() != null) {

                            PreferenceUtil.getInstance(mContext).setWxSession(msg.getSession_id());
                            ShenHeLoginBean.UserShenHeBean myUser = msg.getUser();
                            saveInfo(myUser);
                            startActivity(new Intent(mContext, HomeDiggActivity.class));
                            finish();
                        } else {
                            ToastUtils.getInstanc().showToast("服务异常");
                        }


                    }
                });

    }

    private void saveInfo(ShenHeLoginBean.UserShenHeBean msg) {

        PreferenceUtil.getInstance(mContext).setUserId(msg.getUser_id());
        PreferenceUtil.getInstance(mContext).setHeadUrl(msg.getHead());
        PreferenceUtil.getInstance(mContext).setNickName(msg.getNickname());

    }


}




