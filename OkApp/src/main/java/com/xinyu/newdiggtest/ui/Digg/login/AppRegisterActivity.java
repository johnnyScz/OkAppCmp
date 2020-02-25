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
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.AppUtils;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AppRegisterActivity extends BaseNoEventActivity implements View.OnClickListener {


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
        return R.layout.digg_register1;
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

        if (MyTextUtil.isEmpty(edt_name.getText().toString())) {
            ToastUtils.getInstanc().showToast("请输入您的姓名");
            return;
        }

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

        goRigester();

    }

    /**
     * 注册信息
     */

    public void goRigester() {

        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("name", edt_name.getText().toString());
        map.put("phonenum", edt_phone.getText().toString());
        map.put("code", edt_code.getText().toString());

        url.regeister(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Info>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(Info msg) {
                        loadingDailog.dismiss();

                        if (msg.getOp().getCode().equals("Y")) {

                            String info = msg.getOp().getInfo();

                            if (info.equals("注册成功")) {
                                Intent intent = new Intent(mContext, AppRegisterBangWxActivity.class);
                                intent.putExtra("user_id", msg.getUserid());
                                startActivity(intent);
                                finish();
                            } else {
                                if (info.equals("请重新获取验证码")) {
                                    error_code.setVisibility(View.GONE);
                                    ToastUtils.getInstanc().showToast(info);
                                    return;
                                } else if (info.equals("手机号已注册")) {
                                    ToastUtils.getInstanc().showToast(info);
                                    Intent intent = new Intent(mContext, AppLoginActivity.class);
                                    intent.putExtra("phone", edt_phone.getText().toString());
                                    startActivity(intent);
                                    finish();
                                    return;
                                } else if (info.equals("验证码不正确")) {
                                    error_code.setVisibility(View.VISIBLE);
                                    return;
                                }
                            }

                        } else {
                            ToastUtils.getInstanc().showToast("服务异常");
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
