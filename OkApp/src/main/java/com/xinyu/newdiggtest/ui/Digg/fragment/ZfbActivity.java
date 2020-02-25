package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.ZfbInfo;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.MyMiddleDialog;
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
 * 支付宝账号
 */
public class ZfbActivity extends BaseNoEventActivity {


    @BindView(R.id.zfb_name)
    public EditText zfb_name;//支付宝姓名

    @BindView(R.id.zfb_account)
    public EditText zfb_account;//支付宝账号

    @BindView(R.id.cush_money)
    public TextView cush_money;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_zfb;
    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        cush_money.setText(getIntent().getStringExtra("money"));
    }

    @OnClick(R.id.btn_cashout)
    public void cashOut() {
        if (MyTextUtil.isEmpty(zfb_name.getText().toString().trim())) {
            ToastUtils.getInstanc().showToast("请输入真实姓名");
            return;
        }

        if (MyTextUtil.isEmpty(zfb_account.getText().toString().trim())) {
            ToastUtils.getInstanc().showToast("请输入支付宝账号");
            return;
        }

        goZfbOrder();

    }

    /**
     * 生成提现订单
     */

    private void goZfbOrder() {
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("command", "ok-api.TargetOrderForSelfAction");
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("money", getIntent().getStringExtra("money"));
        requsMap.put("prod_name", "支付宝提现");

        requsMap.put("payee", PreferenceUtil.getInstance(mContext).getUserId());//收款人
        requsMap.put("pay_type", "2");//1微信 2 支付宝
        requsMap.put("type", "7");//提现
        requsMap.put("systemno", "ok");

        requsMap.put("inorout_type", "3");//1 收入 2.支出 3.提现
        requsMap.put("desc", "支付宝提现");
        requsMap.put("username", zfb_name.getText().toString());
        requsMap.put("accounts", zfb_account.getText().toString());


        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        url.ZfbPay(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ZfbInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(ZfbInfo msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            showMyDialog();
                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }

                    }
                });
    }

    /**
     * 显示提现审核日期
     */


    MyMiddleDialog myMiddleDialog;

    private void showMyDialog() {
        if (myMiddleDialog == null) {
            myMiddleDialog = new MyMiddleDialog(this, R.style.MyMiddleDialogStyle) {
                @Override
                protected View getView() {
                    LayoutInflater inflater = LayoutInflater.from(mContext);
                    View view = inflater.inflate(R.layout.dialog_zfb, null);
                    initDialogView(view);
                    return view;
                }
            };
        }
        myMiddleDialog.show();
    }

    private void initDialogView(View view) {

        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMiddleDialog.dismiss();
            }
        });

        view.findViewById(R.id.i_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMiddleDialog.dismiss();
                close2FrontPage();
            }
        });


    }

    /**
     * 关闭自己以及前一个页面
     */
    private void close2FrontPage() {
        Intent intent = new Intent("cush_finish");
        sendBroadcast(intent);
        finish();
    }

}




