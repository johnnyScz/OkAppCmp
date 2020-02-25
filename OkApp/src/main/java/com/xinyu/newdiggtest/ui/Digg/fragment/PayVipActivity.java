package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.AccountBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.ui.BaseActivity;
import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.utils.AppMD5Util;
import com.xinyu.newdiggtest.utils.AppUtils;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.MyMiddleDialog;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;
import com.xinyu.newdiggtest.wxapi.PayResult;
import com.xinyu.newdiggtest.wxapi.WxPayUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * 支付Vip页面
 */
public class PayVipActivity extends BaseActivity {


    @BindView(R.id.tiaozhan)
    public TextView wolletMoney;

    @BindView(R.id.debt_money)
    public TextView debtMoney;

    @BindView(R.id.tv_ye)
    public TextView yuE;

    @BindView(R.id.title_level)
    public TextView title1;

    @BindView(R.id.title2)
    public TextView title2;

    @BindView(R.id.parentll)
    public LinearLayout parentll;


    @BindView(R.id.btn_pay)
    public Button goPay;

    String debet = "98", bangPhone = "0", banance = "0";


    int payType = 0;//0支付宝 1 微信 2 钱包
    String zfbMoney = "0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initPayView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        requestAccount();
    }

    private void initView() {
        //--------支付必要的参数---------
        debtMoney.setText("￥" + debet);
        title1.setText("VIP支付");
        title2.setText("VIP会员");
    }


    private void initPayView() {
        int count = parentll.getChildCount();
        for (int i = 0; i < count; i++) {
            final LinearLayout child = (LinearLayout) parentll.getChildAt(i);
            child.setTag(false);
            final int index = i;
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetView(index);
                    boolean isChecl = !(boolean) child.getTag();
                    child.setTag(isChecl);
                    if (isChecl) {
                        child.setBackgroundResource(R.drawable.shaper_wallet);
                        payType = index;
                    } else {
                        payType = -1;
                        child.setBackgroundResource(R.drawable.shaper_bg_ed);
                    }
                    checkEnable(payType);
                }
            });


        }
        defafutView();
    }

    private void checkEnable(int payType) {
        if (payType == -1) {
            yuE.setVisibility(View.VISIBLE);
            goPay.setEnabled(false);
            goPay.setBackgroundResource(R.drawable.btn_pay_disable);
        } else if (payType == 0 || payType == 1) {
            yuE.setVisibility(View.INVISIBLE);
            goPay.setBackgroundResource(R.drawable.btn_login_select);
            goPay.setEnabled(true);
        } else if (payType == 2) {
            if (Float.parseFloat(banance) < Float.parseFloat(debet)) {
                yuE.setVisibility(View.VISIBLE);
                goPay.setEnabled(false);
                goPay.setBackgroundResource(R.drawable.btn_pay_disable);
            } else {
                yuE.setVisibility(View.INVISIBLE);
                goPay.setEnabled(true);
                goPay.setBackgroundResource(R.drawable.btn_login_select);
            }
        }

    }

    private void defafutView() {
        LinearLayout third = (LinearLayout) parentll.getChildAt(2);
        third.setTag(true);
        third.setBackgroundResource(R.drawable.shaper_wallet);
        payType = 2;
    }

    private void resetView(int index) {
        int count = parentll.getChildCount();
        for (int i = 0; i < count; i++) {
            if (i != index) {
                LinearLayout child = (LinearLayout) parentll.getChildAt(i);
                child.setTag(false);
                child.setBackgroundResource(R.drawable.shaper_bg_ed);
            }
        }
    }


    public void finaPay() {
        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        map.put("sid", PreferenceUtil.getInstance(this).getSessonId());
        map.put("type", "2");//2支出/1收入
        map.put("money", debet);
        map.put("m_type", "3");

        url.payMoney(map).subscribeOn(Schedulers.io())
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
                        if (msg.getOp().getCode().equals("Y")) {

                            Intent intent = new Intent(mContext, PaySuccedActivity.class);
                            intent.putExtra("payMoney", debet);
                            intent.putExtra(IntentParams.FromVIP, "Y");//从vip页面过来的
                            mContext.startActivity(intent);
                            finish();

                        } else {
                            /**
                             * 支付失败
                             */
                            Intent intent = getIntent();
                            intent.setClass(mContext, PayFailActivity.class);
                            mContext.startActivity(intent);
                            finish();
                        }
                    }
                });
    }


    private void checkAcount() {
        if (Float.parseFloat(banance) == 0 || Float.parseFloat(banance) < Float.parseFloat(debet)) {
            yuE.setVisibility(View.VISIBLE);
            goPay.setEnabled(false);
            goPay.setBackgroundResource(R.drawable.btn_pay_disable);

        } else {
            goPay.setEnabled(true);
            yuE.setVisibility(View.INVISIBLE);
            goPay.setBackgroundResource(R.drawable.btn_login_select);

        }

    }


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_pay_rism;
    }


    @OnClick(R.id.icon_back)
    public void goCommit() {
        finish();
    }


    @OnClick(R.id.btn_pay)
    public void goPayOrder() {
        if (payType == -1) {
            ToastUtils.getInstanc(mContext).showToast("请选择支付方式！");
            return;
        }

        if (payType == 2) {//钱包支付
            //因为钱包需要支付密码，如果没有绑定手机号，肯定没有设置支付密码
            if (MyTextUtil.isEmpty(bangPhone)) {
                Intent intent = new Intent(mContext, PayPwdActivity.class);
                intent.putExtra(IntentParams.FirstBangPhone, "bang_phone");
                startActivity(intent);
                return;
            }
            showMyDialog();

        } else {
            paySwich();
        }

    }


    private void paySwich() {
        switch (payType) {
            case 0:
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_PHONE_STATE)
                        != PERMISSION_GRANTED) {
                    //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_PHONE_STATE)) {

                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.READ_PHONE_STATE,}, 1);
                    }
                } else {
                    zfbPay();
                }
                break;

            case 1:
                payWx();
                break;


        }


    }


    MyMiddleDialog myMiddleDialog;

    private void showMyDialog() {
        if (myMiddleDialog == null) {
            myMiddleDialog = new MyMiddleDialog(this, R.style.MyMiddleDialogStyle) {
                @Override
                protected View getView() {
                    LayoutInflater inflater = LayoutInflater.from(mContext);
                    View view = inflater.inflate(R.layout.dialog_pwd, null);
                    initDialogView(view);
                    return view;
                }
            };
        }
        myMiddleDialog.show();
    }


    EditText ed;

    private void initDialogView(View view) {

        ed = view.findViewById(R.id.edt_pwd);

        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMiddleDialog.dismiss();
                ed.setText("");
            }
        });

        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMiddleDialog.dismiss();
                ed.setText("");
            }
        });

        view.findViewById(R.id.conform).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(ed.getText().toString()) || ed.getText().toString().length() != 6) {
                    ToastUtils.getInstanc(mContext).showToast("请输入6位密码");
                    return;
                }
                checkPwd(ed.getText().toString());
                myMiddleDialog.dismiss();
                ed.setText("");
            }
        });


        view.findViewById(R.id.tv_forget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.setClass(mContext, PayPwdActivity.class);
                mContext.startActivity(intent);
                ed.setText("");
                myMiddleDialog.dismiss();
            }
        });

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
                            if (msg.getData() == null) {
                                banance = "0";
                                wolletMoney.setText("钱包:" + banance);
                            } else {
                                banance = msg.getData().getF_balance().trim();
                                wolletMoney.setText("钱包:" + banance);
                                bangPhone = msg.getData().getF_phone();
                                if (!MyTextUtil.isEmpty(bangPhone) &&
                                        MyTextUtil.isEmpty(PreferenceUtil.getInstance(mContext).getPhone())) {

                                    PreferenceUtil.getInstance(mContext).setMobilePhone(bangPhone);
                                }

                            }

                            checkAcount();

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }

                    }
                });
    }


    private void payWx() {
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("command", "ok-api.TargetOrderForSelfAction");
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("money", debet);
        requsMap.put("prod_name", "VIP支付");
        requsMap.put("payer", PreferenceUtil.getInstance(mContext).getUserId());//付款人
        requsMap.put("pay_type", "1");//1微信 0支付宝
        requsMap.put("systemno", "ok");
        requsMap.put("user_client_ip", AppUtils.getIpStr());

        requsMap.put("type", "3");//0 挑战金 1 打赏 2.奖励金
        requsMap.put("inorout_type", "2");//1 收入 2.支出
        requsMap.put("desc", "vip会员");

        WxPayUtil payUtil = WxPayUtil.getInstance(mContext);
        payUtil.setDialog(loadingDailog);
        payUtil.getReq(requsMap);

    }


    private void zfbPay() {
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("command", "ok-api.TargetOrderForSelfAction");
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("money", debet);
        requsMap.put("prod_name", "Vip支付");

        requsMap.put("payer", PreferenceUtil.getInstance(mContext).getUserId());//付款人
        requsMap.put("pay_type", "2");//1微信 2 支付宝
        requsMap.put("systemno", "ok");
        requsMap.put("user_client_ip", AppUtils.getIpStr());

        requsMap.put("type", "3");//0 挑战金 1 打赏 2.奖励金 3vip

        requsMap.put("inorout_type", "2");//1 收入 2.支出
        requsMap.put("desc", "vip会员");

        WxPayUtil payUtil = WxPayUtil.getInstance(mContext);
        payUtil.setDialog(loadingDailog);
        payUtil.setHandler(mhandler);
        payUtil.getZfbReq(requsMap, this);
    }


    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == EventConts.MSG_WX_Succed) {
                Map<String, String> meess = (Map) msg.obj;

                PayResult payResult = new PayResult((meess));

                String resultInfo = payResult.getResultStatus();// 同步返回需要验证的信息
                if (resultInfo.equals("6001")) {
                    ToastUtils.getInstanc(App.mContext).showToast("支付取消");
                } else if (resultInfo.equals("9000")) {
                    String result = payResult.getResult();
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONObject child = object.getJSONObject("alipay_trade_app_pay_response");
                        zfbMoney = child.getString("total_amount");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent itent = new Intent(mContext, PaySuccedActivity.class);
                    itent.putExtra(IntentParams.FromVIP, "Y");//从vip页面过来的
                    itent.putExtra("payMoney", debet);
                    startActivity(itent);
                    finish();

                } else {
                    startActivity(new Intent(mContext, PayFailActivity.class));
                }
            }
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PERMISSION_GRANTED) {
                zfbPay();
            } else {

            }
        }
    }


    int codeTime = 0;

    //这是因为微信会签名两次，这个是正常情况

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onXshellEvent(XshellEvent event) {
        if (event.what == EventConts.MSG_WX_Succed) {
            codeTime++;
            if (codeTime > 1) {
                return;
            }
            codeTime = 0;

            Intent itent = new Intent(mContext, PaySuccedActivity.class);
            itent.putExtra(IntentParams.FromVIP, "Y");//从vip页面过来的
            itent.putExtra("payMoney", debet);
            startActivity(itent);
            finish();

        } else if (event.what == EventConts.MSG_WX_Failure) {
            codeTime++;
            if (codeTime > 1) {
                return;
            }
            startActivity(new Intent(mContext, PayFailActivity.class));
            codeTime = 0;
        }

    }


    private void checkPwd(String pwd) {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);

        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        requsMap.put("pwd", AppMD5Util.MD5(pwd));
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());

        url.checkPwd(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Info>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(Info msg) {
                        if (msg.getOp().getCode().equals("Y")) {
                            finaPay();
                        } else {
                            ToastUtils.getInstanc(mContext).showToast("密码错误！");
                        }
                    }
                });

    }


}




