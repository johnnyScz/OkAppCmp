package com.xinyu.newdiggtest.ui.Digg;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.android.tu.loadingdialog.LoadingDailog;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.utils.AppUtils;
import com.xinyu.newdiggtest.utils.DialogUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RigesterActivity extends Activity {

    @BindView(R.id.edt_phone)
    public EditText inputPhone;

    @BindView(R.id.edt_password)
    public EditText inputPwd;


    @BindView(R.id.counttextview)
    public TextView llSms;

    @BindView(R.id.tv_title)
    public TextView title;

    LoadingDailog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.digg_register);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        title.setText("注册");
        dialog = new DialogUtil(this).buildDialog("加载中...");
    }


    @OnClick(R.id.btn_register)
    public void goRigester() {
        if (TextUtils.isEmpty(inputPhone.getText()) || TextUtils.isEmpty(inputPwd.getText())) {
            ToastUtils.getInstanc(this).showToast("手机号码或验证码不能为空！");
            return;
        }
        dialog.show();
    }


    @OnClick(R.id.counttextview)
    public void sendSms() {
        if (TextUtils.isEmpty(inputPhone.getText())) {
            ToastUtils.getInstanc(this).showToast("请输入正确的手机号码！");
            return;
        }
        AppUtils.setOnMsgCode(new AppUtils.OnMsgCode() {
            @Override
            public void sendMsg() {
                goSendMsg();
            }
        });
        AppUtils.rxCountCode(20, llSms);

    }

    private void goSendMsg() {


    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }
}
