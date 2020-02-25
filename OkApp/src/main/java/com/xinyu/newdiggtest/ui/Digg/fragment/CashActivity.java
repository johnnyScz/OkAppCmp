package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.AccountBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 提现
 */
public class CashActivity extends BaseNoEventActivity {


    @BindView(R.id.left_money)
    public TextView leftMoney;//可提现金额


    @BindView(R.id.edt_money)
    public EditText edt_money;//输入金额

    String yueE = "0";


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_cash;
    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rigester();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void rigester() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("cush_finish");
        registerReceiver(receiver, filter);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("cush_finish")) {
                finish();
            }

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        requestAccount();
    }

    private void requestAccount() {
        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);

        HashMap<String, String> requsMap = new HashMap<>();


        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        url.getAccountInfo(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AccountBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(AccountBean msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            if (msg.getData() == null || MyTextUtil.isEmpty(msg.getData().getF_balance())) {
                                yueE = "0";
                            } else {
                                yueE = msg.getData().getF_balance();
                            }
                            leftMoney.setText("可提现余额" + yueE + "元");
                        } else {
                            leftMoney.setText("可提现余额0元");
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }

                    }
                });
    }


    @OnClick(R.id.cash_all)
    public void cashAll() {
        edt_money.setText(yueE);
    }


    @OnClick(R.id.btn_cashout)
    public void cashOut() {
        if (MyTextUtil.isEmpty(edt_money.getText().toString().trim())) {
            ToastUtils.getInstanc().showToast("请输入提现金额！");
            return;
        }


        String moneydot = edt_money.getText().toString().trim();
        int index = moneydot.lastIndexOf(".");
        if (index != -1) {
            String behindDot = moneydot.substring(index + 1);
            if (!MyTextUtil.isEmpty(behindDot) && behindDot.length() > 2) {
                ToastUtils.getInstanc().showToast("请输入有效金额！");
                return;

            }
        }


        float inputMoney = Float.parseFloat(edt_money.getText().toString());


        float leftYue = Float.parseFloat(yueE);

        if (inputMoney < 10) {
            ToastUtils.getInstanc().showToast("最小提现金额为10元");
            return;
        }

        if (inputMoney > leftYue) {
            ToastUtils.getInstanc().showToast("余额不足，请重新输入");
            return;
        }

        goZfb();

    }

    /**
     * 跳到支付宝页面
     */
    private void goZfb() {
        Intent intent = new Intent(mContext, ZfbActivity.class);
        intent.putExtra("money", edt_money.getText().toString());
        startActivity(intent);

    }


}




