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

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.app.HomeAppActivity;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.AppUtils;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;
import com.xinyu.newdiggtest.utils.WechatRequsetUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BangMobileActivity extends BaseNoEventActivity implements View.OnClickListener {


    @BindView(R.id.edt_name)
    EditText edt_name;

    @BindView(R.id.error_code)
    TextView error_code;//验证码错误


    @BindView(R.id.edt_phone)
    EditText edt_phone;

    @BindView(R.id.edt_code)
    EditText edt_code;

    @BindView(R.id.counttextview)
    TextView counttextview;


    @BindView(R.id.line1)
    View line1;

    @BindView(R.id.line2)
    View line2;

    @BindView(R.id.line3)
    View line3;


    @Override
    protected int getLayoutResouce() {
        return R.layout.bang_mobile;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

    }

    private void initView() {

        edt_name.addTextChangedListener(new TextWatcher() {
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


        edt_phone.addTextChangedListener(new TextWatcher() {
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


        edt_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                resetView();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() > 0) {
                    line3.setBackgroundColor(mContext.getResources().getColor(R.color.button_vip));
                } else {
                    line3.setBackgroundColor(mContext.getResources().getColor(R.color.bar_grey));
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        counttextview.setOnClickListener(this);

    }

    private void resetView() {
        line1.setBackgroundColor(mContext.getResources().getColor(R.color.bar_grey));
        line2.setBackgroundColor(mContext.getResources().getColor(R.color.bar_grey));
        line3.setBackgroundColor(mContext.getResources().getColor(R.color.bar_grey));

    }

    @OnClick(R.id.back)
    public void goBack() {
        finish();
    }


    @OnClick(R.id.btn_register)
    public void goCommit() {


        if (MyTextUtil.isEmpty(edt_phone.getText().toString())) {
            ToastUtils.getInstanc().showToast("请输入手机号码");
            return;
        }

        if (!AppUtils.isCellphone(edt_phone.getText().toString())) {
            ToastUtils.getInstanc().showToast("手机号码不合法, 请重新输入");
            return;
        }


        if (MyTextUtil.isEmpty(edt_code.getText().toString())) {
            ToastUtils.getInstanc().showToast("请输入验证码");
            return;
        }

        goBang();

    }

    /**
     * 绑定手机号
     */
    public void goBang() {
        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        final NetApi url = manager.create(NetApi.class);
        HashMap<String, String> map = new HashMap<>();
        if (!MyTextUtil.isEmpty(edt_name.getText().toString())) {
            map.put("name", edt_name.getText().toString());
        }

        map.put("user_id", getIntent().getStringExtra("userId"));
        map.put("sid", getIntent().getStringExtra("sid"));
        map.put("phone", edt_phone.getText().toString().trim());
        map.put("code", edt_code.getText().toString().trim());


        url.wxBangPhone(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Info>() {
                    @Override
                    public void onCompleted() {
                        loadingDailog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(Info msg) {

                        if (msg.getOp().getCode().equals("Y") && msg.getOp().getCode().equals("绑定成功")) {

                            if (MyTextUtil.isEmpty(PreferenceUtil.getInstance(mContext).getCompanyId())) {

                                new WechatRequsetUtil(mContext).getCompanyList(getIntent().getStringExtra("userId"),
                                        edt_phone.getText().toString().trim());

                            } else {
                                startActivity(new Intent(mContext, HomeAppActivity.class));
                            }

                        } else {
                            Log.e("amtf", "服务onError:手机号绑定异常");
                        }


                    }
                });
    }


    public void sendCode() {

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


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.counttextview) {

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
                    sendCode();
                }
            });

            AppUtils.rxCountCode(60, counttextview, mContext);
        }


    }
}
