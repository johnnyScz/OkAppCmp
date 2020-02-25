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
import com.xinyu.newdiggtest.bean.DashangBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.net.bean.paramsForIntent;
import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.ui.BaseActivity;
import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.utils.AppMD5Util;
import com.xinyu.newdiggtest.utils.AppUtils;
import com.xinyu.newdiggtest.utils.DateUtil;
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

public class PayRismActivity extends BaseActivity {


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

    String debet, bangPhone, banance;

    String mType = "0";//  0 挑战金 1 打赏 2奖励金
    String relevant_type = "0";//relevant_type 0目标 1打卡
    String cushNow = "";

    int payType = 0;//0支付宝 1 微信 2 钱包
    String zfbMoney = "0";

    String isFinish = "0";


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
        cushNow = getIntent().getStringExtra(IntentParams.Now_cash);
        mType = getIntent().getStringExtra(IntentParams.Pay_m_type);
        relevant_type = getIntent().getStringExtra(IntentParams.Pay_relevant_type);
        isFinish = getIntent().hasExtra(IntentParams.IsTarget_Finish) ? getIntent().getStringExtra(IntentParams.IsTarget_Finish) : "0";

        debet = getIntent().getStringExtra("money");
        debtMoney.setText("￥" + debet);
        if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("tiaozhan_fine")) {
            title1.setText("支付挑战金");
            title2.setText("挑战金");
        } else if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("daka_dashang")) {
            title1.setText("支付打赏");
            title2.setText("打赏金");
        } else if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("target_reward")) {
            title1.setText("支付奖励金");
            title2.setText("奖励金");
        }

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


//    public void finaPay() {
//        loadingDailog.show();
//        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
//        AppUrl url = manager.create(AppUrl.class);
//        HashMap<String, String> map = new HashMap<>();
//        map.put("user_id", PreferenceXshellUtil.getInstance(mContext).getUserId());
//        map.put("sid", PreferenceXshellUtil.getInstance(this).getSessonId());
//        map.put("type", "2");//2支出/1收入
//        map.put("money", getIntent().getStringExtra("money"));
//        map.put("relevant_uuid", getIntent().getStringExtra(IntentParams.DAKA_Target_Id));
//
//        if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("tiaozhan_fine")) {
//            //挑战金不用传toUser
//            map.put("m_type", "0");// 0 挑战金 1 打赏 2奖励金
//            map.put("relevant_type", "0");//（0目标，1打卡）
//        } else if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("target_reward")) {
//            map.put("m_type", "2");
//            map.put("relevant_type", "0");
//            String state = getIntent().getStringExtra(IntentParams.Target_State);
//            if (!MyTextUtil.isEmpty(state) && state.equals("1")) {
//                map.put("to_user_id", paramsForIntent.TargetReward.toUser);
//            }
//
//        } else if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("daka_dashang")) {
//            map.put("m_type", "1");//0 挑战金 1 打赏 2奖励金
//            map.put("relevant_type", "1");//（0目标，1打卡）
//            map.put("to_user_id", getIntent().getStringExtra(IntentParams.TO_USER));
//        }
//        url.payMoney(map).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Info>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        loadingDailog.dismiss();
//                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
//
//                    }
//
//                    @Override
//                    public void onNext(Info msg) {
//                        loadingDailog.dismiss();
//                        if (msg.getOp().getCode().equals("Y")) {
//                            Intent intent = new Intent(mContext, PaySuccedActivity.class);
//                            intent.putExtra("payMoney", debet);
//                            mContext.startActivity(intent);
//                            finish();
//
//                        } else {
//                            /**
//                             * 支付失败
//                             */
//                            Intent intent = getIntent();
//                            intent.setClass(mContext, PayFailActivity.class);
//                            mContext.startActivity(intent);
//                            finish();
//                        }
//                    }
//                });
//    }


    /**
     * 目标支付挑战金
     */
    private void goTargetBangFirst() {
        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);

        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("target_id", getIntent().getStringExtra(IntentParams.DAKA_Target_Id));
        requsMap.put("fine", debet);
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        url.targetPayFinish(requsMap).subscribeOn(Schedulers.io())
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
                            mContext.startActivity(intent);
                            finish();
//                            if (payType == 2) {
//                                finaPay();
//                            } else {
//
//                            }

                        } else {
                            Intent intent = getIntent();
                            intent.setClass(mContext, PayFailActivity.class);
                            mContext.startActivity(intent);
                            finish();
                        }

                    }
                });

    }


    private void checkAcount() {
        if (Float.parseFloat(banance) < Float.parseFloat(debet)) {
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


    /**
     * 创建 奖励金/打赏金[挑战金不会走这里]
     */
    private void ceateDashang2Reword() {
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();

        if (getIntent().hasExtra(IntentParams.Wish_Word)) {
            String wish = getIntent().getStringExtra(IntentParams.Wish_Word);

            map.put("wish", "");

        }

        if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("daka_dashang")) {
            map.put("aim_id", getIntent().getStringExtra(IntentParams.DAKA_Target_Id));
            map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
            map.put("money", getIntent().getStringExtra("money"));
            map.put("status", "0");//0是立即可以领取，1是等待结算
            map.put("type", "0");//0打卡打赏，立即结算。1表示奖励金，等待结算
            map.put("nick_name", PreferenceUtil.getInstance(mContext).getNickName());
            map.put("hearten_user_id", getIntent().getStringExtra(IntentParams.TO_USER));

            if (!MyTextUtil.isEmpty(paramsForIntent.TargetDakaDashang.start_time)) {
                map.put("start_date", paramsForIntent.TargetDakaDashang.start_time);
            } else {
                map.put("start_date", DateUtil.getCurrentData());
            }
            if (!MyTextUtil.isEmpty(paramsForIntent.TargetDakaDashang.end_time)) {
                map.put("end_date", paramsForIntent.TargetDakaDashang.end_time);
            } else {
                map.put("end_date", DateUtil.getCurrentData());
            }
            map.put("target_name", getIntent().getStringExtra(IntentParams.Target_Name));

            map.put("target_name", getIntent().getStringExtra(IntentParams.Target_Name));

        } else if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("target_reward")) {
            if (paramsForIntent.TargetReward == null)
                return;
            map.put("aim_id", paramsForIntent.TargetReward.target_id);
            map.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
            map.put("money", getIntent().getStringExtra("money"));
            if (isFinish.equals("1")) {
                map.put("status", "0");//0是立即可以领取，1是等待结算
            } else {
                map.put("status", "1");//0是立即可以领取，1是等待结算
            }
            map.put("type", "1");//0打卡打赏，立即结算。1表示奖励金，等待结算
            map.put("nick_name", PreferenceUtil.getInstance(mContext).getNickName());

            map.put("hearten_user_id", paramsForIntent.TargetReward.toUser);
            map.put("start_date", paramsForIntent.TargetReward.start_time);
            map.put("end_date", paramsForIntent.TargetReward.end_time);
            map.put("target_name", getIntent().getStringExtra(IntentParams.Target_Name));
        }

        url.targetDashagn(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DashangBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(DashangBean msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {

                            Intent intent = new Intent(mContext, PaySuccedActivity.class);
                            intent.putExtra("payMoney", debet);
                            mContext.startActivity(intent);
                            finish();

//                            if (payType == 2) {
//                                finaPay();
//                            } else {
//
//                            }
                        } else {
                            Intent intent = getIntent();
                            intent.setClass(mContext, PayFailActivity.class);
                            mContext.startActivity(intent);
                            finish();
                        }

                    }
                });
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
        requsMap.put("prod_name", "订单支付");

        requsMap.put("payer", PreferenceUtil.getInstance(mContext).getUserId());//付款人
        requsMap.put("pay_type", "1");//1微信 0支付宝
        requsMap.put("systemno", "ok");
        requsMap.put("user_client_ip", AppUtils.getIpStr());
        requsMap.put("state", isFinish);

        if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("daka_dashang")) {
            requsMap.put("payee", getIntent().getStringExtra(IntentParams.TO_USER));//收款人
            requsMap.put("type", "1");//0 挑战金 1 打赏 2.奖励金
            requsMap.put("relevant_type", "1");//0 目标 1.打卡
            requsMap.put("inorout_type", "2");//1 收入 2.支出
            requsMap.put("desc", "打赏支付");
            requsMap.put("relevant_uuid", paramsForIntent.TargetDakaDashang.target_id);//目标或者打卡的id
            requsMap.put("start_time", paramsForIntent.TargetDakaDashang.start_time);
            requsMap.put("end_time", paramsForIntent.TargetDakaDashang.end_time);
            requsMap.put("object_name", paramsForIntent.TargetDakaDashang.object_name);

        } else if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("tiaozhan_fine")) {
            requsMap.put("payee", PreferenceUtil.getInstance(mContext).getUserId());
            requsMap.put("type", "0");//0 挑战金 1 打赏 2.奖励金
            requsMap.put("relevant_type", "0");//0 目标 1.打卡
            requsMap.put("inorout_type", "2");//1 收入 2.支出
            requsMap.put("desc", "挑战金支付");
            requsMap.put("relevant_uuid", paramsForIntent.TargetTiaozhan.target_id);//目标或者打卡的id
            requsMap.put("start_time", paramsForIntent.TargetTiaozhan.start_time);
            requsMap.put("end_time", paramsForIntent.TargetTiaozhan.end_time);
            requsMap.put("object_name", paramsForIntent.TargetTiaozhan.object_name);

        } else if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("target_reward")) {

            requsMap.put("payee", getIntent().getStringExtra(IntentParams.TO_USER));
            requsMap.put("type", "2");//0 挑战金 1 打赏 2.奖励金
            requsMap.put("relevant_type", "0");//0 目标 1.打卡
            requsMap.put("inorout_type", "2");//1 收入 2.支出
            requsMap.put("desc", "奖励金支付");
            requsMap.put("relevant_uuid", paramsForIntent.TargetReward.target_id);//目标或者打卡的id
            requsMap.put("start_time", paramsForIntent.TargetReward.start_time);
            requsMap.put("end_time", paramsForIntent.TargetReward.end_time);
            requsMap.put("object_name", paramsForIntent.TargetReward.object_name);

        }

        WxPayUtil payUtil = WxPayUtil.getInstance(mContext);
        payUtil.setDialog(loadingDailog);
        payUtil.getReq(requsMap);

    }


    private void zfbPay() {
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("command", "ok-api.TargetOrderForSelfAction");
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("money", debet);
        requsMap.put("prod_name", "订单支付");

        requsMap.put("payer", PreferenceUtil.getInstance(mContext).getUserId());//付款人
        requsMap.put("pay_type", "2");//1微信 2 支付宝
        requsMap.put("systemno", "ok");
        requsMap.put("user_client_ip", AppUtils.getIpStr());
        requsMap.put("state", isFinish);

        if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("daka_dashang")) {
            requsMap.put("payee", getIntent().getStringExtra(IntentParams.TO_USER));//收款人
            requsMap.put("type", "1");//0 挑战金 1 打赏 2.奖励金
            requsMap.put("relevant_type", "1");//0 目标 1.打卡
            requsMap.put("inorout_type", "2");//1 收入 2.支出
            requsMap.put("desc", "打赏支付");
            requsMap.put("relevant_uuid", paramsForIntent.TargetDakaDashang.target_id);//目标或者打卡的id
            requsMap.put("start_time", paramsForIntent.TargetDakaDashang.start_time);
            requsMap.put("end_time", paramsForIntent.TargetDakaDashang.end_time);
            requsMap.put("object_name", paramsForIntent.TargetDakaDashang.object_name);

        } else if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("tiaozhan_fine")) {
            requsMap.put("payee", PreferenceUtil.getInstance(mContext).getUserId());
            requsMap.put("type", "0");//0 挑战金 1 打赏 2.奖励金
            requsMap.put("relevant_type", "0");//0 目标 1.打卡
            requsMap.put("inorout_type", "2");//1 收入 2.支出
            requsMap.put("desc", "挑战金支付");
            requsMap.put("relevant_uuid", paramsForIntent.TargetTiaozhan.target_id);//目标或者打卡的id
            requsMap.put("start_time", paramsForIntent.TargetTiaozhan.start_time);
            requsMap.put("end_time", paramsForIntent.TargetTiaozhan.end_time);
            requsMap.put("object_name", paramsForIntent.TargetTiaozhan.object_name);

        } else if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("target_reward")) {

            requsMap.put("payee", getIntent().getStringExtra(IntentParams.TO_USER));

            requsMap.put("type", "2");//0 挑战金 1 打赏 2.奖励金
            requsMap.put("relevant_type", "0");//0 目标 1.打卡
            requsMap.put("inorout_type", "2");//1 收入 2.支出
            requsMap.put("desc", "奖励金支付");
            requsMap.put("relevant_uuid", paramsForIntent.TargetReward.target_id);//目标或者打卡的id
            requsMap.put("start_time", paramsForIntent.TargetReward.start_time);
            requsMap.put("end_time", paramsForIntent.TargetReward.end_time);
            requsMap.put("object_name", paramsForIntent.TargetReward.object_name);

        }


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
//                    if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("tiaozhan_fine")) {
//                        goTargetBangFirst();
//                    } else {
//                        ceateDashang2Reword();
//                    }
                    Intent intent = new Intent(mContext, PaySuccedActivity.class);
                    intent.putExtra("payMoney", debet);
                    mContext.startActivity(intent);
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
//            if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("tiaozhan_fine")) {
//                goTargetBangFirst();
//            } else {
//                ceateDashang2Reword();
//            }
            Intent intent = new Intent(mContext, PaySuccedActivity.class);
            intent.putExtra("payMoney", debet);
            mContext.startActivity(intent);
            finish();
            codeTime = 0;
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
                            if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("tiaozhan_fine")) {
                                goTargetBangFirst();
                            } else {
                                ceateDashang2Reword();
                            }
                        } else {
                            ToastUtils.getInstanc(mContext).showToast("密码错误！");
                        }
                    }
                });

    }


}




